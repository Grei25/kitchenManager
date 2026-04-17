$conn = New-Object System.Data.SqlClient.SqlConnection
$conn.ConnectionString = "Server=localhost;Database=kitchenmanager_db;Integrated Security=True;TrustServerCertificate=True"
$conn.Open()

# Drop constraint
$cmd = $conn.CreateCommand()
$cmd.CommandText = "ALTER TABLE orders DROP CONSTRAINT CK__orders__status__4D94879B"
$cmd.ExecuteNonQuery()
Write-Host "Dropped"

# Add new constraint
$cmd.CommandText = "ALTER TABLE orders ADD CONSTRAINT CK_orders_status CHECK (status IN ('NEW','ACCEPTED','COOKING','READY','COMPLETED','CONFIRMED','DELIVERING','DELIVERED','CANCELLED'))"
$cmd.ExecuteNonQuery()
Write-Host "Added"

# Verify
$cmd.CommandText = "SELECT name FROM sys.check_constraints WHERE parent_object_id = OBJECT_ID('orders')"
$reader = $cmd.ExecuteReader()
while ($reader.Read()) { Write-Host $reader[0] }
$reader.Close()

$conn.Close()
Write-Host "Done"