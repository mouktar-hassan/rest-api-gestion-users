package com.example.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String traceId = MDC.get("traceId");  // ✅ Correct
        String userId = MDC.get("userId");

        logger.info("📥 [REQUEST] [{}] {} - traceId: {}, userId: {}",
                request.getMethod(), request.getRequestURI(), traceId, userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        String traceId = MDC.get("traceId");  // ✅ Correct
        String userId = MDC.get("userId");

        logger.info("📤 [RESPONSE] [{}] {} - status {} - traceId: {}, userId: {}",
                request.getMethod(), request.getRequestURI(), response.getStatus(), traceId, userId);
    }

}
