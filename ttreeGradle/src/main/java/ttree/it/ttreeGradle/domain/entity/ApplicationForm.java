package ttree.it.ttreeGradle.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ApplicationForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String year;

    @Column (nullable = false)
    private String semester;

    @Column(length = 100, nullable = false)
    private String teamName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String teamMember;

    @Column(nullable = false)
    private String origFilename;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String filePath;

    @Builder
    public ApplicationForm(Long id, String year, String semester, String teamName, String teamMember, String origFilename, String filename, String filePath){
        this.id = id;
        this.year = year;
        this.semester = semester;
        this.teamName = teamName;
        this.teamMember = teamMember;
        this.origFilename = origFilename;
        this.filename = filename;
        this.filePath = filePath;
    }


}
