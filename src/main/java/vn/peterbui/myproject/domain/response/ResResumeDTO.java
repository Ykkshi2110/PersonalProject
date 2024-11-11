package vn.peterbui.myproject.domain.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.peterbui.myproject.type.StatusEnum;
import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = false)
public class ResResumeDTO {
    private long id;
    private String email;
    private String url;
    private StatusEnum status;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;

    private String companyName;
    private UserResume user;
    private JobResume job;

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class UserResume {
        private long id;
        private String name;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class JobResume {
        private long id;
        private String name;
    }
}
