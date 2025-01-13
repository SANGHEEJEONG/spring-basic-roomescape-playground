package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthRoleInterceptor implements HandlerInterceptor {

    private final JWTUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = jwtUtils.extractToken(request).token();

        if (!jwtUtils.getClaimsFromToken(token).role().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
