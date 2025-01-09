package roomescape.waiting;

public record WaitingResponse(
        Long id,
        String name,
        String date,
        String time,
        String theme
) {
}
