package vn.peterbui.myproject.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.validation.Valid;
import vn.peterbui.myproject.domain.Role;
import vn.peterbui.myproject.domain.User;
import vn.peterbui.myproject.domain.dto.CreateUserRequest;
import vn.peterbui.myproject.exception.UserDoesNotExist;
import vn.peterbui.myproject.exception.UserExistedException;
import vn.peterbui.myproject.repository.UserRepository;
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUser() {
        return this.userRepository.findAll();
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

}
