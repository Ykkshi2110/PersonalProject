package vn.peterbui.myproject.domain.response;

import lombok.*;
import vn.peterbui.myproject.type.GenderEnum;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = false)
public class ResUserDTO {
    private long id;
    private String email;
    private String name;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant updatedAt;
    private Instant createdAt;
    private CompanyUser company;
    private ResRoleDTO role;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompanyUser {
        private long id;
        private String name;
    }
}
