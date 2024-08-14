package vn.peterbui.myproject.domain.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.peterbui.myproject.type.RoleType;

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
    private Set<RoleType> roleType;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "CreateUserRequest [address=" + address + ", avatar=" + avatar + ", email=" + email + ", fullName="
                + fullName + ", password=" + password + ", phone=" + phone + ", roles=" + roleType + "]";
    }

    public Set<RoleType> getRoleType() {
        return roleType;
    }

    public void setRoleType(Set<RoleType> roleType) {
        this.roleType = roleType;
    }

    

}
