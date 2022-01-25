package com.csicit.ace.report.core.config;

import com.csicit.ace.report.core.flash.AceStiDesignerFxServlet;
import com.csicit.ace.report.core.flash.AceStiViewerFxServlet;
import com.csicit.ace.report.core.flash.ApplicationInitializer;
import com.stimulsoft.web.servlet.StiWebResourceServlet;
import com.stimulsoft.webdesigner.servlet.StiWebDesignerActionServlet;
import com.stimulsoft.webviewer.servlet.StiWebViewerActionServlet;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shanwj
 * @date 2019/8/6 10:20
 */
@Configuration
public class ServletConfig {

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        return new ServletRegistrationBean(new StiWebResourceServlet(),"/stimulsoft_web_resource/*");
    }

    @Bean
    public ServletRegistrationBean stiWebDesignerActionServletBean(){
        return new ServletRegistrationBean(new StiWebDesignerActionServlet(),"/stimulsoft_webdesigner_action");
    }

    @Bean
    public ServletRegistrationBean stiWebViewerActionServletBean(){
        return new ServletRegistrationBean(new StiWebViewerActionServlet(),"/stimulsoft_webviewer_action");
    }

    @Bean
    public ServletRegistrationBean stiDesignerFxServletBean(){
        return new ServletRegistrationBean(new AceStiDesignerFxServlet(),"/stimulsoft_designerfx");
    }

    @Bean
    public ServletRegistrationBean stiViewerFxServletBean(){
        return new ServletRegistrationBean(new AceStiViewerFxServlet(),"/stimulsoft_viewerfx");
    }

    @Bean
    public ServletListenerRegistrationBean applicationInitializerBean(){
        return new ServletListenerRegistrationBean(new ApplicationInitializer());
    }

    /**
     * tomcat 7以后会对请求路径的中有要求，加入特殊字符开放
     * @return
     */
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
                //允许的特殊字符
                connector.setProperty("relaxedQueryChars", "|{}[]#+\\");
            }
        });
        return factory;
    }

}
