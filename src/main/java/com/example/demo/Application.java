package com.example.demo;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
//@SpringBootApplication=@Configuration+@EnableAutoConfiguration+@ComponentScan
//@Configuration
//@EnableAutoConfiguration
//@ComponentScan
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);


//    public static void main(String[] args) throws UnknownHostException {
//        SpringApplication app = new SpringApplication(Application.class);
//        Environment env = app.run(args).getEnvironment();
//        String contextPath = env.getProperty("server.contextPath");
//        if (StringUtils.isEmpty(contextPath)) {
//            contextPath = "";
//        } else {
//            if (contextPath.startsWith("/")) {
//                contextPath = contextPath.substring(1);
//            }
//        }
//
//        log.info("Access URLs:\n----------------------------------------------------------\n\t" +
//                        "Local: \t\thttp://127.0.0.1:{}/{}\n\t" +
//                        "External: \thttp://{}:{}/{}\n----------------------------------------------------------",
//                env.getProperty("server.port"),
//                contextPath,
//                InetAddress.getLocalHost().getHostAddress(),
//                env.getProperty("server.port"),
//                contextPath);
//}

    //    @Bean
//    public EmbeddedServletContainerFactory servletContainer() {
//        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint securityConstraint = new SecurityConstraint();
//                securityConstraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                securityConstraint.addCollection(collection);
//                context.addConstraint(securityConstraint);
//            }
//        };
//
//        tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
//        return tomcat;
//    }
//
//    private Connector initiateHttpConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        connector.setPort(8080);
//        connector.setSecure(false);
//        connector.setRedirectPort(8443);
//
//        return connector;
//    }
    public static void main(String[] args){

        // 程序启动入口
        // 启动嵌入式的 Tomcat 并初始化 Spring 环境及其各 Spring 组件
        SpringApplication.run(Application.class, args);
    }

}
