package vn.peterbui.myproject.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.peterbui.myproject.domain.Company;
import vn.peterbui.myproject.domain.Job;
import vn.peterbui.myproject.domain.Skill;
import vn.peterbui.myproject.domain.response.Meta;
import vn.peterbui.myproject.domain.response.ResCreateJobDTO;
import vn.peterbui.myproject.domain.response.ResUpdateJobDTO;
import vn.peterbui.myproject.domain.response.ResultPaginationDTO;
import vn.peterbui.myproject.exception.IdInvalidException;
import vn.peterbui.myproject.repository.CompanyRepository;
import vn.peterbui.myproject.repository.JobRepository;
import vn.peterbui.myproject.repository.SkillRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final ModelMapper modelMapper;
    private final CompanyRepository companyRepository;
    private static final String ERROR_JOB = "Job doesn't exist";

    public ResCreateJobDTO convertCreateJobToDTO(Job job) {
        ResCreateJobDTO resCreateJobDTO = modelMapper.map(job, ResCreateJobDTO.class);
        List<String> skills = job
                .getSkills()
                .stream()
                .map(Skill::getName)
                .toList();
        resCreateJobDTO.setSkills(skills);
        return resCreateJobDTO;
    }

    public ResUpdateJobDTO convertUpdateJobToDTO(Job job) {
        ResUpdateJobDTO resUpdateJobDTO = modelMapper.map(job, ResUpdateJobDTO.class);
        List<String> skills = job
                .getSkills()
                .stream()
                .map(Skill::getName)
                .toList();
        resUpdateJobDTO.setSkills(skills);
        return resUpdateJobDTO;
    }

    public ResCreateJobDTO handleCreateJob(@Valid Job reqJob) {
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

        return this.convertCreateJobToDTO(reqJob);
    }

    public ResUpdateJobDTO handleUpdateJob(Job reqJob, Job jobInDB) {
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
            jobInDB.setSkills(skills);
        }

        // check Company
        if(reqJob.getCompany() != null){
            Optional<Company> company = this.companyRepository.findById(reqJob.getCompany().getId());
            company.ifPresent(jobInDB::setCompany);
        }

        jobInDB.setActive(reqJob.isActive());
        jobInDB.setName(reqJob.getName());
        jobInDB.setLevel(reqJob.getLevel());
        jobInDB.setQuantity(reqJob.getQuantity());
        jobInDB.setLocation(reqJob.getLocation());
        jobInDB.setSalary(reqJob.getSalary());
        jobInDB.setStartDate(reqJob.getStartDate());
        jobInDB.setEndDate(reqJob.getEndDate());
        this.jobRepository.save(currentJob);

        return this.convertUpdateJobToDTO(currentJob);
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
