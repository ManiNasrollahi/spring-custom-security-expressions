package com.maninasrolahi.spring.custom.security.expressions.springCustomSecurityExpressions.testController;

import com.maninasrolahi.spring.custom.security.expressions.springCustomSecurityExpressions.sample.controller.TestExpressionController;
import com.maninasrolahi.spring.custom.security.expressions.springCustomSecurityExpressions.sample.security.Expression.CustomMethodSecurityExpression;
import com.maninasrolahi.spring.custom.security.expressions.springCustomSecurityExpressions.sample.security.securityConfig.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WebMvcTest(controllers = TestExpressionController.class)
@Import(
        {
                CustomMethodSecurityExpression.class,
                SecurityConfig.class,
                TestExpressionController.class,
        }
)
public class TestController {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenAlwaysAllow_httpAccess_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/testExpression/allow"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Allowed!"));
    }

    @Test
    void givenAlwaysDeny_httpAccess_thenForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/testExpression/deny"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void givenAllowedIp_httpAccess_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/testExpression/ip")
                        .header("CUSTOM_IP_HEADER", "127.0.0.1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("IP Check Passed!"));
    }

    @Test
    void givenNotAllowedIp_httpAccess_thenForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/testExpression/ip")
                        .header("CUSTOM_IP_HEADER", "192.168.1.10"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void givenAlwaysAllow_method_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/testExpression/allowMethod"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Method Allowed!"));
    }

    @Test
    void givenAlwaysDeny_method_thenForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/testExpression/denyMethod"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void givenAllowedIp_method_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/testExpression/ipMethod")
                        .header("CUSTOM_IP_HEADER", "127.0.0.1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("IP Check Method Passed!"));
    }

    @Test
    void givenNotAllowedIp_method_thenForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/testExpression/ipMethod")
                        .header("CUSTOM_IP_HEADER", "192.168.1.10"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
