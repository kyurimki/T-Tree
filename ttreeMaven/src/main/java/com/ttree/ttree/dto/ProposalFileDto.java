package com.ttree.ttree.dto;

import com.ttree.ttree.domain.entity.ProposalFile;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProposalFileDto {
    private Long proposal_id;
    private String proposal_origFilename;
    private String proposal_filename;
    private String proposal_filePath;

    public ProposalFile toEntity(){
        ProposalFile build = ProposalFile.builder()
                .proposal_id(proposal_id)
                .proposal_origFilename(proposal_origFilename)
                .proposal_filename(proposal_filename)
                .proposal_filePath(proposal_filePath)
                .build();
        return build;
    }

    @Builder
    public ProposalFileDto(Long proposal_id, String proposal_origFilename, String proposal_filename, String proposal_filePath){
        this.proposal_id = proposal_id;
        this.proposal_origFilename = proposal_origFilename;
        this.proposal_filename = proposal_filename;
        this.proposal_filePath = proposal_filePath;
    }
}
