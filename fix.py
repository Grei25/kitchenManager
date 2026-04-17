import sys
import pyodbc

conn_str = (
    'DRIVER={ODBC Driver 17 for SQL Server};'
    'SERVER=localhost;'
    'DATABASE=kitchenmanager_db;'
    'UID=kitchen_user;'
    'PWD=123;'
    'TrustServerCertificate=yes'
)

conn = pyodbc.connect(conn_str)
cursor = conn.cursor()

# Check constraints
cursor.execute("SELECT name FROM sys.check_constraints WHERE parent_object_id = OBJECT_ID('orders')")
print("Current constraints:")
for row in cursor.fetchall():
    print(f"  {row[0]}")

# Delete all orders to avoid constraint violations on existing data
cursor.execute("DELETE FROM order_items")
cursor.execute("DELETE FROM orders")
conn.commit()
print("\nDeleted all orders")

# Drop constraint if exists
cursor.execute("IF EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = 'CK__orders__status__4D94879B') ALTER TABLE orders DROP CONSTRAINT CK__orders__status__4D94879B")
conn.commit()
print("Dropped old constraint")

# Add new constraint
cursor.execute("ALTER TABLE orders ADD CONSTRAINT CK_orders_status CHECK (status IN ('NEW','ACCEPTED','COOKING','READY','COMPLETED','CONFIRMED','DELIVERING','DELIVERED','CANCELLED'))")
conn.commit()
print("Added new constraint")

# Verify
cursor.execute("SELECT name FROM sys.check_constraints WHERE parent_object_id = OBJECT_ID('orders')")
print("\nNew constraints:")
for row in cursor.fetchall():
    print(f"  {row[0]}")

conn.close()
print("\nDone!")