package vn.peterbui.myproject.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import vn.peterbui.myproject.domain.Permission;
import vn.peterbui.myproject.domain.Role;
import vn.peterbui.myproject.domain.User;
import vn.peterbui.myproject.repository.PermissionRepository;
import vn.peterbui.myproject.repository.RoleRepository;
import vn.peterbui.myproject.repository.UserRepository;
import vn.peterbui.myproject.type.GenderEnum;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Override
    public void run(String... args) throws Exception {
        long countRecordPermission = this.permissionRepository.count();
        long countRecordRole = this.roleRepository.count();
        long countRecordUser = this.userRepository.count();

        if (countRecordPermission == 0) {
            ArrayList<Permission> arr = new ArrayList<>(); 
            arr.add(new Permission("Create a permission", "/api/v1/permissions", "POST", "PERMISSIONS"));
            arr.add(new Permission("Update a permission", "/api/v1/permissions", "PUT", "PERMISSIONS"));
            arr.add(new Permission("Delete a permission", "/api/v1/permissions/{id}", "DELETE", "PERMISSIONS"));
            arr.add(new Permission("Get a permission by id", "/api/v1/permissions/{id}", "GET", "PERMISSIONS"));
            arr.add(new Permission("Get permissions with pagination", "/api/v1/permissions", "GET", "PERMISSIONS"));

            arr.add(new Permission("Create a role", "/api/v1/roles", "POST", "ROLES"));
            arr.add(new Permission("Update a role", "/api/v1/roles", "PUT", "ROLES"));
            arr.add(new Permission("Delete a role", "/api/v1/roles/{id}", "DELETE", "ROLES"));
            arr.add(new Permission("Get a role by id", "/api/v1/roles/{id}", "GET", "ROLES"));
            arr.add(new Permission("Get roles with pagination", "/api/v1/roles", "GET", "ROLES"));

            arr.add(new Permission("Create a user", "/api/v1/users", "POST", "USERS"));
            arr.add(new Permission("Update a user", "/api/v1/users", "PUT", "USERS"));
            arr.add(new Permission("Delete a user", "/api/v1/users/{id}", "DELETE", "USERS"));
            arr.add(new Permission("Get a user by id", "/api/v1/users/{id}", "GET", "USERS"));
            arr.add(new Permission("Get users with pagination", "/api/v1/users", "GET", "USERS"));

            this.permissionRepository.saveAll(arr);
        }

        if (countRecordRole == 0) {
            List<Permission> permissions = this.permissionRepository.findAll();
            // role
            Role adminRole = new Role();
            adminRole.setName("SUPER_ADMIN");
            adminRole.setDescription("ADMIN FULL PERMISSION");
            adminRole.setActive(true);
            adminRole.setPermissions(permissions);
            this.roleRepository.save(adminRole);
        }

        if (countRecordUser == 0) {
            Role adminRole = this.roleRepository.findByName("SUPER_ADMIN");
            // user
            User user = new User();
            user.setEmail("PeterBui@gmail.com");
            user.setAddress("179/58/16 Lê Đình Thám");
            user.setFullName("Peter Bùi Đz number1");
            user.setAge(20);
            user.setGender(GenderEnum.MALE);
            user.setPassword(passwordEncoder.encode("Buianhquoc2110@"));
            user.setRole(adminRole);
            this.userRepository.save(user);
        }

        if (countRecordPermission > 0 && countRecordRole > 0 && countRecordUser > 0) {
            logger.info("Skip initialize Data");
        } else {
            logger.info("Data Initialized!");
        }

    }

}
