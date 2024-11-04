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
import vn.peterbui.myproject.domain.Company;
import vn.peterbui.myproject.domain.dto.ResCompanyDTO;
import vn.peterbui.myproject.domain.dto.ResultPaginationDTO;
import vn.peterbui.myproject.service.CompanyService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final ModelMapper modelMapper;

    public ResCompanyDTO mapToDTO(Company company) {
        return modelMapper.map(company, ResCompanyDTO.class);
    }

    @PostMapping("/companies/create")
    @ApiMessage("Create a company")
    public ResponseEntity<ResCompanyDTO> createCompany(@Valid @RequestBody Company company) {
        ResCompanyDTO resCompanyDTO = mapToDTO(this.companyService.handleCreateCompany(company));
        return ResponseEntity.status(HttpStatus.CREATED).body(resCompanyDTO);
    }

    @PutMapping("/companies/update")
    @ApiMessage("Update a company")
    public ResponseEntity<ResCompanyDTO> updateCompany(@Valid @RequestBody Company company) {
        ResCompanyDTO resCompanyDTO = mapToDTO(this.companyService.handleUpdateCompany(company));
        return ResponseEntity.status(HttpStatus.OK).body(resCompanyDTO);
    }

    @DeleteMapping("/companies/delete/{id}")
    @ApiMessage("Delete a company")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        this.companyService.deleteCompany(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/companies")
    @ApiMessage("Fetch all companies")
    public ResponseEntity<ResultPaginationDTO> fetchAllCompanies(@Filter Specification<Company> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.getAllCompanies(spec, pageable));
    }

    @GetMapping("/companies/{id}")
    @ApiMessage("Fetch company by id")
    public ResponseEntity<ResCompanyDTO> fetchCompanyById(@PathVariable Long id) {
        ResCompanyDTO resCompanyDTO = mapToDTO(this.companyService.getCompanyById(id));
        return ResponseEntity.status(HttpStatus.OK).body(resCompanyDTO);
    }
}
