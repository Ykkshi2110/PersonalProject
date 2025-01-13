package vn.peterbui.myproject.service;


import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.peterbui.myproject.convert.SecurityUtil;
import vn.peterbui.myproject.domain.Job;
import vn.peterbui.myproject.domain.Resume;
import vn.peterbui.myproject.domain.User;
import vn.peterbui.myproject.domain.response.Meta;
import vn.peterbui.myproject.domain.response.ResResumeDTO;
import vn.peterbui.myproject.domain.response.ResultPaginationDTO;
import vn.peterbui.myproject.exception.IdInvalidException;
import vn.peterbui.myproject.repository.JobRepository;
import vn.peterbui.myproject.repository.ResumeRepository;
import vn.peterbui.myproject.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final FilterParser filterParser;
    private final FilterSpecificationConverter filterSpecificationConverter;
    private final UserService userService;
    private static final String ERROR_RESUME = "Resume does not exist";

    // convert ResResumeDTO with property companyName
    public ResResumeDTO convertToDTO(Resume resume) {
        ResResumeDTO resumeDTO = modelMapper.map(resume, ResResumeDTO.class);
        if(resume.getJob() != null){
            resumeDTO.setCompanyName(resume.getJob().getCompany().getName());
        }
        return resumeDTO;
    }


    public Resume handleCreateResume(@Valid Resume reqResume) {
        Job job = this.jobRepository
                .findById(reqResume
                        .getJob()
                        .getId())
                .orElseThrow(() -> new IdInvalidException("Job does not exists"));
        User user = this.userRepository
                .findById(reqResume
                        .getUser()
                        .getId())
                .orElseThrow(() -> new IdInvalidException("User does not exists"));
        reqResume.setUser(user);
        reqResume.setJob(job);
        return this.resumeRepository.save(reqResume);
    }

    public Resume handleUpdateResume(Resume reqResume) {
        Resume currentResume = this.resumeRepository
                .findById(reqResume.getId())
                .orElseThrow(() -> new IdInvalidException(ERROR_RESUME));

        currentResume.setStatus(reqResume.getStatus());
        return this.resumeRepository.save(currentResume);
    }

    public void handleDeleteResume(long id) {
        Resume currentResume = this.resumeRepository
                .findById(id)
                .orElseThrow(() -> new IdInvalidException(ERROR_RESUME));
        this.resumeRepository.delete(currentResume);
    }

    public ResultPaginationDTO handleFetchAllResumes(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> resumePage = this.resumeRepository.findAll(spec, pageable);
        Page<ResResumeDTO> resumeDTOPage = resumePage.map(this::convertToDTO);
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(resumeDTOPage.getTotalPages());
        meta.setTotal(resumeDTOPage.getTotalElements());
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(resumeDTOPage.getContent());
        return resultPaginationDTO;
    }

    public ResResumeDTO handleFetchResumeById(long id) {
        Resume currentResume = this.resumeRepository
                .findById(id)
                .orElseThrow(() -> new IdInvalidException(ERROR_RESUME));
        return this.convertToDTO(currentResume);
    }

    public ResultPaginationDTO fetchResumeByUser(Pageable pageable){
        // query builder
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true ? SecurityUtil.getCurrentUserLogin().get() : "";
        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> specification = filterSpecificationConverter.convert(node);
        Page<Resume> resumePage = this.resumeRepository.findAll(specification, pageable);
        Page<ResResumeDTO> resumeDTOPage = resumePage.map(this::convertToDTO);
        Meta meta = new Meta();
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(resumeDTOPage.getTotalPages());
        meta.setTotal(resumeDTOPage.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(resumeDTOPage.getContent());


        return resultPaginationDTO;
    }


}
