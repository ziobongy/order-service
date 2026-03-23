package it.adesso.management.ordermanagementservice.entities.external;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "composed_pizza", schema = "external")
public class ComposedPizza {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base")
    private Basis base;

    @OneToMany(mappedBy = "pizza")
    private Set<PizzaIngredient>  ingredients;

}