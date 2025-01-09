package roomescape.waiting;

public record WaitingRequest(
        String name,
        String date,
        Long time,
        Long theme
) {
}
