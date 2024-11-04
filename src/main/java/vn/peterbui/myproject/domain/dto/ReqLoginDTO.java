package vn.peterbui.myproject.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReqLoginDTO {

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    private String password;
}
