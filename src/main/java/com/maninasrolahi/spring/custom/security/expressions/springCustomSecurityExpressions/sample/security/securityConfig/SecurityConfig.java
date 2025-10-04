package com.maninasrolahi.spring.custom.security.expressions.springCustomSecurityExpressions.sample.security.securityConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final ApplicationContext applicationContext;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/testExpression/allow")
                        .access(getExpressionManager("permitAll() and @exp.alwaysAllow()"))
                        .requestMatchers("/testExpression/deny")
                        .access(getExpressionManager("permitAll() and @exp.alwaysDeny()"))
                        .requestMatchers("/testExpression/ip")
                        .access(getExpressionManager("permitAll() and @exp.isIpValid('127.0.0.1')"))
                        .anyRequest()
                        .permitAll()
                ).build();
    }

    private WebExpressionAuthorizationManager getExpressionManager(String exp) {
        WebExpressionAuthorizationManager manager = new WebExpressionAuthorizationManager(exp);
        manager.setExpressionHandler(expressionHandler());
        return manager;
    }

    @Bean
    public SecurityExpressionHandler<RequestAuthorizationContext> expressionHandler() {
        DefaultHttpSecurityExpressionHandler handler = new DefaultHttpSecurityExpressionHandler();
        handler.setApplicationContext(applicationContext);
        return handler;
    }
}
