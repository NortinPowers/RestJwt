//package by.nortin.restjwt.config;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.nio.file.AccessDeniedException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//@Component
//@Slf4j
//public class AccessDeniedFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try {
//            filterChain.doFilter(request, response);
//        } catch (AccessDeniedException accessDeniedException) {
//            log.debug("Incorrect role");
//            throw new AccessDeniedException("");
//        }
////        if (request.getAttribute("accessDeniedError") != null) {
////            log.debug("Incorrect role");
////            throw new AccessDeniedException("");
////        }
//    }
//}
