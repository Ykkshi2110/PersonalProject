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
import vn.peterbui.myproject.domain.User;
import vn.peterbui.myproject.domain.response.ResCreateUserDTO;
import vn.peterbui.myproject.domain.response.ApiResponse;
import vn.peterbui.myproject.domain.response.ResUpdateUserDTO;
import vn.peterbui.myproject.domain.response.ResUserDTO;
import vn.peterbui.myproject.domain.response.ResultPaginationDTO;
import vn.peterbui.myproject.service.UserService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public ResUserDTO convertToResUserDTO(User user) {
        return modelMapper.map(user, ResUserDTO.class);
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        return modelMapper.map(user, ResUpdateUserDTO.class);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        return modelMapper.map(user, ResCreateUserDTO.class);
    }

    @GetMapping("/users")
    @ApiMessage("fetch all user")
    public ResponseEntity<ResultPaginationDTO> getUser(@Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAllUser(spec, pageable));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable long id) {
        User user = this.userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(this.convertToResUserDTO(user));
    }

    @PostMapping("/users")
    public ResponseEntity<ResCreateUserDTO> handleCreateUser(@RequestBody @Valid User reqCreateUser) {
        ResCreateUserDTO resCreateUserDTO = this.convertToResCreateUserDTO(userService.handleCreateUser(reqCreateUser));
        return ResponseEntity.status(HttpStatus.CREATED).body(resCreateUserDTO);
    }

    @PutMapping("/users")
    public ResponseEntity<ResUpdateUserDTO> handeUpdateUser(@RequestBody User user) {
        ResUpdateUserDTO resUpdateUserDTO = this.convertToResUpdateUserDTO(this.userService.handleUpdateUser(user));
        return ResponseEntity.status(HttpStatus.CREATED).body(resUpdateUserDTO);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Object>> handleDeleteUser(@PathVariable long id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
