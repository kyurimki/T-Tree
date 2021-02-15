package ttree.it.ttreeGradle.token;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {

    @Id
    public String email;

    public String token;

    public LocalDate createdDate;

    @Builder
    public Token(String email, String token, LocalDate createdDate) {
        this.email = email;
        this.token = token;
        this.createdDate = createdDate;
    }
}
