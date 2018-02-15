package lv.k2611a.app;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

@Configuration
public class DataSourceFactory implements ApplicationListener<EnvironmentChangeEvent> {

    @Autowired
    private Environment environment;

    private volatile DataSource currentDataSource;

    @PostConstruct
    public void init() {
        refreshDatasource();
    }

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        refreshDatasource();
    }

    private void refreshDatasource() {
        String datasourceUrl = environment.getProperty("spring.datasource.url");
        currentDataSource = DataSourceBuilder
                .create()
                .username("")
                .password("")
                .url(datasourceUrl)
                .driverClassName("org.hsqldb.jdbc.JDBCDriver")
                .build();
    }

    @Bean
    @Primary
    public DataSource buildDatasource() {
        return new DatasourceProxy();
    }


    private final class DatasourceProxy implements DataSource {
        @Override
        public Connection getConnection() throws SQLException {
            return currentDataSource.getConnection();
        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            return currentDataSource.getConnection(username, password);
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return currentDataSource.unwrap(iface);
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return currentDataSource.isWrapperFor(iface);
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return currentDataSource.getLogWriter();
        }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {
            throw new IllegalStateException("Not supported");
        }

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {
            throw new IllegalStateException("Not supported");
        }

        @Override
        public int getLoginTimeout() throws SQLException {
            return currentDataSource.getLoginTimeout();
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return currentDataSource.getParentLogger();
        }
    }


}
