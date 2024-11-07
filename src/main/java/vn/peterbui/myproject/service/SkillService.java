package vn.peterbui.myproject.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.peterbui.myproject.domain.Skill;
import vn.peterbui.myproject.domain.response.Meta;
import vn.peterbui.myproject.domain.response.ResultPaginationDTO;
import vn.peterbui.myproject.exception.IdInvalidException;
import vn.peterbui.myproject.repository.SkillRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;
    private static final String ERROR_SKILL = "Skill doesn't exist";

    public Skill handleCreateSkill(@Valid Skill reqSkill) {
        if(this.skillRepository.existsByName(reqSkill.getName())) {
            throw new IdInvalidException("Skill already exists");
        }
        return this.skillRepository.save(reqSkill);
    }

    public Skill handleUpdateSkill(@Valid Skill reqSkill) {
        Skill currentSkill = this.skillRepository.findById(reqSkill.getId()).orElseThrow(() -> new IdInvalidException(ERROR_SKILL));
        currentSkill.setName(reqSkill.getName());
        return this.skillRepository.save(currentSkill);
    }

    public ResultPaginationDTO handleFetchAllSkill(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageSkills = this.skillRepository.findAll(spec, pageable);
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageSkills.getTotalPages());
        meta.setTotal(pageSkills.getTotalElements());
        ResultPaginationDTO paginationDTO = new ResultPaginationDTO();
        paginationDTO.setMeta(meta);
        paginationDTO.setResult(pageSkills.getContent());
        return paginationDTO;
    }

    public void handleDeleteSkill(long id) {
        Skill currentSkill = this.skillRepository.findById(id).orElseThrow(() -> new IdInvalidException(ERROR_SKILL));
        currentSkill.getJobs().forEach(currentJob -> currentJob.getSkills().remove(currentSkill));
        this.skillRepository.delete(currentSkill);
    }
}
