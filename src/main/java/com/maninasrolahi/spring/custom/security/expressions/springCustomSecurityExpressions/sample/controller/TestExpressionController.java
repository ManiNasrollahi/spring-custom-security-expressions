package com.maninasrolahi.spring.custom.security.expressions.springCustomSecurityExpressions.sample.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testExpression")
public class TestExpressionController {

    @GetMapping("/allow")
    public String allow() {
        return "Allowed!";
    }

    @GetMapping("/deny")
    public String deny() {
        return "request will never reach here!!!";
    }

    @GetMapping("/ip")
    public String ip() {
        return "IP Check Passed!";
    }

    @GetMapping("/allowMethod")
    @PreAuthorize("@exp.alwaysAllow()")
    public String allowMethod() {
        return "Method Allowed!";
    }

    @GetMapping("/denyMethod")
    @PreAuthorize("@exp.alwaysDeny()")
    public String denyMethod() {
        return "request will never reach here!!!";
    }

    @GetMapping("/ipMethod")
    @PreAuthorize("@exp.isIpValid('127.0.0.1')")
    public String ipMethod() {
        return "IP Check Method Passed!";
    }
}
