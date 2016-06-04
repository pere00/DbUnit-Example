package com.qsi.example.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.mysql.jdbc.Driver;

@Configuration
@EnableAspectJAutoProxy
@PropertySource("test-db.properties") 
@ComponentScan({"com.qsi.example"})
public class AbstractUnitConfig {
	
	@Value("${db.driver}") String driver;
    @Value("${db.dialect}") String dialect;
    @Value("${db.url}") String url;
    @Value("${db.username}") String username;
    @Value("${db.password}") String password;
    @Value("${db.hbm2ddl}") String hbm2ddl;
	
 
	@SuppressWarnings("unchecked")
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        try {
			dataSource.setDriverClass((Class<Driver>)Class.forName(driver));
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found for driver: " + driver + " Please provide a valid dataSource driver");
		}
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        
        return dataSource;
	}
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
      LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
      em.setDataSource(dataSource());

      JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

      em.setJpaVendorAdapter(vendorAdapter);
      em.setJpaProperties(additionalJpaProperties());


      return em;
    }

    Properties additionalJpaProperties() {
          Properties properties = new Properties();
          properties.setProperty("hibernate.hbm2ddl.auto", hbm2ddl);
          properties.setProperty("hibernate.dialect", dialect);
          properties.setProperty("hibernate.show_sql", "true");
          properties.setProperty("hibernate.query.jpaql_strict_compliance", "true");

          return properties;
    }
    
    @Bean
    public JdbcTemplate mysqlJdbcTemplate(@Qualifier("dataSource") final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory emf){
      JpaTransactionManager transactionManager = new JpaTransactionManager();
      transactionManager.setEntityManagerFactory(emf);

      return transactionManager;
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer() {
                   return new PropertySourcesPlaceholderConfigurer();
    }
    
   
    
    

}
