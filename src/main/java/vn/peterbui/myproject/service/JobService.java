package vn.peterbui.myproject.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.peterbui.myproject.domain.Job;
import vn.peterbui.myproject.domain.Skill;
import vn.peterbui.myproject.domain.response.Meta;
import vn.peterbui.myproject.domain.response.ResJobDTO;
import vn.peterbui.myproject.domain.response.ResultPaginationDTO;
import vn.peterbui.myproject.exception.IdInvalidException;
import vn.peterbui.myproject.repository.JobRepository;
import vn.peterbui.myproject.repository.SkillRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final ModelMapper modelMapper;
    private static final String ERROR_JOB = "Job doesn't exist";

    public ResJobDTO convertJobToDTO(Job job) {
        ResJobDTO resJobDTO = modelMapper.map(job, ResJobDTO.class);
        List<String> skills = job
                .getSkills()
                .stream()
                .map(Skill::getName)
                .toList();
        resJobDTO.setSkills(skills);
        return resJobDTO;
    }

    public ResJobDTO handleCreateJob(@Valid Job reqJob) {
        // check List skill
        if (reqJob.getSkills() != null) {
            List<Long> idSkills = reqJob
                    .getSkills()
                    .stream()
                    .map(Skill::getId)
                    .toList();
            List<Skill> skills = this.skillRepository.findAllById(idSkills);
            reqJob.setSkills(skills);
        }
        this.jobRepository.save(reqJob);

        return this.convertJobToDTO(reqJob);
    }

    public ResJobDTO handleUpdateJob(@Valid Job reqJob) {
        Job currentJob = this.jobRepository
                .findById(reqJob.getId())
                .orElseThrow(() -> new IdInvalidException(ERROR_JOB));
        // check List skill
        if (reqJob.getSkills() != null) {
            List<Long> idSkills = reqJob
                    .getSkills()
                    .stream()
                    .map(Skill::getId)
                    .toList();
            List<Skill> skills = this.skillRepository.findAllById(idSkills);
            currentJob.setSkills(skills);
        }

        currentJob.setDescription(reqJob.getDescription());
        currentJob.setActive(reqJob.isActive());
        currentJob.setName(reqJob.getName());
        currentJob.setLevel(reqJob.getLevel());
        currentJob.setQuantity(reqJob.getQuantity());
        currentJob.setLocation(reqJob.getLocation());
        currentJob.setSalary(reqJob.getSalary());
        currentJob.setStartDate(reqJob.getStartDate());
        currentJob.setEndDate(reqJob.getEndDate());
        this.jobRepository.save(currentJob);

        return this.convertJobToDTO(currentJob);
    }

    public void handleDeleteJob(long id) {
        Job currentJob = this.jobRepository
                .findById(id)
                .orElseThrow(() -> new IdInvalidException(ERROR_JOB));
        this.jobRepository.delete(currentJob);
    }

    public ResultPaginationDTO handleFetchAllJob(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJobs = this.jobRepository.findAll(spec, pageable);
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageJobs.getTotalPages());
        meta.setTotal(pageJobs.getTotalElements());
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(pageJobs.getContent());
        return resultPaginationDTO;
    }

    public Job handleFetchJobById(long id) {
        return this.jobRepository
                .findById(id)
                .orElseThrow(() -> new IdInvalidException(ERROR_JOB));
    }


}
