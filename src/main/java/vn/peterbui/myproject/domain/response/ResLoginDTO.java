package vn.peterbui.myproject.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import vn.peterbui.myproject.domain.Role;

@Data
@EqualsAndHashCode(callSuper = false)
public class ResLoginDTO {

    @JsonProperty("access_token")
    private String accessToken;
    private UserLogin user;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserLogin {
        private long id;
        private String email;
        private String name;
        private Role role;
    }

    // Trả về khi Get Account
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserGetAccount {
        private UserLogin user;
    }

    // Lưu trữ lại thông tin bên trong Token 
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInsideToken {
        private long id;
        private String email;
        private String name;
    }
}
