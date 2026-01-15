package com.aws_practice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    /*
     *
     * @JsonIgnore avoid circular reference :
     *       a Category would include its products, and each Product would include its Category, creating an infinite loop.
     *
     * The mappedBy attribute tells JPA that the products collection in Category is mapped by the category field in the Product entity.
     *
     * */
    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Product> products;

    public Category(String name) {
        this.name = name;
    }
}
