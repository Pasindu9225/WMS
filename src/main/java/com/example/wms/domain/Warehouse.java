package com.example.wms.domain;

import com.example.wms.tenant.TenantContext;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "warehouses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse extends BaseEntity {

    // Note: The 'id' is already inherited from BaseEntity.
    // If your BaseEntity doesn't have an ID, keep this one.

    @Column(nullable = false)
    private String name;

    private String location;

    /**
     * Requirement 15: Automatically assign Tenant IDs before saving to database.
     * This prevents the "null constraint" 500 error.
     */
    @PrePersist
    public void onPrePersist() {
        if (this.getGroupId() == null) {
            this.setGroupId(TenantContext.getGroupId());
        }
        if (this.getCompanyId() == null) {
            this.setCompanyId(TenantContext.getCompanyId());
        }
    }
}