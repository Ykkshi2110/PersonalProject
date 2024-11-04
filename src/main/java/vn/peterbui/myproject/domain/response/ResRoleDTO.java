package vn.peterbui.myproject.domain.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ResRoleDTO {
    private long id;
    private String name;
}
