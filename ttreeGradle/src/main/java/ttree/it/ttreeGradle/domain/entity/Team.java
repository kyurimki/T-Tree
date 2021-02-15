package ttree.it.ttreeGradle.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(length = 100, nullable = false)
    private String teamName;

    @Column (nullable = false)
    private String teamYear;

    @Column (nullable = false)
    private String teamSemester;

    @Column(columnDefinition = "TEXT")
    private String teamStatus1;

    @Column(columnDefinition = "TEXT")
    private String teamStatus2;

    @Column(columnDefinition = "TEXT")
    private String teamStatus3;

    @Column(columnDefinition = "TEXT")
    private String teamStatus4;

    @Column(columnDefinition = "TEXT")
    private String finalStatus;

    @Column(columnDefinition = "TEXT")
    private String finalStatusReason;


    @Builder
    public Team(Long teamId, String teamName, String teamYear, String teamSemester, String teamStatus1, String teamStatus2, String teamStatus3, String teamStatus4, String finalStatus, String finalStatusReason) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamYear = teamYear;
        this.teamSemester = teamSemester;
        this.teamStatus1 = teamStatus1;
        this.teamStatus2 = teamStatus2;
        this.teamStatus3 = teamStatus3;
        this.teamStatus4 = teamStatus4;
        this.finalStatus = finalStatus;
        this.finalStatusReason = finalStatusReason;
    }
}