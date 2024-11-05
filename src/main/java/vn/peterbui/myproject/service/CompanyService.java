package vn.peterbui.myproject.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.peterbui.myproject.domain.Company;
import vn.peterbui.myproject.domain.User;
import vn.peterbui.myproject.domain.response.Meta;
import vn.peterbui.myproject.domain.response.ResCompanyDTO;
import vn.peterbui.myproject.domain.response.ResultPaginationDTO;
import vn.peterbui.myproject.exception.IdInvalidException;
import vn.peterbui.myproject.repository.CompanyRepository;
import vn.peterbui.myproject.repository.UserRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    private static final String ERROR_COMPANY = "Company doesn't exist";

    public Company handleCreateCompany(@Valid Company reqCompany) {
        Company company = new Company();
        company.setName(reqCompany.getName());
        company.setDescription(reqCompany.getDescription());
        company.setAddress(reqCompany.getAddress());
        company.setLogo(reqCompany.getLogo());
        return companyRepository.save(company);
    }

    public Company handleUpdateCompany(Company reqCompany) {
        Company currentCompany = companyRepository
                .findById(reqCompany.getId())
                .orElseThrow(() -> new IdInvalidException(ERROR_COMPANY));
        currentCompany.setName(reqCompany.getName());
        currentCompany.setDescription(reqCompany.getDescription());
        currentCompany.setAddress(reqCompany.getAddress());
        currentCompany.setLogo(reqCompany.getLogo());
        return companyRepository.save(currentCompany);
    }

    public void deleteCompany(long id) {
        Company currentCompany = this.companyRepository
                .findById(id)
                .orElse(null);
        if (currentCompany == null) {
            throw new IdInvalidException(ERROR_COMPANY);
        } else {
            List<User> users = currentCompany.getUsers();
            this.userRepository.deleteAll(users);
            this.companyRepository.deleteById(id);
        }
    }

    public ResultPaginationDTO getAllCompanies(Specification<Company>spec, Pageable pageable) {
        Page<Company> pageCompanies = this.companyRepository.findAll(spec, pageable);

        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageCompanies.getTotalPages());
        meta.setTotal(pageCompanies.getTotalElements());
        ResultPaginationDTO paginationDTO = new ResultPaginationDTO();
        paginationDTO.setMeta(meta);
        paginationDTO.setResult(pageCompanies.getContent());
        return paginationDTO;
    }

    public Company getCompanyById(long id) {
        return this.companyRepository.findById(id).orElseThrow(() -> new IdInvalidException(ERROR_COMPANY));
    }
}
