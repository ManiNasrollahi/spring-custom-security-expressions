package com.maninasrolahi.spring.custom.security.expressions.springCustomSecurityExpressions.sample.security.Expression;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component("exp")
public class CustomMethodSecurityExpression {

    private HttpServletRequest request;

    public boolean alwaysAllow() {
        return true;
    }

    public boolean alwaysDeny() {
        return false;
    }

    public boolean isIpValid(String... ips) {
        String clientIp = request.getHeader("CUSTOM_IP_HEADER");
        if (clientIp == null || clientIp.isBlank()) {
            return false;
        } else {
            clientIp = clientIp.split(",")[0].trim();
        }

        return Arrays.asList(ips).contains(clientIp);
    }

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
