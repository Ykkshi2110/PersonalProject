package vn.peterbui.myproject.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.peterbui.myproject.type.GenderEnum;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserDTO {
    private long id;
    private String address;
    private String avatar;
    private String email;
    private String fullName;
    private int age;
    private GenderEnum gender;
    private RoleDTO role;
}
