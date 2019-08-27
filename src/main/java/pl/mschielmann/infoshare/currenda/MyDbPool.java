package pl.mschielmann.infoshare.currenda;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

class MyDbPool {
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/test";
    private static final String USER = "root";
    private static final String PASSWORD = "my-secret-pw";
    private static final BasicDataSource ds = new BasicDataSource();

    static {
        ds.setUrl(CONNECTION_STRING);
        ds.setUsername(USER);
        ds.setPassword(PASSWORD);
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
    }

    static DataSource getDataSource() {
        return ds;
    }
}
