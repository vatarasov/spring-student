package ru.vtarasov.spring.student;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author vtarasov
 * @since 22.09.2019
 */
@Configuration
@EnableWebMvc
@ComponentScan("ru.vtarasov.spring.student")
public class StudentApplicationContextConfig {}
