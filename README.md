# 🔐 Spring Security Custom Expression Demo

This project demonstrates how to integrate **custom Spring Security expressions** into both  
✨ **HTTP endpoint authorization** and  
✨ **method-level authorization (`@PreAuthorize`)**  

using **Spring Boot / Spring Security**.

---

## 🚀 Features

-  Use your own methods inside Spring Security expressions  
-  Reuse logic for both `@PreAuthorize` and `.access()` rules   
-  Extendable, lightweight, and framework-native  

---

## ⚙️ Overview

Spring Security supports expression-based access control for flexible authorization rules.  
This demo extends that by defining custom logic in a bean (`@exp`) that you can invoke inside expressions like:

```java
@PreAuthorize("@exp.isIpValid('127.0.0.1')")
```
**or**
```java
.access(getExpressionManager("@exp.alwaysAllow()"))
```


---

## 🧩 Configuration Example

**SecurityConfig:**
```java
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
```
**CustomMethodSecurityExpression:**
```java
@Component("exp")
/*
This bean (exp) is available in all security expressions thanks to
@EnableMethodSecurity and WebExpressionAuthorizationManager bean!!!.
*/
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
```
---

## 💡 Extending

You can add new reusable methods easily ! just create your own method in **CustomMethodSecurityExpression**

```java
public boolean hasCustomHeader(String name, String value) {
    return value.equals(request.getHeader(name));
}
```
Usage: 
```java
@PreAuthorize("@exp.hasCustomHeader('X-Token', 'abc123')")
```
**or**
```java
.access(getExpressionManager("@exp.hasCustomHeader('X-Token', 'abc123')"))
```
---

Contributing
------------

Contributions are welcome! Feel free to open issues and pull requests.

✨ Author: Mani Nasollahi
