package vn.peterbui.myproject.domain.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.peterbui.myproject.domain.Role;
import vn.peterbui.myproject.type.GenderEnum;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReqCreateUser {
    @NotNull
    private String address;
    private String avatar;

    @NotNull
    @Email(message = "Invalid Email", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
    private String fullName;

    @NotNull
    @Size(min = 6, message = "Password must be at least 6 character")
    private String password;
    private int age;
    private GenderEnum gender;

    private Role role;
   
}