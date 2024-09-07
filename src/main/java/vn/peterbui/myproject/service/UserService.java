package vn.peterbui.myproject.service;

import java.util.Set;
import java.util.stream.Collectors; 
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
import vn.peterbui.myproject.exception.UserDoesNotExist;
import vn.peterbui.myproject.exception.UserExistedException;
import vn.peterbui.myproject.repository.UserRepository;
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final ConvertUtils convertUtils;
    

    public ResultPaginationDTO getAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUsers = this.userRepository.findAll(spec, pageable);
        Page<UserDTO> pageUserDTOs = pageUsers.map(convertUtils::convertToDto);
        Meta meta = new Meta();
        meta.setCurrent(pageable.getPageNumber()+1);
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
        Set<Role> roles = createUserRequest.getRoleType().stream()
                .map(roleService::findRoleByName)
                .collect(Collectors.toSet());
        user.setRoles(roles);
        return this.userRepository.save(user);
    }

    public User handleUpdateUser(User user) {
        User currentUser = this.userRepository.findById(user.getId())
                .orElseThrow(() -> new UserDoesNotExist("USER DOESN'T EXIST WITH ID = " + user.getId()));
        currentUser.setAddress(user.getAddress());
        currentUser.setFullName(user.getFullName());
        currentUser.setPhone(user.getPhone());
        Set<Role> roles = user.getRoles().stream()
                            .map(role -> roleService.findRoleByName(role.getName()))
                            .collect(Collectors.toSet());
        currentUser.setRoles(roles);
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

}
