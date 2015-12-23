package com.chuongntv.areaapp;

import com.jolbox.bonecp.BoneCPDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.mockito.InjectMocks;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by pm03 on 12/22/15.
 */
@Configuration
@PropertySource("classpath:application.properties")
public class PersistenceContext {

    protected static final String PROPERTY_NAME_DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    protected static final String PROPERTY_NAME_DATABASE_PASSWORD = "abc123@@";
    protected static final String PROPERTY_NAME_DATABASE_URL = "jdbc:mysql://localhost:3306/benxe";
    protected static final String PROPERTY_NAME_DATABASE_USERNAME = "root";
    @Resource
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        BoneCPDataSource dataSource = new BoneCPDataSource();

        dataSource.setDriverClass(environment.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER));
        dataSource.setJdbcUrl(environment.getRequiredProperty(PROPERTY_NAME_DATABASE_URL));
        dataSource.setUsername(environment.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));
        dataSource.setPassword(environment.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD));

        return dataSource;
    }
}