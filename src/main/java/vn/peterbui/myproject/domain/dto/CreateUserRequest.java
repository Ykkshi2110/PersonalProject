package vn.peterbui.myproject.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.peterbui.myproject.domain.Role;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateUserRequest {
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
    private String phone;

    private Role role;
   
}
