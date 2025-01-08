package roomescape.reservation;

public record ReservationRequest (
        String name,
        String date,
        String theme,
        String time
){
}
