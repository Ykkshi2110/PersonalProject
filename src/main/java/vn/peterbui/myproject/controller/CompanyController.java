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
import vn.peterbui.myproject.domain.dto.CompanyDTO;
import vn.peterbui.myproject.domain.dto.ResultPaginationDTO;
import vn.peterbui.myproject.service.CompanyService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final ModelMapper modelMapper;

    public CompanyDTO mapToDTO(Company company) {
        return modelMapper.map(company, CompanyDTO.class);
    }

    @PostMapping("/companies/create")
    @ApiMessage("Create a company")
    public ResponseEntity<CompanyDTO> createCompany(@Valid @RequestBody Company company) {
        CompanyDTO companyDTO = mapToDTO(this.companyService.handleCreateCompany(company));
        return ResponseEntity.status(HttpStatus.CREATED).body(companyDTO);
    }

    @PutMapping("/companies/update")
    @ApiMessage("Update a company")
    public ResponseEntity<CompanyDTO> updateCompany(@Valid @RequestBody Company company) {
        CompanyDTO companyDTO = mapToDTO(this.companyService.handleUpdateCompany(company));
        return ResponseEntity.status(HttpStatus.OK).body(companyDTO);
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
    public ResponseEntity<CompanyDTO> fetchCompanyById(@PathVariable Long id) {
        CompanyDTO companyDTO = mapToDTO(this.companyService.getCompanyById(id));
        return ResponseEntity.status(HttpStatus.OK).body(companyDTO);
    }
}
