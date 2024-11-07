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
import vn.peterbui.myproject.domain.Skill;
import vn.peterbui.myproject.domain.response.ResultPaginationDTO;
import vn.peterbui.myproject.service.SkillService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;

    @PostMapping("/skills/create")
    @ApiMessage("Create a skill")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill reqSkill) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.handleCreateSkill(reqSkill));
    }

    @PutMapping("/skills/update")
    @ApiMessage("Update a skill")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill reqSkill) {
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.handleUpdateSkill(reqSkill));
    }

    @DeleteMapping("/skills/delete/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> deleteSkill(@PathVariable long id) {
        this.skillService.handleDeleteSkill(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/skills")
    @ApiMessage("Fetch all skill")
    public ResponseEntity<ResultPaginationDTO> getAllSkills(@Filter Specification<Skill> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.handleFetchAllSkill(spec, pageable));
    }
}
