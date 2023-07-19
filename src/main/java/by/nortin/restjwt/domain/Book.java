package by.nortin.restjwt.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books")
@NoArgsConstructor
@Getter
@Setter
public class Book extends BaseEntity {

    private String title;
    private String author;
}
