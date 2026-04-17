import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import java.sql.*;

public class FixConstraint {
    public static void main(String[] args) {
        try {
            DriverManager.registerDriver(new SQLServerDriver());
            Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=kitchenmanager_db;encrypt=true;trustServerCertificate=true",
                "kitchen_user", "123");

            System.out.println("Connected");

            // Delete orders
            Statement stmt = conn.createStatement();
            stmt.execute("DELETE FROM order_items");
            stmt.execute("DELETE FROM orders");
            System.out.println("Deleted orders");

            // Drop constraint
            try { stmt.execute("ALTER TABLE orders DROP CONSTRAINT CK__orders__status__4D94879B"); System.out.println("Dropped"); } catch(Exception e) {}

            // Add new constraint
            stmt.execute("ALTER TABLE orders ADD CONSTRAINT CK_orders_status CHECK (status IN ('NEW','ACCEPTED','COOKING','READY','COMPLETED','CONFIRMED','DELIVERING','DELIVERED','CANCELLED'))");
            System.out.println("Added new constraint");

            conn.close();
            System.out.println("Done!");
        } catch(Exception e) { e.printStackTrace(); }
    }
}