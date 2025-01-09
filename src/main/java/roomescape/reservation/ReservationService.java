package roomescape.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Time time = timeRepository.findById(reservationRequest.time())
                .orElseThrow(() -> new IllegalArgumentException("해당 시간이 존재하지 않습니다."));
        Theme theme = themeRepository.findById(reservationRequest.theme())
                .orElseThrow(() -> new IllegalArgumentException("해당 테마가 존재하지 않습니다."));
        Member member = memberRepository.findByName(reservationRequest.name())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        reservationRepository.findByDateAndThemeIdAndTimeId(reservationRequest.date(), theme.getId(), time.getId())
                .ifPresent(it -> {
                    throw new IllegalArgumentException("이미 예약된 시간입니다.");
                });

        Reservation reservation = new Reservation(reservationRequest.name(), reservationRequest.date(), time, theme, member);
        reservationRepository.save(reservation);

        return new ReservationResponse(reservation.getId(), reservationRequest.name(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getTime());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getTime()))
                .toList();
    }

    public List<MyReservationResponse> findByMember(String name) {
        Member member = memberRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        List<MyReservationResponse> reservations = reservationRepository.findByName(name).stream()
                .map(it -> new MyReservationResponse(it.getId(), it.getTheme().getName(), it.getDate(), it.getTime().getTime(), "예약"))
                .toList();

        List<MyReservationResponse> waitings = waitingRepository.findWaitingsWithRankByMemberId(member.getId()).stream()
                .map(w -> new MyReservationResponse(w.getWaiting().getId(), w.getWaiting().getTheme().getName(), w.getWaiting().getDate(), w.getWaiting().getTime().getTime(), w.getRank() + 1 + "번째 예약대기"))
                .toList();

        return Stream.concat(reservations.stream(), waitings.stream())
                .collect(Collectors.toList());
    }
}
