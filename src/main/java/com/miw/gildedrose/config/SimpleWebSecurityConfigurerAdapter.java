package com.miw.gildedrose.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SimpleWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    // TODO this is for demonstrating securing endpoint using basic authentication and not suggested used in production code
    @Value("${ordering.user:john}")
    private String user;

    // TODO this is for demonstrating securing endpoint using basic authentication and not suggested used in production code
    @Value("${ordering.password:doe}")
    private String password;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inMemoryUserDetailsManager());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/ordering/request").hasRole("USER")
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username(this.user)
                .password(this.password)
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
