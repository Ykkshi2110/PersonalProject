package vn.peterbui.myproject.domain.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.peterbui.myproject.type.LevelEnum;

import java.time.Instant;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ResJobDTO {
    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;
    private String description;
    private Instant startDate;
    private Instant endDate;
    private boolean active;
    private List<String> skills;
    private Instant createdAt;
    private Instant updateAdt;
    private String createdBy;
    private String updatedBy;
}
