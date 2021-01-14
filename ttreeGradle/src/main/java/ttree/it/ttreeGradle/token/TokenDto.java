package ttree.it.ttreeGradle.token;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TokenDto {
    private Long id;
    private String email;
    private String token;
    private LocalDate createdDate;
    private boolean flag;

    public Token toEntity() {
        Token build = Token.builder()
                .email(email)
                .token(token)
                .createdDate(createdDate)
                .flag(flag)
                .build();
        return build;
    }

    @Builder
    public TokenDto(String email, String token, LocalDate createdDate, boolean flag) {
        this.email = email;
        this.token = token;
        this.createdDate = createdDate;
        this.flag = flag;
    }
}
