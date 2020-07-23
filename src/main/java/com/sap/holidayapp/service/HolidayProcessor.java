package com.sap.holidayapp.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.io.IOUtils;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.api.uri.info.PostUriInfo;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPADefaultProcessor;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAProcessor;
import org.apache.olingo.odata2.jpa.processor.api.access.JPAProcessor;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;
import org.apache.olingo.odata2.jpa.processor.api.factory.ODataJPAFactory;
import org.apache.olingo.odata2.jpa.processor.core.ODataJPAResponseBuilderDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.holidayapp.config.SpringApplicationContext;
import com.sap.holidayapp.model.HolidayDetail;
import com.sap.holidayapp.model.HolidayHeader;

@Primary
@Component
@RequestScope
public class HolidayProcessor extends ODataJPADefaultProcessor {

  private ODataJPAContext oDataJpaContext;

  @Autowired
  public HolidayProcessor(ODataJPAContext oDataJPAContext) {
    super(oDataJPAContext);
    oDataJpaContext = oDataJPAContext;
  }

  @Override
  public ODataResponse readEntitySet(GetEntitySetUriInfo uriInfo, String contentType)
      throws ODataException {
    oDataJpaContext.setODataContext(getContext());
    ODataJPAProcessor oDataJPAProcessor = (ODataJPAProcessor) this;

    JPAProcessor jpaProcessor = ODataJPAFactory.createFactory().getJPAAccessFactory()
        .getJPAProcessor(oDataJPAProcessor.getOdataJPAContext());

    EntityManager entityManager = oDataJPAContext.getEntityManager();
    if (!entityManager.getTransaction().isActive())
      entityManager.getTransaction().begin();
    oDataJPAContext.setEntityManager(entityManager);
    List<Object> result = new ArrayList<>();

    String entityName = uriInfo.getTargetType().getName();
    if (entityName.equals("HolidayHeader")) {
      List<HolidayHeader> holidayHeaderList = jpaProcessor.process(uriInfo);
      result.addAll(holidayHeaderList);
    } else if (entityName.equals("HolidayDetail")) {
      List<HolidayDetail> holidayDetailList = jpaProcessor.process(uriInfo);
      // List<HolidayDetail> holidayDetailList = this.detailRepository.findAll();
      result.addAll(holidayDetailList);
    }

    ODataJPAResponseBuilderDefault responseBuilder =
        new ODataJPAResponseBuilderDefault(oDataJPAContext);

    try {
      return responseBuilder.build(uriInfo, result, contentType);
    } catch (ODataJPARuntimeException e) {
      throw new ODataException(e);
    }
  }

  @Override
  public ODataResponse createEntity(PostUriInfo uriInfo, InputStream content,
      String requestContentType, String contentType) throws ODataException {
    oDataJpaContext.setODataContext(getContext());
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

        returnObject = SpringApplicationContext.getBean(HolidayService.class)
            .createHolidayData(holidayHeader, oDataJpaContext);
        break;
      default:
        break;
    }

    return responseBuilder.build((PostUriInfo) uriInfo, returnObject, contentType);
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


