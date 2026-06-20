package sn.fouadi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder //instancie un objet
@NamedQuery(name = "Category.All",query = "SELECT c FROM Category c ORDER BY c.name")
public class Category extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private boolean state;

    @OneToMany(mappedBy = "category")
    private List<Book> books;

}


