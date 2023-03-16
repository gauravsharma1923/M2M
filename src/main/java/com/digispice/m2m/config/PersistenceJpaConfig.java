package com.digispice.m2m.config;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.digispice.m2m.entity" })
@PropertySource({ "classpath:persistence-${persistenceTarget:dev}.properties" })
@EnableJpaRepositories(basePackages = "com.digispice.m2m.repository")

public class PersistenceJpaConfig {

    @Autowired
    private Environment env;

    public PersistenceJpaConfig() {
        super();
    }

    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(apiDataSource());
        em.setPackagesToScan(new String[] { "com.digispice.m2m.entity" });
        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(hibernateProperties());
        return em;
    }

   @Bean
   @Qualifier("apiDataSource")
   public HikariDataSource apiDataSource() {
       
    	final HikariDataSource hikariDataSource=new HikariDataSource(hikariConfig());
        return hikariDataSource;
    }
    
 
    @Bean
    public JpaTransactionManager transactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    final Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        hibernateProperties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        hibernateProperties.setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
        hibernateProperties.setProperty("hibernate.generate_statistics", env.getProperty("hibernate.generate_statistics"));
        return hibernateProperties;
    }
    
        final  HikariConfig hikariConfig() {
    	
        final HikariConfig hikariConfig =new HikariConfig();
        hikariConfig.setDriverClassName(env.getProperty("jdbc.driverClassName"));
	    hikariConfig.setJdbcUrl(env.getProperty("jdbc.url"));
	    hikariConfig.setUsername(env.getProperty("jdbc.username"));
	    hikariConfig.setPassword(env.getProperty("jdbc.password"));
	    hikariConfig.setMaximumPoolSize(Integer.parseInt(env.getProperty("hikari.maximum-pool-size")));
	    hikariConfig.setConnectionTimeout(Integer.parseInt(env.getProperty("hikari.connection-timeout")));
	    hikariConfig.addDataSourceProperty("cachePrepStatements",env.getProperty("hikari.cachePrepStmts"));
        hikariConfig.addDataSourceProperty( "prepStmtCacheSize" , env.getProperty("hikari.prepStmtCacheSize"));
        hikariConfig.addDataSourceProperty( "prepStmtCacheSqlLimit" , env.getProperty("hikari.prepStmtCacheSqlLimit"));
	    hikariConfig.setMinimumIdle(Integer.parseInt(env.getProperty("hikari.minimum-idle")));
	    hikariConfig.setIdleTimeout(Integer.parseInt(env.getProperty("hikari.idle-timeout")));
	    hikariConfig.setAutoCommit(Boolean.parseBoolean(env.getProperty("hikari.auto-commit")));
	    return hikariConfig;
     
  }
}