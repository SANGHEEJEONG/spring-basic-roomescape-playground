package roomescape.member;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import roomescape.auth.JWTUtils;
import roomescape.auth.UserClaims;
import roomescape.auth.UserToken;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberDao memberDao;
    private final JWTUtils jwtUtils;

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public UserToken getToken(LoginRequest loginRequest) {
        try {
            Member member = memberDao.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
            return jwtUtils.createToken(member);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("로그인 정보가 잘못되었습니다.");
        }
    }

    public UserClaims checkLogin(UserToken userToken) {
        try {
            UserClaims userClaims = jwtUtils.getClaimsFromToken(userToken.token());
            Member member = memberDao.findByName(userClaims.name());
            return new UserClaims(member.getName());
        } catch (EmptyResultDataAccessException exception) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
    }
}
