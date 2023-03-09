package org.prgrms.kdt.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/*
  - web container에 TestServlet이 있다는 것을 알려야 함. >> web.xml에 설정
  >>> @WebServlet 으로 간편화.
  - value = "/*" : 모든 url 요청과 연결
  - loadOnStartup = 1 : 요청 전에 미리 servlet을 로드하겠다.
  - loadOnStartup = -1 : 요청을 받으면 servlet을 로드. (default)
*/

//@WebServlet(value = "/*", loadOnStartup = 1) //->> KdtWebApplicationInitializer 에 정의.
public class TestServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(TestServlet.class);

    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("Init Servlet");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var requestURI = req.getRequestURI();
        logger.info("Got Request from {}", requestURI);

        var writer = resp.getWriter();
        writer.println("Hello Servlet!");
    }
}
