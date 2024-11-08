package vn.peterbui.myproject.domain.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper=false)
public class ResUpdateResumeDTO {
    private long id;
    private Instant updatedAt;
    private String updatedBy;
}
