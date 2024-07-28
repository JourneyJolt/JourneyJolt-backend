package dev.cyan.travel.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cyan.travel.response.MessageResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AccessDeniedHandlerJwt implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(AccessDeniedHandlerJwt.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        logger.error(accessDeniedException.getMessage());

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        objectMapper.writeValue(response.getOutputStream(), new MessageResponse(accessDeniedException.getMessage()));
    }
}
