package com.sap.holidayapp.service;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.api.uri.info.PostUriInfo;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.core.ODataJPAResponseBuilderDefault;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.holidayapp.model.HolidayHeader;
import com.sap.icd.odatav2.spring.config.AbstractODataProcessor;
import com.sap.icd.odatav2.spring.config.StandardODataJPAProcessor;

public class HolidayProcessor extends AbstractODataProcessor {
  @Autowired
  private ODataJPAContext oDataJpaContext;

  private HolidayService holidayService;

  public HolidayProcessor(StandardODataJPAProcessor standardODataJPAProcessor,
      HolidayService holidayService) {
    super(standardODataJPAProcessor);
    this.oDataJpaContext = oDataJpaContext;
    this.holidayService = holidayService;
  }

  @Override
  public ODataResponse readEntitySet(GetEntitySetUriInfo uriInfo, String contentType)
      throws ODataException {
    return null;
  }

  @Override
  public ODataResponse createEntity(PostUriInfo uriInfo, InputStream content,
      String requestContentType, String contentType) throws ODataException {
    @SuppressWarnings("unused")
    EdmEntitySet edmEntitySet = uriInfo.getTargetEntitySet();
    Object returnObject = null;
    ODataJPAResponseBuilderDefault responseBuilder =
        new ODataJPAResponseBuilderDefault(this.oDataJpaContext);


    String entityName = uriInfo.getTargetType().getName();

    switch (entityName) {
      case "HolidayHeader":
        HolidayHeader holidayHeader =
            (HolidayHeader) this.getPojo(content, new TypeReference<HolidayHeader>() {});
        returnObject = this.holidayService.createHolidayData(holidayHeader, oDataJpaContext);
        break;
      default:
        break;
    }

    return responseBuilder.build(uriInfo, returnObject, contentType);
  }

  public <T> Object getPojo(InputStream content, TypeReference<T> typeReference)
      throws ODataException {
    String request = "";
    try {
      request = IOUtils.toString(content, "UTF-8");
    } catch (IOException e) {
      throw new ODataException(e);
    }
    ObjectMapper objectMapper = new ObjectMapper();

    try {
      return objectMapper.readValue(request, typeReference);
    } catch (IOException e) {
      throw new ODataException(e);
    }

  }

}


