package roomescape.auth;

public record UserClaims(
        String name,
        String role
) {
}
