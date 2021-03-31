package com.ttree.ttree.domain.entity;

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
public class ProposalFile {

    @Id
    private Long proposal_id;

    @Column(nullable = false)
    private String proposal_origFilename;

    @Column(nullable = false)
    private String proposal_filename;

    @Column(nullable = false)
    private String proposal_filePath;

    @Builder
    public ProposalFile(Long proposal_id, String proposal_origFilename, String proposal_filename, String proposal_filePath){
        this.proposal_id = proposal_id;
        this.proposal_origFilename = proposal_origFilename;
        this.proposal_filename = proposal_filename;
        this.proposal_filePath = proposal_filePath;
    }
}
