package org.launchcode.models;

import javax.annotation.Generated;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {

    @Id
    @GeneratedValue
    private int id;

    @OneToMany
    @JoinColumn(name = "category_id")
    private List<Cheese> cheeses = new ArrayList<>();

    @NotNull
    @Size(min=3, max=15)
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public Category() { }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getId() { return id; }
}