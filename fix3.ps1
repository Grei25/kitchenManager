$conn = New-Object System.Data.SqlClient.SqlConnection
$conn.ConnectionString = "Server=localhost;Database=kitchenmanager_db;Integrated Security=True;TrustServerCertificate=True;MultipleActiveResultSets=true"
$conn.Open()

# Get all constraints on orders table
$cmd = $conn.CreateCommand()
$cmd.CommandText = "SELECT name FROM sys.check_constraints WHERE parent_object_id = OBJECT_ID('orders')"
$constraints = @()
$reader = $cmd.ExecuteReader()
while ($reader.Read()) { $constraints += $reader[0] }
$reader.Close()

Write-Host "Found constraints: $($constraints -join ', ')"

# Drop each constraint
foreach ($c in $constraints) {
    $cmd.CommandText = "ALTER TABLE orders DROP CONSTRAINT $c"
    try {
        $cmd.ExecuteNonQuery() | Out-Null
        Write-Host "Dropped: $c"
    } catch {
        Write-Host "Could not drop: $c"
    }
}

# Add new constraint
$cmd.CommandText = "ALTER TABLE orders ADD CONSTRAINT CK_orders_status CHECK (status IN ('NEW','ACCEPTED','COOKING','READY','COMPLETED','CONFIRMED','DELIVERING','DELIVERED','CANCELLED'))"
$cmd.ExecuteNonQuery()
Write-Host "Added new constraint"

$conn.Close()
Write-Host "Done"