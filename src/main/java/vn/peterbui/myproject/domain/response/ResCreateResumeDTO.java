package vn.peterbui.myproject.domain.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper=false)
public class ResCreateResumeDTO {
    private long id;
    private Instant createdAt;
    private String createdBy;
}
