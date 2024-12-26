package roomescape.member;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.auth.AuthClaims;
import roomescape.auth.AuthToken;
import roomescape.auth.JWTUtils;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberDao memberDao;
    private final JWTUtils jwtUtils;

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public AuthToken getToken(LoginRequest loginRequest) {
        try {
            Member member = memberDao.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
            return jwtUtils.createToken(member);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("로그인 정보가 잘못되었습니다.");
        }
    }

    public AuthClaims checkLogin(AuthToken userToken) {
        try {
            AuthClaims userClaims = jwtUtils.getClaimsFromToken(userToken.token());
            memberDao.findByName(userClaims.name());
            return userClaims;
        } catch (EmptyResultDataAccessException exception) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
    }
}
