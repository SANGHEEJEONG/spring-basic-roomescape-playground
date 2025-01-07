package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.MemberService;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class AuthRoleInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new IllegalArgumentException("토큰을 찾을 수 없습니다."));

        AuthClaims userClaims = memberService.checkLogin(new AuthToken(token));
        if (!"ADMIN".equals(userClaims.role())) { // 상수 기준으로 비교하기
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
