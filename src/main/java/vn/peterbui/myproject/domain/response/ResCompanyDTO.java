package vn.peterbui.myproject.domain.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ResCompanyDTO {
    private String name;
    private String description;
    private String address;
    private String logo;
}
