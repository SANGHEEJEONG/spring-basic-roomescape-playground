package roomescape.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.auth.AuthClaims;
import roomescape.auth.AuthToken;
import roomescape.auth.JWTUtils;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JWTUtils jwtUtils;

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public AuthToken getToken(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new IllegalArgumentException("로그인 실패"));
        return jwtUtils.createToken(member);

    }

    public AuthClaims checkLogin(AuthToken userToken) {
        AuthClaims userClaims = jwtUtils.getClaimsFromToken(userToken.token());
        Member member = memberRepository.findByName(userClaims.name())
                .orElseThrow(() -> new IllegalArgumentException("헤당 멤버가 없습니다."));
        return userClaims;
    }
}
