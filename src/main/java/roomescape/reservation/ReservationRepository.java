package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    List<Reservation> findByName(String name);

    Optional<Reservation> findByDateAndThemeIdAndTimeId(String date, Long themeId, Long timeId);
}
