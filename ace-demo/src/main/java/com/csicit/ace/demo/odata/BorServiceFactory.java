package com.csicit.ace.demo.odata;

import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAServiceFactory;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author shanwj
 * @date 2019/10/11 16:20
 * odata2 数据对象转换
 *
 */
public class BorServiceFactory extends ODataJPAServiceFactory {
    private static final String PERSISTENCE_UNIT_NAME = "jpa";
    @Override
    public ODataJPAContext initializeODataJPAContext() throws ODataJPARuntimeException {
        ODataJPAContext oDataJPAContext = this.getODataJPAContext();
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            oDataJPAContext.setEntityManagerFactory(emf);
            oDataJPAContext.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
            return oDataJPAContext;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
