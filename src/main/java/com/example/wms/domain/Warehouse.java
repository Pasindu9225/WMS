package com.example.wms.domain;

import com.example.wms.tenant.TenantContext;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "warehouses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Requirement: Implement soft delete logic
@SQLDelete(sql = "UPDATE warehouses SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Warehouse extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private String location;

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