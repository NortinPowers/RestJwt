//package by.nortin.restjwt.exception;
//
//import static by.nortin.restjwt.utils.ResponseUtils.getObjectMapperWithTimeModule;
//import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//
//import by.nortin.restjwt.model.BaseResponse;
//import by.nortin.restjwt.model.ExceptionResponse;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//@RequiredArgsConstructor
//public class CustomAuthenticationEntryPointExceptionHandler implements AuthenticationEntryPoint {
//
//    private final CustomExceptionHandler customExceptionHandler;
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        ResponseEntity<BaseResponse> responseEntity = customExceptionHandler.handleException(authException);
//        ExceptionResponse responseEntityBody = (ExceptionResponse) responseEntity.getBody();
//        response.setContentType(APPLICATION_JSON_VALUE);
//        response.setStatus(responseEntity.getStatusCode().value());
//        ObjectMapper mapper = getObjectMapperWithTimeModule();
//        response.getWriter().write(mapper.writeValueAsString(responseEntityBody));
//
//    }
//}
