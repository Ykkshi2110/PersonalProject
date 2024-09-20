package vn.peterbui.myproject.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserDTO {
    private long id;
    private String address;
    private String avatar;
    private String email;
    private String fullName;
    private String phone;
    private RoleDTO role;
}
