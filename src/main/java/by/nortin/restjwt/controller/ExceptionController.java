//package by.nortin.restjwt.controller;
//
//import by.nortin.restjwt.exception.CustomExceptionHandler;
//import by.nortin.restjwt.model.BaseResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/error")
//public class ExceptionController {
//
//    private final CustomExceptionHandler customExceptionHandler;
//
//    @GetMapping("/access-denied")
//    public ResponseEntity<BaseResponse> getAccessDeniedResponse() {
//        return customExceptionHandler.handleException(new AccessDeniedException(""));
//    }
//}
