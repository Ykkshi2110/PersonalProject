package vn.peterbui.myproject.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.peterbui.myproject.convert.ConvertUtils;
import vn.peterbui.myproject.domain.Company;
import vn.peterbui.myproject.domain.Role;
import vn.peterbui.myproject.domain.User;
import vn.peterbui.myproject.domain.response.Meta;
import vn.peterbui.myproject.domain.response.ResUserDTO;
import vn.peterbui.myproject.domain.response.ResultPaginationDTO;
import vn.peterbui.myproject.exception.IdInvalidException;
import vn.peterbui.myproject.exception.UserDoesNotExist;
import vn.peterbui.myproject.exception.UserExistedException;
import vn.peterbui.myproject.repository.CompanyRepository;
import vn.peterbui.myproject.repository.RoleRepository;
import vn.peterbui.myproject.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConvertUtils convertUtils;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;

    public ResUserDTO convertToResUserDTO(User user) {
        return modelMapper.map(user, ResUserDTO.class);
    }

    public ResultPaginationDTO getAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUsers = this.userRepository.findAll(spec, pageable);
        Page<ResUserDTO> pageUserDTOs = pageUsers.map(this::convertToResUserDTO);
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber()+1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageUserDTOs.getTotalPages());
        meta.setTotal(pageUserDTOs.getTotalElements());

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(pageUserDTOs.getContent());
        return resultPaginationDTO;
    }

    public User getUserById(long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new UserDoesNotExist("USER DOESN'T EXIST WITH ID = " + id));
    }

    public User handleCreateUser(@Valid User reqCreateUser) {
        if(this.userRepository.existsByEmail(reqCreateUser.getEmail())) throw new UserExistedException("USER EXISTED!");
        reqCreateUser.setPassword(passwordEncoder.encode(reqCreateUser.getPassword()));

        // check Company by id
        if(reqCreateUser.getCompany() != null){
            Company reqCompany = this.companyRepository.findById(reqCreateUser
                    .getCompany()
                    .getId()).orElseThrow(null);
            reqCreateUser.setCompany(reqCompany);
        }
        // check Role by id 
        if(reqCreateUser.getRole() != null){
            Role reqRole = this.roleRepository.findById(reqCreateUser
                    .getRole().getId()).orElse(null);
            reqCreateUser.setRole(reqRole);
        }

        return this.userRepository.save(reqCreateUser);
    }

    public User handleUpdateUser(User user) {
        User currentUser = this.userRepository.findById(user.getId())
                .orElseThrow(() -> new UserDoesNotExist("USER DOESN'T EXIST WITH ID = " + user.getId()));
        currentUser.setAddress(user.getAddress());
        currentUser.setName(user.getName());
        currentUser.setAge(user.getAge());
        currentUser.setGender(user.getGender());
        // check role by id
        if(user.getRole() != null){
            Role reqRole = this.roleRepository.findById(user.getRole().getId()).orElseThrow(() -> new IdInvalidException("Role does not exists"));
            currentUser.setRole(reqRole);
        }

        // check company by id
        if(user.getCompany() != null){
            Company reqCompany = this.companyRepository.findById(user.getCompany().getId()).orElseThrow(() -> new IdInvalidException("Company doesn't exists"));
            currentUser.setCompany(reqCompany);
        }

        return this.userRepository.save(currentUser);
    }


    public void handleDeleteUser(long id) {
        User currentUser = this.userRepository.findById(id).orElse(null);
        if (currentUser == null) {
            throw new UserDoesNotExist("USER DOES NOT EXIST!!!");
        } else {
            this.userRepository.delete(currentUser);
        }
    }

    public User handleGetUserByUserName(String email){
        return this.userRepository.getUserByEmail(email);
    }

    public void updateUserToken(String token, String email){
        User currentUser = this.handleGetUserByUserName(email);
        if(currentUser != null){
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserRefreshTokenAndEmail(String token, String email){
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

    public boolean checkEmailExists(String email){
        return this.userRepository.existsByEmail(email);
    }
}
