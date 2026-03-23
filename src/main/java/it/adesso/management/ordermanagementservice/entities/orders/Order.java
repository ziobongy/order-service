package it.adesso.management.ordermanagementservice.entities.orders;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.Set;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "external_id", nullable = false)
    private Long externalId;

    @NotNull
    @Column(name = "placed_by", nullable = true)
    private String placedBy;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "charged_by", nullable = true)
    private String chargedBy;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private Set<OrderEntry> entries;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

}