package com.example.config;
import org.slf4j.MDC;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class MDCFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);

        // Simule l’identification d’un utilisateur (si authentifié)
        String userId = ((HttpServletRequest) request).getHeader("X-User-Id");
        if (userId != null) {
            MDC.put("userId", userId);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear(); // Nettoyage du MDC après la requête
        }
    }
}
