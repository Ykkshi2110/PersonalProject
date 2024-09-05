package vn.peterbui.myproject.controller;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import vn.peterbui.myproject.convert.ConvertUtils;
import vn.peterbui.myproject.domain.ApiResponse;
import vn.peterbui.myproject.domain.User;
import vn.peterbui.myproject.domain.dto.CreateUserRequest;
import vn.peterbui.myproject.domain.dto.ResultPaginationDTO;
import vn.peterbui.myproject.domain.dto.UserDTO;
import vn.peterbui.myproject.service.UserService;

@RestController
@RequestMapping("/api/test")
public class UserController {
    private final UserService userService;
    private final ConvertUtils convertUtils;

    public UserController(UserService userService, ConvertUtils convertUtils) {
        this.userService = userService;
        this.convertUtils = convertUtils;
    }

    @GetMapping("/users")
    public ResponseEntity<ResultPaginationDTO> getUser(@RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional) {
        String sCurrent = currentOptional.isPresent()  ? currentOptional.get() : "";
        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";

        int current = Integer.parseInt(sCurrent);
        int pageSize = Integer.parseInt(sPageSize);

        // Phải truyền tham số vào là current - 1 vì trang đầu tiên thư viện mặc định bằng 0
        Pageable pageable = PageRequest.of( current - 1, pageSize);
        
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAllUser(pageable));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable long id) {
        User user = this.userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(convertUtils.convertToDto(user));
    }

    @PostMapping("/users/create")
    public ResponseEntity<UserDTO> handleCreateUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        UserDTO userDTO = convertUtils.convertToDto(this.userService.handleCreateUser(createUserRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PutMapping("/users/update")
    public ResponseEntity<UserDTO> handeUpdateUser(@RequestBody User user) {
        UserDTO userDTO = convertUtils.convertToDto(this.userService.handleUpdateUser(user));
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<ApiResponse<Object>> handleDeleteUser(@PathVariable long id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
