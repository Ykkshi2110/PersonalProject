package vn.peterbui.myproject.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.peterbui.myproject.convert.ConvertUtils;
import vn.peterbui.myproject.domain.User;
import vn.peterbui.myproject.domain.dto.CreateUserRequest;
import vn.peterbui.myproject.domain.dto.UserDTO;
import vn.peterbui.myproject.service.UserService;

@RestController
@RequestMapping("/api/test")
public class UserController {
    private final UserService userService;
    private final ConvertUtils convertUtils;
    
    public UserController (UserService userService, ConvertUtils convertUtils) {
        this.userService = userService;
        this.convertUtils = convertUtils;
    }

    @GetMapping("/user")
    public List<UserDTO> getUser (){
        return this.userService.getAllUser()
                .stream()
                .map(convertUtils::convertToDto)
                .toList();
    }

    @GetMapping("/user/{id}") 
    public UserDTO getUserById(@PathVariable long id) {
       User user = this.userService.getUserById(id);
       return convertUtils.convertToDto(user);
    }

    @PostMapping("/user/create/abc")
    public String handleCreateUser (@RequestBody CreateUserRequest createUserRequest) {
        return this.userService.handleCreateUser(createUserRequest);
    }

    @PutMapping("/user/update")
    public String handeUpdateUser (@RequestBody User user) {
        return this.userService.handleUpdateUser(user);
    }

    @DeleteMapping("/user/delete/{id}") 
    public String handleDeleteUser (@PathVariable long id){
        return this.userService.handleDeleteUser(id);
    }

    
}
