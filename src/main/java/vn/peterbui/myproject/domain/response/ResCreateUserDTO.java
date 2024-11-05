package vn.peterbui.myproject.domain.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import vn.peterbui.myproject.domain.Role;
import vn.peterbui.myproject.type.GenderEnum;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = false)
public class ResCreateUserDTO {
    private String address;
    private String email;
    private String name;
    private int age;
    private GenderEnum gender;
    private Instant createdAt;
    private CompanyUser company;

    @Getter
    @Setter
    public static class CompanyUser {
        private long id;
        private String name;
    }
}
