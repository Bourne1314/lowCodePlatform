package com.csicit.ace.webservice.config;

import com.csicit.ace.webservice.service.MdmWebService;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/5 14:15
 */
@Configuration
public class CxfConfig {
    //默认servlet路径/*,如果覆写则按照自己定义的来

    @Autowired
    private MdmWebService mdmWebService;
    @Bean
    public ServletRegistrationBean dispatcherServlet() {
        return new ServletRegistrationBean(new CXFServlet(), "/*");
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    //把实现类交给spring管理
//    @Bean
//    public MdmWebService mdmWebService() {
//        return new MdmWebServiceImpl();
//    }

    //终端路径
    @Bean(name = "endpointAMdm")
    public Endpoint endpointAMdm() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), mdmWebService);
        //endpoint.getInInterceptors().add(new AuthInterceptor());//添加校验拦截器
        endpoint.publish("/mdm");
        return (Endpoint)endpoint;
    }
}
