package by.nortin.restjwt.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Book extends BaseEntity implements Serializable {

    private String title;
    private String author;
}
