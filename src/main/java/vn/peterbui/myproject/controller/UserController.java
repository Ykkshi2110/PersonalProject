package vn.peterbui.myproject.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import vn.peterbui.myproject.convert.ConvertUtils;
import vn.peterbui.myproject.convert.annotation.ApiMessage;
import vn.peterbui.myproject.domain.ApiResponse;
import vn.peterbui.myproject.domain.User;
import vn.peterbui.myproject.domain.request.ReqCreateUser;
import vn.peterbui.myproject.domain.response.ResultPaginationDTO;
import vn.peterbui.myproject.domain.response.ResUserDTO;
import vn.peterbui.myproject.service.UserService;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final ConvertUtils convertUtils;

    public UserController(UserService userService, ConvertUtils convertUtils) {
        this.userService = userService;
        this.convertUtils = convertUtils;
    }

    @GetMapping("/users")
    @ApiMessage("fetch all user")
    public ResponseEntity<ResultPaginationDTO> getUser(@Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAllUser(spec, pageable));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable long id) {
        User user = this.userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(convertUtils.convertToDto(user));
    }

    @PostMapping("/users/create")
    public ResponseEntity<ResUserDTO> handleCreateUser(@RequestBody @Valid ReqCreateUser reqCreateUser) {
        ResUserDTO resUserDTO = convertUtils.convertToDto(this.userService.handleCreateUser(reqCreateUser));
        return ResponseEntity.status(HttpStatus.CREATED).body(resUserDTO);
    }

    @PutMapping("/users/update")
    public ResponseEntity<ResUserDTO> handeUpdateUser(@RequestBody User user) {
        ResUserDTO resUserDTO = convertUtils.convertToDto(this.userService.handleUpdateUser(user));
        return ResponseEntity.status(HttpStatus.CREATED).body(resUserDTO);
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<ApiResponse<Object>> handleDeleteUser(@PathVariable long id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
