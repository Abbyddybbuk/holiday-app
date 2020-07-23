package com.sap.holidayapp.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SpringApplicationContext implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringApplicationContext.applicationContext = applicationContext;
  }

  public static Object getBean(String name) throws BeansException {
    return applicationContext.getBean(name);
  }

  public static <T> T getBean(Class<T> cls) throws BeansException {
    return applicationContext.getBean(cls);
  }

  public static Environment getEnvironment() {
    return applicationContext.getEnvironment();
  }
}
