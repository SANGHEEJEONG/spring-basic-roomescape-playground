package roomescape.member;

public record LoginRequest(
        String email,
        String password
) {
}
