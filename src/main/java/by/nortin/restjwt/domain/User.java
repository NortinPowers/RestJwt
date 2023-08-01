package by.nortin.restjwt.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements Serializable {

    @Column(name = "user_name")
    private String userName;
    private String password;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
