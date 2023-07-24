package by.nortin.restjwt.config;

import by.nortin.restjwt.handler.CustomAccessDeniedHandler;
import by.nortin.restjwt.token.JwtTokenManager;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenManager jwtTokenManager;
    //    private final CustomExceptionHandler customExceptionHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
//    private final CustomAuthenticationEntryPointExceptionHandler customAuthenticationEntryPointExceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
//        String jwt = null;
//        String username = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String jwt = authorization.substring(7);
//            jwt = authorization.substring(7);
//            boolean incorrect = false;
//            String exceptionMessage = "";
            try {
                String username = jwtTokenManager.getUserName(jwt);
//                username = jwtTokenManager.getUserName(jwt);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            jwtTokenManager.getUserRoles(jwt).stream().map(SimpleGrantedAuthority::new).toList()
                    );
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
//                filterChain.doFilter(request, response);

            } catch (MalformedJwtException exception) {
                createExceptionResponse(request, response, "Incorrect token");
                return;
//                incorrect = true;
//                exceptionMessage = "Incorrect token";

//                +
//                String errorText = "ERROR!!!";
//                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//                response.getWriter().write(errorText);

            } catch (ExpiredJwtException exception) {
                createExceptionResponse(request, response, "The life cycle of the token is completed");
                return;
//                incorrect = true;
//                exceptionMessage = "The life cycle of the token is completed";
            } catch (SignatureException exception) {
                createExceptionResponse(request, response, "Incorrect signature");
                return;
//                incorrect = true;
//                exceptionMessage = "Incorrect signature";
            }
//            } finally {
//                if (incorrect) {
//                    createExceptionResponse(request, response, exceptionMessage);
////                    customAccessDeniedHandler.handle(request, response, new AccessDeniedException(exceptionMessage));
//                    //noinspection ReturnInsideFinallyBlock
//
//                    // rewrite (or if (flag) on chain)
////                    return;
////                    customAuthenticationEntryPointExceptionHandler.commence(request, response, new AuthenticationException(exceptionMessage) {
////                    });
//                }
//            }
//            filterChain.doFilter(request, response);
        }

//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                    username,
//                    null,
//                    jwtTokenManager.getUserRoles(jwt).stream().map(SimpleGrantedAuthority::new).toList()
//            );
//            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//        }
        filterChain.doFilter(request, response);
    }

    private void createExceptionResponse(HttpServletRequest request, HttpServletResponse response, String exceptionMessage) throws IOException, ServletException {
        log.debug(exceptionMessage);
        customAccessDeniedHandler.handle(request, response, new AccessDeniedException(exceptionMessage));
    }
}
