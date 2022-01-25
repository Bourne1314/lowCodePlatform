package com.csicit.ace.demo.odata;

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shanwj
 * @date 2019/10/11 16:16
 */
@Configuration
public class ServletConfig {

    @Bean
    public ServletRegistrationBean cXFNonSpringJaxrsServletBean() throws ServletException {
        ServletRegistrationBean bean = new ServletRegistrationBean(new CXFNonSpringJaxrsServlet(), "/Bor.svc/*");
        bean.setLoadOnStartup(2);
        Map<String, String> map = new HashMap<>(16);
        map.put("javax.ws.rs.Application", "org.apache.olingo.odata2.core.rest.app.ODataApplication");
        map.put("org.apache.olingo.odata2.service.factory", "com.csicit.ace.demo.odata.BorServiceFactory");
        map.put("jaxrs.providers", "org.apache.cxf.rs.security.cors.CrossOriginResourceSharingFilter");
        bean.setInitParameters(map);
        return bean;
    }

    @Bean
    public ServletRegistrationBean carsServletBean() throws ServletException {
        ServletRegistrationBean bean = new ServletRegistrationBean(new CarsServlet(), "/cars.svc/*");
        return bean;
    }
}
