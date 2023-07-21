package by.nortin.restjwt.token;

import static by.nortin.restjwt.utils.Constants.ROLES;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenManager {

//    @Value("${jwt.secret}")
//    private String secret;

    private final Key key;

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    public JwtTokenManager(@Value("${jwt.secret}") String secret) {
        this.key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateJwtToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put(ROLES, roles);
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());
//        Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
//                .signWith(HS256, secret)
                .signWith(key, HS256)
                .compact();
    }

    public String getUserName(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getUserRoles(String token) {
        return getAllClaimsFromToken(token).get(ROLES, List.class);
    }

    private Claims getAllClaimsFromToken(String token) {
//        Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
