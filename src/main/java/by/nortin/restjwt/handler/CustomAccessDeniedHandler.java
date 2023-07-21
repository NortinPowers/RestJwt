package by.nortin.restjwt.handler;

import by.nortin.restjwt.exception.CustomExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final CustomExceptionHandler customExceptionHandler;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException, ServletException {
//        +
//        ResponseEntity<BaseResponse> responseEntity = customExceptionHandler.handleException(ex);
//        response.setContentType(APPLICATION_JSON_VALUE);
//        ObjectMapper mapper = JsonMapper.builder()
//                                        .addModule(new JavaTimeModule())
//                                        .build();
//        response.getWriter().write(mapper.writeValueAsString(responseEntity));

//        +
        response.sendRedirect("/error/access-denied");
    }
}
