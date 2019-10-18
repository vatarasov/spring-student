package ru.vtarasov.spring.student;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * @author vtarasov
 * @since 22.09.2019
 */
public class StudentApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { StudentApplicationContextConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[0];
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}