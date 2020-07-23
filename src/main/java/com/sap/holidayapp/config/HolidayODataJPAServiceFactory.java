package com.sap.holidayapp.config;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAServiceFactory;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;
import com.sap.holidayapp.service.HolidayProcessor;


public class HolidayODataJPAServiceFactory extends ODataJPAServiceFactory {
  @Override
  public ODataSingleProcessor createCustomODataProcessor(ODataJPAContext oDataJPAContext) {
    return new HolidayProcessor(oDataJPAContext);
  }

  @Override
  public ODataJPAContext initializeODataJPAContext() throws ODataJPARuntimeException {

    ODataJPAContext oDataJPAContext = getODataJPAContext();

    try {
      String persistenceUnitName =
          SpringApplicationContext.getEnvironment().getProperty("persistence.unit.name");
      EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
      oDataJPAContext.setEntityManagerFactory(emf);

      oDataJPAContext.setPersistenceUnitName(persistenceUnitName);
    } catch (Exception e) { // NOSONAR
      throw new RuntimeException(e);
    }
    return oDataJPAContext;
  }

//  @SuppressWarnings("unchecked")
//  @Override
//  public <T extends ODataCallback> T getCallback(Class<T> callbackInterface) {
//    if (callbackInterface.isAssignableFrom(ODataErrorCallback.class)) {
////      return (T) new ODataGlobalExceptionHandler();
//    } else {
//      return super.getCallback(callbackInterface);
//    }
//  }

}
