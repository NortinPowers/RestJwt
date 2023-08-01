package by.nortin.restjwt.handler;

import static by.nortin.restjwt.utils.ResponseUtils.getObjectMapperWithTimeModule;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import by.nortin.restjwt.exception.CustomExceptionHandler;
import by.nortin.restjwt.model.BaseResponse;
import by.nortin.restjwt.model.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final CustomExceptionHandler customExceptionHandler;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException, ServletException {
        ResponseEntity<BaseResponse> responseEntity = customExceptionHandler.handleException(ex);
        ExceptionResponse responseEntityBody = (ExceptionResponse) responseEntity.getBody();
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(responseEntity.getStatusCode().value());
        ObjectMapper mapper = getObjectMapperWithTimeModule();
        response.getWriter().write(mapper.writeValueAsString(responseEntityBody));
    }
}
