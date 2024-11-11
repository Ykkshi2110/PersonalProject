package vn.peterbui.myproject.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.peterbui.myproject.convert.annotation.ApiMessage;
import vn.peterbui.myproject.domain.Job;
import vn.peterbui.myproject.domain.response.ResCreateJobDTO;
import vn.peterbui.myproject.domain.response.ResUpdateJobDTO;
import vn.peterbui.myproject.domain.response.ResultPaginationDTO;
import vn.peterbui.myproject.repository.JobRepository;
import vn.peterbui.myproject.service.JobService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @PostMapping("/jobs")
    @ApiMessage("Create a job")
    public ResponseEntity<ResCreateJobDTO> createJob(@Valid @RequestBody Job reqJob) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.handleCreateJob(reqJob));
    }

    @PutMapping("/jobs")
    @ApiMessage("Update a job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@RequestBody Job reqJob) {
        Job currentJob = this.jobService.handleFetchJobById(reqJob.getId());
        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.handleUpdateJob(reqJob, currentJob));
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete a job")
    public ResponseEntity<Void> deleteJob(@PathVariable long id) {
        this.jobService.handleDeleteJob(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/jobs")
    @ApiMessage("Fetch all job")
    public ResponseEntity<ResultPaginationDTO> fetchAllJob(@Filter Specification<Job> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.handleFetchAllJob(spec, pageable));
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Fetch a job by id")
    public ResponseEntity<Job> fetchJobById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.handleFetchJobById(id));
    }
}
