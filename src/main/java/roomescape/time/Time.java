package roomescape.time;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String time;

    public Time(Long id, String time) {
        this.id = id;
        this.time = time;
    }

    public Time(String time) {
        this.time = time;
    }

    public Time() {

    }
}
