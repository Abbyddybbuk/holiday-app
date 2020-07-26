package com.sap.holidayapp.config;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Profile({ "default" })
public class LocalDbConfig {

    @Bean
    public JpaVendorAdapter eclipseLink() {
        return new EclipseLinkJpaVendorAdapter();
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean emf(JpaVendorAdapter adapter, DataSource ds,
            @Value("${com.sap.icd.odatav2.spring.persistenceUnit}") String persistenceUnitName) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(adapter);
        factory.setDataSource(ds);
        factory.setPersistenceUnitName(persistenceUnitName);
        factory.setJpaPropertyMap(getJpaProperties());
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    private Map<String, String> getJpaProperties() {
        Map<String, String> map = new HashMap<>();
        map.put(PersistenceUnitProperties.DDL_GENERATION, PersistenceUnitProperties.CREATE_OR_EXTEND);
        map.put("eclipselink.ddl-generation.output-mode", "both");
        return map;
    }
  

}
