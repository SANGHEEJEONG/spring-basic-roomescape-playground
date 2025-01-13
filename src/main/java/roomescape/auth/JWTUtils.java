package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.exception.InvalidTokenException;
import roomescape.exception.MissingTokenException;
import roomescape.member.Member;

import java.util.Arrays;

@Component
public class JWTUtils {

    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    public AuthToken createToken(Member member) {
        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        return new AuthToken(accessToken);
    }

    public AuthToken extractToken(HttpServletRequest request){
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new IllegalArgumentException("토큰을 찾을 수 없습니다."));

        return new AuthToken(token);
    }

    public AuthClaims getClaimsFromToken(String token) {
        if (token == null || token.isBlank()) {
            throw new MissingTokenException("토큰이 비어있습니다.");
        }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String name = claims.get("name", String.class);
            String role = claims.get("role", String.class);

            if (name == null || role == null) {
                throw new InvalidTokenException("필수 클레임이 누락되었습니다.");
            }

            return new AuthClaims(name, role);

        } catch (JwtException e) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다.");
        }
    }
}
