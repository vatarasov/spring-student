package ru.vtarasov.spring.student;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author vtarasov
 * @since 18.10.2019
 */
@Configuration
@EnableWebSecurity
public class StudentSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.user.name:user}")
    private String name;

    @Value("${spring.security.user.password:user}")
    private String password;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
            .withUser(name)
            .password("{noop}" + password)
            .roles();
    }
}
