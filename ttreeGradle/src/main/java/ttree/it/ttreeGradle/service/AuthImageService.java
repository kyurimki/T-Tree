package ttree.it.ttreeGradle.service;



import org.springframework.stereotype.Service;
import ttree.it.ttreeGradle.domain.entity.AuthImage;
import ttree.it.ttreeGradle.domain.repository.AuthImageRepository;
import ttree.it.ttreeGradle.dto.AuthImageDto;

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
