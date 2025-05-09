package vn.peterbui.myproject.controller;

import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.peterbui.myproject.convert.SecurityUtil;
import vn.peterbui.myproject.convert.annotation.ApiMessage;
import vn.peterbui.myproject.domain.Company;
import vn.peterbui.myproject.domain.Job;
import vn.peterbui.myproject.domain.Resume;
import vn.peterbui.myproject.domain.User;
import vn.peterbui.myproject.domain.response.ResCreateResumeDTO;
import vn.peterbui.myproject.domain.response.ResResumeDTO;
import vn.peterbui.myproject.domain.response.ResUpdateResumeDTO;
import vn.peterbui.myproject.domain.response.ResultPaginationDTO;
import vn.peterbui.myproject.service.ResumeService;
import vn.peterbui.myproject.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final FilterSpecificationConverter filterSpecificationConverter;
    private final FilterBuilder filterBuilder;

    @PostMapping("/resumes")
    @ApiMessage("Create a resume")
    public ResponseEntity<ResCreateResumeDTO> createResume(@Valid @RequestBody Resume reqResume) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(modelMapper.map(this.resumeService.handleCreateResume(reqResume), ResCreateResumeDTO.class));
    }

    @PutMapping("/resumes")
    @ApiMessage("Update a resume")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume reqResume) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modelMapper.map(this.resumeService.handleUpdateResume(reqResume), ResUpdateResumeDTO.class));
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete a resume")
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) {
        this.resumeService.handleDeleteResume(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch all resumes")
    public ResponseEntity<ResultPaginationDTO> fetchAllResumes(@Filter Specification<Resume> spec, Pageable pageable) {

        List<Long> arrJobIds = null;
        String email = SecurityUtil
                .getCurrentUserLogin()
                .isPresent() == true ? SecurityUtil
                .getCurrentUserLogin()
                .get() : "";
        User currentUser = this.userService.handleGetUserByUserName(email);
        if (currentUser != null) {
            Company userCompany = currentUser.getCompany();
            if (userCompany != null) {
                List<Job> companyJobs = userCompany.getJobs();
                if (companyJobs != null && companyJobs.size() > 0) {
                    arrJobIds = companyJobs
                            .stream()
                            .map(x -> x.getId())
                            .collect(Collectors.toList());
                }
            }
        }

        Specification<Resume> jobInSpec = filterSpecificationConverter.convert(filterBuilder
                .field("job")
                .in(filterBuilder.input(arrJobIds))
                .get());
        Specification<Resume> finalSpec = jobInSpec.and(spec);

        return ResponseEntity
                .ok()
                .body(this.resumeService.handleFetchAllResumes(finalSpec, pageable));
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Fetch resume by id")
    public ResponseEntity<ResResumeDTO> fetchResume(@PathVariable Long id) {
        return ResponseEntity
                .ok()
                .body(this.resumeService.handleFetchResumeById(id));
    }

    @PostMapping("/resumes/by-user")
    @ApiMessage("Get list resumes by user")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable) {
        return ResponseEntity
                .ok()
                .body(this.resumeService.fetchResumeByUser(pageable));
    }
}
