package vn.peterbui.myproject.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RoleDTO {
    private long id;
    private String name;
}
