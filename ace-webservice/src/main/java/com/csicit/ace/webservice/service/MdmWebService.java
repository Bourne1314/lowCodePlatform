package com.csicit.ace.webservice.service;

import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/6 11:24
 */
@Transactional

public interface MdmWebService {

    @WebMethod
    String opearteOrganization(@WebParam(name = "xmlStr") String xmlStr);

    @WebMethod
    String operateEmployee(@WebParam(name = "xmlStr") String xmlStr);
}
