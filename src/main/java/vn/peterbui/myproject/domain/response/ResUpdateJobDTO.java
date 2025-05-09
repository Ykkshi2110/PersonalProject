package vn.peterbui.myproject.domain.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.peterbui.myproject.type.LevelEnum;

import java.time.Instant;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class ResUpdateJobDTO {
    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;
    private Instant startDate;
    private Instant endDate;
    private boolean active;
    private List<String> skills;
    private Instant updatedAt;
    private String updatedBy;
}
