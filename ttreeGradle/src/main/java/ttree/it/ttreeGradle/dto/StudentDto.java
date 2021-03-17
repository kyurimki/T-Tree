package ttree.it.ttreeGradle.dto;

public class StudentDto {
    private UserDto userDto;
    private TeamDto teamDto;

    public UserDto getUserDto() {
        return userDto;
    }

    public TeamDto getTeamDto() {
        return teamDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public void setTeamDto(TeamDto teamDto) {
        this.teamDto = teamDto;
    }
}
