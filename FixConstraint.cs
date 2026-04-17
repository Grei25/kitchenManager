using System;
using Microsoft.Data.SqlClient;

class Program
{
    static void Main()
    {
        string connStr = "Server=localhost;Database=kitchenmanager_db;User Id=sa;Password=123;TrustServerCertificate=True;";
        using var conn = new SqlConnection(connStr);
        conn.Open();

        var cmd1 = new SqlCommand("ALTER TABLE orders DROP CONSTRAINT CK__orders__status__4D94879B", conn);
        cmd1.ExecuteNonQuery();
        Console.WriteLine("Constraint dropped");

        var cmd2 = new SqlCommand("ALTER TABLE orders ADD CONSTRAINT CK_orders_status CHECK (status IN ('NEW', 'ACCEPTED', 'COOKING', 'READY', 'COMPLETED', 'CONFIRMED', 'DELIVERING', 'DELIVERED', 'CANCELLED'))", conn);
        cmd2.ExecuteNonQuery();
        Console.WriteLine("Constraint added");
    }
}