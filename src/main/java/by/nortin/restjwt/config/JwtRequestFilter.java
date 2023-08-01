package by.nortin.restjwt.config;

import static by.nortin.restjwt.test.utils.ResponseUtils.EXPIRED_JWT_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.test.utils.ResponseUtils.MALFORMED_JWT_EXCEPTION_MESSAGE;
import static by.nortin.restjwt.test.utils.ResponseUtils.SIGNATURE_EXCEPTION_MESSAGE;

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
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @SuppressWarnings("checkstyle:ReturnCount")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String jwt = authorization.substring(7);
            try {
                String username = jwtTokenManager.getUserName(jwt);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            jwtTokenManager.getUserRoles(jwt).stream().map(SimpleGrantedAuthority::new).toList()
                    );
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            } catch (MalformedJwtException exception) {
                createExceptionResponse(request, response, MALFORMED_JWT_EXCEPTION_MESSAGE);
                return;
            } catch (ExpiredJwtException exception) {
                createExceptionResponse(request, response, EXPIRED_JWT_EXCEPTION_MESSAGE);
                return;
            } catch (SignatureException exception) {
                createExceptionResponse(request, response, SIGNATURE_EXCEPTION_MESSAGE);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void createExceptionResponse(HttpServletRequest request, HttpServletResponse response, String exceptionMessage) throws IOException, ServletException {
        log.debug(exceptionMessage);
        customAccessDeniedHandler.handle(request, response, new AccessDeniedException(exceptionMessage));
    }
}
