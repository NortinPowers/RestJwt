package by.nortin.restjwt.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity implements Serializable {

    private String name;
    @OneToMany(mappedBy = "role")
    private List<User> users;
}
