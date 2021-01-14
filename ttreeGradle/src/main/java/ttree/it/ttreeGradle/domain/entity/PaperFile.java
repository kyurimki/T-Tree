package ttree.it.ttreeGradle.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaperFile {

    @Id
    private Long paper_id;

    @Column(nullable = false)
    private String paper_origFilename;

    @Column(nullable = false)
    private String paper_filename;

    @Column(nullable = false)
    private String paper_filePath;

    @Builder
    public PaperFile(Long paper_id, String paper_origFilename, String paper_filename, String paper_filePath) {
        this.paper_id = paper_id;
        this.paper_origFilename = paper_origFilename;
        this.paper_filename = paper_filename;
        this.paper_filePath = paper_filePath;
    }
}