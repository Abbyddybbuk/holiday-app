package com.sap.holidayapp.config;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import org.eclipse.persistence.config.PersistenceUnitProperties;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;


@Configuration
@Profile({"cloud"})
@EnableAutoConfiguration
public class CloudDbConfig extends AbstractCloudConfig {
  private static Logger logger = LoggerFactory.getLogger(CloudDbConfig.class);
  private static final String VCAP_SERVICES = "vcap.services.";
  // @Value("${COM_SAP_ICD_MT_HANA_SERVICE_CODE_LIST}")
  private String hanaInstanceName;
  
  @Primary
  @Bean
  public DataSource dataSource(Environment env) {
    logger.info("Building datasource");
    hanaInstanceName = "hanatrial";

    String driver = com.sap.db.jdbc.Driver.getDriverName();// env.getProperty(VCAP_SERVICES + hanaInstanceName + ".credentials.driver", "");
    if (driver==null || StringUtils.isEmpty(driver)) driver="Test123";
    logger.info(driver);
    String url = "jdbc:sap://zeus.hana.prod.us-east-1.whitney.dbaas.ondemand.com:21022?encrypt=true&validateCertificate=true&currentschema=USR_8Z0GFE1VA6ZOOQXP72DOACAIA";
    logger.info(url);
    String user = "USR_8Z0GFE1VA6ZOOQXP72DOACAIA";// env.getProperty(VCAP_SERVICES + hanaInstanceName + ".credentials.user", "");
    logger.info(user);
    String password = "Qt7P3LSZbnUVhQ4fiYSHvImnbX5BEljs-83qqiy7m0KJ2.Mj_JSynYbria8RJkzxJi4MqpvjRTcyYiW0EsfIHwftMITk8dfze-mBotdtL0PkZzfqJxMaroLmm3og4NnY";
        //env.getProperty(VCAP_SERVICES + hanaInstanceName + ".credentials.password", "");
    logger.info(password);
    DataSource dataSource = null;
    dataSource = DataSourceBuilder.create()
        .type(com.zaxxer.hikari.HikariDataSource.class).url(url).username(user).password(password)
        .build();
    return dataSource;
  }

//  @Autowired
//  DataSource ds;
//
//  @Lookup
//  public DataSource getDataSource() {
//    return ds;
//  }


  @Bean
  public JpaVendorAdapter eclipselink() {
    return new EclipseLinkJpaVendorAdapter();
  }

  @Bean(name = "entityManagerFactory")
  public LocalContainerEntityManagerFactoryBean emf(JpaVendorAdapter adapter, DataSource ds, Environment env) {
    String persistenceUnitName =
        env.getProperty("com.sap.icd.odatav2.spring.persistenceUnit", "default");
    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setJpaVendorAdapter(adapter);
    factory.setDataSource(ds);
    factory.setPersistenceUnitName(persistenceUnitName);
//    factory.setJpaPropertyMap(getJpaProperties());
    return factory;
  }

  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
    return new JpaTransactionManager(emf);
  }

  private Map<String, String> getJpaProperties() {
    Map<String, String> map = new HashMap<>();
    map.put(PersistenceUnitProperties.DDL_GENERATION, "none");
    map.put(PersistenceUnitProperties.TARGET_DATABASE, "HANA");
    return map;
  }

}
