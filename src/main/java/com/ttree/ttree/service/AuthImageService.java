package com.ttree.ttree.service;


import com.ttree.ttree.domain.entity.AuthImage;
import com.ttree.ttree.domain.repository.AuthImageRepository;
import com.ttree.ttree.dto.AuthImageDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AuthImageService {
    private AuthImageRepository authImageRepository;

    public AuthImageService(AuthImageRepository authImageRepository) {this.authImageRepository = authImageRepository;}

    @Transactional
    public Long saveAuthImage(AuthImageDto authImageDto) { return authImageRepository.save(authImageDto.toEntity()).getId();}

    @Transactional
    public AuthImageDto getAuthImage(Long id) {
        AuthImage authImage = authImageRepository.findById(id).get();

        AuthImageDto authImageDto = AuthImageDto.builder()
                .id(id)
                .origFilename(authImage.getOrigFilename())
                .filename(authImage.getFilename())
                .filePath(authImage.getFilePath())
                .build();
        return authImageDto;
    }

    @Transactional
    public void deleteFile(Long id) {
        authImageRepository.deleteById(id);
    }
}
