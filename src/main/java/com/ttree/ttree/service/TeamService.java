package com.ttree.ttree.service;

import com.ttree.ttree.domain.entity.Team;
import com.ttree.ttree.domain.repository.TeamRepository;
import com.ttree.ttree.dto.TeamDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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

    @Transactional
    public List<TeamDto> getTeamListByName(String teamName) {
        List<Team> teamList = teamRepository.findAll();
        List<TeamDto> teamDtoList = new ArrayList<>();
        for(Team team : teamList) {
            if(teamName.equals(team.getTeamName())) {
                Long id = team.getTeamId();
                TeamDto teamDto = getTeam(id);
                teamDtoList.add(teamDto);
            }
        }
        return teamDtoList;
    }

    @Transactional
    public List<TeamDto> getTeamList() {
        List<Team> teamList = teamRepository.findAll();
        List<TeamDto> teamDtoList = new ArrayList<>();
        for(Team team : teamList) {
            Long id = team.getTeamId();
            TeamDto teamDto = getTeam(id);
            teamDtoList.add(teamDto);
        }
        return teamDtoList;
    }

    @Transactional
    public List<TeamDto> searchTeamByTime(String yearToSearch, String semesterToSearch) {
        List<Team> teamList = teamRepository.findAll();
        List<TeamDto> teamDtoList = new ArrayList<>();
        for(Team team : teamList) {
            if(yearToSearch.equals(team.getTeamYear()) && semesterToSearch.equals(team.getTeamSemester())) {
                Long id = team.getTeamId();
                TeamDto teamDto = getTeam(id);
                teamDtoList.add(teamDto);
            }
        }
        return teamDtoList;
    }
}
