package vn.peterbui.myproject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.peterbui.myproject.convert.ConvertUtils;
import vn.peterbui.myproject.domain.Meta;
import vn.peterbui.myproject.domain.Role;
import vn.peterbui.myproject.domain.User;
import vn.peterbui.myproject.domain.dto.CreateUserRequest;
import vn.peterbui.myproject.domain.dto.ResultPaginationDTO;
import vn.peterbui.myproject.domain.dto.UserDTO;
import vn.peterbui.myproject.exception.IdInvalidException;
import vn.peterbui.myproject.exception.UserDoesNotExist;
import vn.peterbui.myproject.exception.UserExistedException;
import vn.peterbui.myproject.repository.RoleRepository;
import vn.peterbui.myproject.repository.UserRepository;
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConvertUtils convertUtils;
    private final RoleRepository roleRepository;

    public ResultPaginationDTO getAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUsers = this.userRepository.findAll(spec, pageable);
        Page<UserDTO> pageUserDTOs = pageUsers.map(convertUtils::convertToDto);
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

    public User handleCreateUser(@Valid CreateUserRequest createUserRequest) {
        User user = new User();
        if(this.userRepository.existsByEmail(createUserRequest.getEmail())) throw new UserExistedException("USER EXISTED!");
        user.setAddress(createUserRequest.getAddress());
        user.setAvatar(createUserRequest.getAvatar());
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        user.setFullName(createUserRequest.getFullName());
        user.setPhone(createUserRequest.getPhone());

        // check Role by id 
        Role reqRole = this.roleRepository.findById(createUserRequest.getRole().getId()).orElseThrow(() -> new IdInvalidException("Role does not exists"));
        user.setRole(reqRole);
        
        return this.userRepository.save(user);
    }

    public User handleUpdateUser(User user) {
        User currentUser = this.userRepository.findById(user.getId())
                .orElseThrow(() -> new UserDoesNotExist("USER DOESN'T EXIST WITH ID = " + user.getId()));
        currentUser.setAddress(user.getAddress());
        currentUser.setFullName(user.getFullName());
        currentUser.setPhone(user.getPhone());

        // check role by id 
        Role reqRole = this.roleRepository.findById(user.getRole().getId()).orElseThrow(() -> new IdInvalidException("Role does not exists"));
        currentUser.setRole(reqRole);

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

}
