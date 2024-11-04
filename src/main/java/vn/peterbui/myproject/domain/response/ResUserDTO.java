package vn.peterbui.myproject.domain.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.peterbui.myproject.type.GenderEnum;

@Data
@EqualsAndHashCode(callSuper = false)
public class ResUserDTO {
    private long id;
    private String address;
    private String avatar;
    private String email;
    private String fullName;
    private int age;
    private GenderEnum gender;
    private ResRoleDTO role;
}
