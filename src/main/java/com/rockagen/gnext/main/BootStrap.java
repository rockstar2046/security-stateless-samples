/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rockagen.gnext.main;

import com.alibaba.druid.support.http.StatViewServlet;
import com.rockagen.gnext.Filter.AccessFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.GroovyWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.Log4jConfigListener;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.EnumSet;

/**
 * Main entry point
 *
 * @author ra
 * @since JDK1.8
 */
public class BootStrap implements WebApplicationInitializer {

    
    private GroovyWebApplicationContext rootContext;
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        rootContext=new GroovyWebApplicationContext();
        rootContext.setServletContext(servletContext);
        rootContext.setConfigLocation("classpath:applicationContext.groovy");
        rootContext.refresh();
        // Set init parameter
        setInitParameter(servletContext);
        // Add listener
        addListeners(servletContext);
        // Add Filter
        addFilter(servletContext);
        // Add Servlet
        addServlet(servletContext);
    }

    protected void addServlet(final ServletContext ctx) {
        DispatcherServlet ds=new DispatcherServlet(rootContext);
        // Spring mvc
        ctx.addServlet("spring-mvc",ds).addMapping("/");

        StatViewServlet druid=new StatViewServlet();

        // Druid view
        ctx.addServlet("druid",druid).addMapping("/druid/*");

    }

    protected void addFilter(final ServletContext ctx) {

        AccessFilter af= (AccessFilter) rootContext.getBean("accessFilter");
        CharacterEncodingFilter cef = (CharacterEncodingFilter) rootContext.getBean("characterEncodingFilter");

        DelegatingFilterProxy dfp = new DelegatingFilterProxy("springSecurityFilterChain");

        EnumSet<DispatcherType> dts=EnumSet.allOf(DispatcherType.class);

        // Access log filter
        ctx.addFilter("accessFilter",af).addMappingForUrlPatterns(dts,true,"/*");
        // Error wrapper filter
        // Character encoding filter
        ctx.addFilter("characterEncodingFilter", cef).addMappingForUrlPatterns(dts, true, "/*");
        // Spring security filter
        ctx.addFilter("spring-security", dfp).addMappingForUrlPatterns(dts, true, "/*");

    }

    protected void addListeners(final ServletContext ctx) {
        ctx.addListener(new ContextLoaderListener(rootContext));
        Log4jConfigListener lcl = new Log4jConfigListener();
        ctx.addListener(lcl);
    }

    public void setInitParameter(final ServletContext ctx) {
        // Spring log4j param
        ctx.setInitParameter("log4jConfigLocation", "classpath:log4j.properties");
        ctx.setInitParameter("log4jRefreshInterval", "60000");
    }
}
