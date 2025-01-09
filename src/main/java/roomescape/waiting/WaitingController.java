package roomescape.waiting;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.AuthClaims;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class WaitingController {

    private final WaitingService waitingService;
    @PostMapping("/waitings")
    public ResponseEntity create(@RequestBody WaitingRequest waitingRequest, AuthClaims userClaims) {
        if (waitingRequest.date() == null
                || waitingRequest.theme() == null
                || waitingRequest.time() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (waitingRequest.name() == null) {
            waitingRequest = new WaitingRequest(
                    userClaims.name(),
                    waitingRequest.date(),
                    waitingRequest.theme(),
                    waitingRequest.time()
            );
        }

        WaitingResponse waiting = waitingService.save(waitingRequest);
        return ResponseEntity.created(URI.create("/waitings/" + waiting.id())).body(waiting);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        waitingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
