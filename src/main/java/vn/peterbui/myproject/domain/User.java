package vn.peterbui.myproject.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;   
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.peterbui.myproject.convert.SecurityUtil;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor 
@EqualsAndHashCode(callSuper = false)
@Table(name = "users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @NotNull
    private String address;
    private String avatar;

    @NotNull
    @Email(message = "Invalid Email", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
    private String fullName;

    @NotNull
    @Size(min = 6, message = "Password must be at least 6 character")
    private String password;
    private String phone;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    private Instant createdAt;
    private Instant updateAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

     @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        this.createdAt = Instant.now();
    }

     @PreUpdate
    public void handleBeforeUpdate(){
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        this.updateAt = Instant.now();
    }

}
