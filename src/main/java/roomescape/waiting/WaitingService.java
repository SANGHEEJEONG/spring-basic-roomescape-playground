package roomescape.waiting;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
@RequiredArgsConstructor
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/waitings")
    public WaitingResponse save(WaitingRequest waitingRequest){
        Time time = timeRepository.findById(waitingRequest.time())
                .orElseThrow(() -> new IllegalArgumentException("해당 시간이 존재하지 않습니다."));
        Theme theme = themeRepository.findById(waitingRequest.theme())
                .orElseThrow(() -> new IllegalArgumentException("해당 테마가 존재하지 않습니다."));
        Member member = memberRepository.findByName(waitingRequest.name())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        Waiting waiting = new Waiting(waitingRequest.name(),waitingRequest.date(), time, theme, member);
        waitingRepository.save(waiting);

        return new WaitingResponse(waiting.getId(), waiting.getName(), waiting.getDate(), waiting.getTime().getTime(), waiting.getTheme().getName());
    }

    public void deleteById(final Long id) {
        waitingRepository.deleteById(id);
    }
}
