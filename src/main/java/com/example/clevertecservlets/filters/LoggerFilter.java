package com.example.clevertecservlets.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Collectors;

@WebFilter(value = "/*", filterName = "1")
public class LoggerFilter implements Filter {

    private Logger logger;

    @Override
    public void init(FilterConfig filterConfig) {
        this.logger = Logger.getLogger(LoggerFilter.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        Iterator<String> iterator = req.getHeaderNames().asIterator();
        while (iterator.hasNext()) {
            String str = iterator.next();
            logger.info(str + " : " + req.getHeader(str));
        }

        String body = req.getReader().lines().collect(Collectors.joining());
        logger.info(body);
        request.setAttribute("body", body);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
