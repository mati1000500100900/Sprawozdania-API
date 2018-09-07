package com.tokar;

import com.tokar.Config.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

    @Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/courses/*");
        registrationBean.addUrlPatterns("/teacher/*");
        registrationBean.addUrlPatterns("/user/whoami");
        return registrationBean;
    }
    public static void main(String args[]){
        SpringApplication.run(Main.class, args);
    }
}
