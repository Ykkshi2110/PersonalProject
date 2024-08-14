package vn.peterbui.myproject.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import jakarta.validation.Valid;
import vn.peterbui.myproject.domain.Role;
import vn.peterbui.myproject.domain.User;
import vn.peterbui.myproject.domain.dto.CreateUserRequest;
import vn.peterbui.myproject.exception.UserDoesNotExist;
import vn.peterbui.myproject.repository.UserRepository;
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public List<User> getAllUser() {
        return this.userRepository.findAll();
    }

    public User getUserById(long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new UserDoesNotExist("USER DOESN'T EXIST WITH ID = " + id));
    }

    public String handleCreateUser(@Valid CreateUserRequest createUserRequest) {
        User user = new User();
        user.setAddress(createUserRequest.getAddress());
        user.setAvatar(createUserRequest.getAvatar());
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(createUserRequest.getPassword());
        user.setFullName(createUserRequest.getFullName());
        user.setPhone(createUserRequest.getPhone());
        Set<Role> roles = createUserRequest.getRoleType().stream()
                .map(roleService::findRoleByName)
                .collect(Collectors.toSet());
        user.setRoles(roles);
        this.userRepository.save(user);
        return "CREATE USER SUCCESSFULLY";
    }

    public String handleUpdateUser(User user) {
        User currentUser = this.userRepository.findById(user.getId())
                .orElseThrow(() -> new UserDoesNotExist("USER DOESN'T EXIST WITH ID = " + user.getId()));
        currentUser.setAddress(user.getAddress());
        currentUser.setFullName(user.getFullName());
        currentUser.setPhone(user.getPhone());
        Set<Role> roles = user.getRoles().stream()
                            .map(role -> roleService.findRoleByName(role.getName()))
                            .collect(Collectors.toSet());
        currentUser.setRoles(roles);
        this.userRepository.save(currentUser);
        return "UPDATE USER SUCCESSFULLY";
    }

    public String handleDeleteUser(long id) {
        User currentUser = this.userRepository.findById(id).orElse(null);
        if (currentUser == null) {
            throw new UserDoesNotExist("USER DOES NOT EXIST!!!");
        } else {
            this.userRepository.delete(currentUser);
            return "DELETE USER SUCCESSFULLY";
        }
    }

}
