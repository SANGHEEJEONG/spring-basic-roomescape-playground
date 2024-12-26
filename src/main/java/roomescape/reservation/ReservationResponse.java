package roomescape.reservation;

public record ReservationResponse(
        Long id,
        String name,
        String date,
        String theme,
        String time
){
}
