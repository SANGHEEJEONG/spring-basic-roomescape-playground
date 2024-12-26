package roomescape.auth;

public record AuthClaims(
        String name,
        String role
) {
}
