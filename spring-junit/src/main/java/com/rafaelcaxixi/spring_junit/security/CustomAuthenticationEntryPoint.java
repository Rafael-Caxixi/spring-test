    package com.rafaelcaxixi.spring_junit.security;

    import com.fasterxml.jackson.databind.ObjectMapper;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.web.AuthenticationEntryPoint;
    import org.springframework.stereotype.Component;

    import java.io.IOException;
    import java.time.LocalDateTime;
    import java.util.Map;

    @Component
    public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

        private final ObjectMapper objectMapper;

        public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public void commence(HttpServletRequest request,
                             HttpServletResponse response,
                             AuthenticationException authException) throws IOException {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            var errorResponse = Map.of(
                    "timestamp", LocalDateTime.now(),
                    "status", HttpServletResponse.SC_UNAUTHORIZED,
                    "error", "Unauthorized",
                    "message", "Token invalido ou ausente.",
                    "path", request.getRequestURI(),
                    "method", request.getMethod()
            );

            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }