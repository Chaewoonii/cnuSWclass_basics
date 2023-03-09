package org.prgrms.kdt.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;

public class KdtWebApplicationInitializer implements WebApplicationInitializer {
    private static final Logger logger = LoggerFactory.getLogger(KdtWebApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        logger.info("Starting Server...");
        var servletRegistration = servletContext.addServlet("test", new TestServlet()); // Servlet 추가
        servletRegistration.addMapping("/*");
        servletRegistration.setLoadOnStartup(1);
    }
}
