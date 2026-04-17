$conn = New-Object System.Data.SqlClient.SqlConnection
$conn.ConnectionString = "Server=localhost;Database=kitchenmanager_db;Integrated Security=True;TrustServerCertificate=True"
$conn.Open()

# Check existing constraints
$cmd = $conn.CreateCommand()
$cmd.CommandText = "SELECT name FROM sys.check_constraints WHERE parent_object_id = OBJECT_ID('orders')"
$reader = $cmd.ExecuteReader()
Write-Host "Current constraints:"
while ($reader.Read()) { $reader["name"] }
$reader.Close()

# Drop all check constraints on status column
$cmd.CommandText = "ALTER TABLE orders DROP CONSTRAINT CK_orders_status"
try { $cmd.ExecuteNonQuery(); Write-Host "Dropped CK_orders_status" } catch { Write-Host "Already dropped" }

$cmd.CommandText = "ALTER TABLE orders DROP CONSTRAINT CK__orders__status__4D94879B"
try { $cmd.ExecuteNonQuery(); Write-Host "Dropped CK__orders__status__4D94879B" } catch { Write-Host "Already dropped" }

# Add new constraint
$cmd.CommandText = "ALTER TABLE orders ADD CONSTRAINT CK_orders_status CHECK (status IN ('NEW','ACCEPTED','COOKING','READY','COMPLETED','CONFIRMED','DELIVERING','DELIVERED','CANCELLED'))"
$cmd.ExecuteNonQuery()
Write-Host "Added new constraint"

$conn.Close()