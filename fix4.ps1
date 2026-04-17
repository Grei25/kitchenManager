$conn = New-Object System.Data.SqlClient.SqlConnection
$conn.ConnectionString = "Server=localhost;Database=kitchenmanager_db;User ID=kitchen_user;Password=123;TrustServerCertificate=True"
$conn.Open()

$cmd = $conn.CreateCommand()
$cmd.CommandText = "SELECT name FROM sys.check_constraints WHERE parent_object_id = OBJECT_ID('orders')"
$reader = $cmd.ExecuteReader()
Write-Host "Current constraints:"
while ($reader.Read()) { Write-Host "  - $($reader[0])" }
$reader.Close()

# Drop old constraint if exists
$cmd.CommandText = "IF EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = 'CK__orders__status__4D94879B') ALTER TABLE orders DROP CONSTRAINT CK__orders__status__4D94879B"
$cmd.ExecuteNonQuery() | Out-Null
Write-Host "Dropped old constraint"

# Add new constraint
$cmd.CommandText = "ALTER TABLE orders ADD CONSTRAINT CK_orders_status CHECK (status IN ('NEW','ACCEPTED','COOKING','READY','COMPLETED','CONFIRMED','DELIVERING','DELIVERED','CANCELLED'))"
$cmd.ExecuteNonQuery() | Out-Null
Write-Host "Added new constraint"

$conn.Close()
Write-Host "Success!"