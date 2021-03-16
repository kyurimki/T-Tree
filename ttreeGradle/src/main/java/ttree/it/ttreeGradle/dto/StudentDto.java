package ttree.it.ttreeGradle.dto;

public class StudentDto {
    private Long id;
    private UserDto userDto;
    private TeamDto teamDto;

    public StudentDto(Long id, UserDto userDto, TeamDto teamDto) {
        this.id = id;
        this.userDto = userDto;
        this.teamDto = teamDto;
    }
}
