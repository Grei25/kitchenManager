const mssql = require('mssql');

async function fix() {
  try {
    await mssql.connect({
      server: 'localhost',
      user: 'sa',
      password: '123',
      database: 'kitchenmanager_db',
      options: { trustServerCertificate: true }
    });
    console.log('Connected');

    await mssql.query("ALTER TABLE orders DROP CONSTRAINT CK__orders__status__4D94879B");
    console.log('Dropped');

    await mssql.query("ALTER TABLE orders ADD CONSTRAINT CK_orders_status CHECK (status IN ('NEW', 'ACCEPTED', 'COOKING', 'READY', 'COMPLETED', 'CONFIRMED', 'DELIVERING', 'DELIVERED', 'CANCELLED'))");
    console.log('Added');

    await mssql.close();
  } catch (e) {
    console.error(e);
  }
}

fix();