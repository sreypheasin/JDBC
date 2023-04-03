package co.istad;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class jdbcImpl {
    public DataSource dataSource(){
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUser("postgres");
        dataSource.setPassword("170823");
        dataSource.setDatabaseName("student");
        return dataSource;
    }
}
