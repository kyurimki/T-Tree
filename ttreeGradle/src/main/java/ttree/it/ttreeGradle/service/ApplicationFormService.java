package ttree.it.ttreeGradle.service;

import org.springframework.stereotype.Service;
import ttree.it.ttreeGradle.domain.entity.ApplicationForm;
import ttree.it.ttreeGradle.domain.repository.ApplicationFormRepository;
import ttree.it.ttreeGradle.dto.ApplicationFormDto;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationFormService {

    private ApplicationFormRepository applicationFormRepository;

    public ApplicationFormService(ApplicationFormRepository applicationFormRepository){
        this.applicationFormRepository = applicationFormRepository;
    }

    @Transactional
    public Long saveApplicationForm(ApplicationFormDto applicationFormDto){
        return applicationFormRepository.save(applicationFormDto.toEntity()).getId();
    }

    @Transactional
    public ApplicationFormDto getApplicationForm(Long id){
        ApplicationForm applicationForm = applicationFormRepository.findById(id).get();

        ApplicationFormDto applicationFormDto = ApplicationFormDto.builder()
                .id(applicationForm.getId())
                .year(applicationForm.getYear())
                .semester(applicationForm.getSemester())
                .teamName(applicationForm.getTeamName())
                .teamMember(applicationForm.getTeamMember())
                .origFilename(applicationForm.getOrigFilename())
                .filename(applicationForm.getFilename())
                .filePath(applicationForm.getFilePath())
                .build();

        return applicationFormDto;

    }

    public List<ApplicationFormDto> getApplicationFormList(){
        List<ApplicationForm> applicationFormList = applicationFormRepository.findAll();
        List<ApplicationFormDto> applicationFormDtoList = new ArrayList<>();

        for(ApplicationForm applicationForm : applicationFormList){
            ApplicationFormDto applicationFormDto = ApplicationFormDto.builder()
                    .id(applicationForm.getId())
                    .year(applicationForm.getYear())
                    .semester(applicationForm.getSemester())
                    .teamName(applicationForm.getTeamName())
                    .teamMember(applicationForm.getTeamMember())
                    .origFilename(applicationForm.getOrigFilename())
                    .build();
            applicationFormDtoList.add(applicationFormDto);

        }
        return applicationFormDtoList;
    }

    @Transactional
    public void deleteApplicationForm(Long id){
        applicationFormRepository.deleteById(id);
    }

}
