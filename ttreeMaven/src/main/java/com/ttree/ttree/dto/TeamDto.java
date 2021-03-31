package com.ttree.ttree.dto;

import com.ttree.ttree.domain.entity.Team;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TeamDto {
    private Long teamId;
    private String teamName;
    private String teamYear;
    private String teamSemester;
    private String teamStatus1;
    private String teamStatus2;
    private String teamStatus3;
    private String teamStatus4;
    private String finalStatus;
    private String finalStatusReason;

    public Team toEntity() {
        Team build = Team.builder()
                .teamId(teamId)
                .teamName(teamName)
                .teamYear(teamYear)
                .teamSemester(teamSemester)
                .teamStatus1(teamStatus1)
                .teamStatus2(teamStatus2)
                .teamStatus3(teamStatus3)
                .teamStatus4(teamStatus4)
                .finalStatus(finalStatus)
                .finalStatusReason(finalStatusReason)
                .build();
        return build;
    }

    @Builder
    public TeamDto(Long teamId, String teamName, String teamYear, String teamSemester, String teamStatus1, String teamStatus2, String teamStatus3, String teamStatus4, String finalStatus, String finalStatusReason) {
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
