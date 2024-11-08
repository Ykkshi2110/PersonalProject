package vn.peterbui.myproject.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.peterbui.myproject.convert.annotation.ApiMessage;
import vn.peterbui.myproject.domain.Resume;
import vn.peterbui.myproject.domain.response.ResCreateResumeDTO;
import vn.peterbui.myproject.domain.response.ResResumeDTO;
import vn.peterbui.myproject.domain.response.ResUpdateResumeDTO;
import vn.peterbui.myproject.domain.response.ResultPaginationDTO;
import vn.peterbui.myproject.service.ResumeService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;
    private final ModelMapper modelMapper;

    @PostMapping("/resumes/create")
    @ApiMessage("Create a resume")
    public ResponseEntity<ResCreateResumeDTO> createResume(@Valid @RequestBody Resume reqResume) {
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(this.resumeService.handleCreateResume(reqResume), ResCreateResumeDTO.class));
    }

    @PutMapping("/resumes/update")
    @ApiMessage("Update a resume")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume reqResume) {
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(this.resumeService.handleUpdateResume(reqResume), ResUpdateResumeDTO.class));
    }

    @DeleteMapping("/resumes/delete/{id}")
    @ApiMessage("Delete a resume")
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) {
        this.resumeService.handleDeleteResume(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch all resumes")
    public ResponseEntity<ResultPaginationDTO> fetchAllResumes(@Filter Specification<Resume> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.resumeService.handleFetchAllResumes(spec, pageable));
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Fetch resume by id")
    public ResponseEntity<ResResumeDTO> fetchResume(@PathVariable Long id) {
        return ResponseEntity.ok().body(this.resumeService.handleFetchResumeById(id));
    }
}
