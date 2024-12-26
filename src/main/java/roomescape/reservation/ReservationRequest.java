package roomescape.reservation;

public record ReservationRequest (
        String name,
        String date,
        Long theme,
        Long time
){
}
