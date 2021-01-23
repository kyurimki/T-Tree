package com.ttree.ttree.service;

import com.ttree.ttree.domain.entity.Board;
import com.ttree.ttree.domain.entity.Team;
import com.ttree.ttree.domain.repository.TeamRepository;
import com.ttree.ttree.dto.BoardDto;
import com.ttree.ttree.dto.TeamDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TeamService {
    private TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) { this.teamRepository = teamRepository; }

    @Transactional
    public Long saveTeam(TeamDto teamDto) {
        return teamRepository.save(teamDto.toEntity()).getTeamId();
    }

    @Transactional
    public TeamDto getTeam(Long id) {
        Team team = teamRepository.findById(id).get();

        TeamDto teamDto = TeamDto.builder()
                .teamId(team.getTeamId())
                .teamName(team.getTeamName())
                .teamYear(team.getTeamYear())
                .teamSemester(team.getTeamSemester())
                .teamStatus1(team.getTeamStatus1())
                .teamStatus2(team.getTeamStatus2())
                .teamStatus3(team.getTeamStatus3())
                .teamStatus4(team.getTeamStatus4())
                .finalStatus(team.getFinalStatus())
                .finalStatusReason(team.getFinalStatusReason())
                .build();
        return teamDto;
    }
}
