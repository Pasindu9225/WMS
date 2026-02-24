package com.example.wms.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Item extends BaseEntity {

    @Column(nullable = false)
    private String itemCode; // Requirement: Unique item codes per tenant

    private String description;

    private Double quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse; // Links the item to a specific warehouse
}