package roomescape.member;


public record MemberRequest(
        String name,
        String email,
        String password
) {
}

