//package by.nortin.restjwt.utils;
//
//import static by.nortin.restjwt.utils.ResponseUtils.getObjectMapperWithTimeModule;
//import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//
//import by.nortin.restjwt.exception.CustomExceptionHandler;
//import by.nortin.restjwt.model.BaseResponse;
//import by.nortin.restjwt.model.ExceptionResponse;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import lombok.experimental.UtilityClass;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
//
//@UtilityClass
//public class ExceptionHandlerUtils {
//
//    public void setResponse(HttpServletResponse response, CustomExceptionHandler exceptionHandler, AccessDeniedException ex) throws IOException {
//        ResponseEntity<BaseResponse> responseEntity = exceptionHandler.handleException(ex);
//        ExceptionResponse responseEntityBody = (ExceptionResponse) responseEntity.getBody();
//        response.setContentType(APPLICATION_JSON_VALUE);
//        response.setStatus(responseEntity.getStatusCode().value());
//        ObjectMapper mapper = getObjectMapperWithTimeModule();
//        response.getWriter().write(mapper.writeValueAsString(responseEntityBody));
//    }
//}
