package com.example.wms.repository;

import com.example.wms.domain.Warehouse;
import com.example.wms.tenant.TenantContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional // Requirement 54
class WarehouseRepositoryTest {

    @Autowired
    private WarehouseRepository repository;

    @Test // Requirement 58
    void shouldNotReturnOtherCompanyData() {
        // 1. Create Data for Company A
        Warehouse whA = new Warehouse();
        whA.setName("WH-A");
        whA.setGroupId("GROUP_A");
        whA.setCompanyId("COMPANY_A1");
        repository.save(whA);

        // 2. Create Data for Company B
        Warehouse whB = new Warehouse();
        whB.setName("WH-B");
        whB.setGroupId("GROUP_A");
        whB.setCompanyId("COMPANY_B1");
        repository.save(whB);

        // 3. Simulate Tenant A1 Context (Requirement 61)
        TenantContext.setCompanyId("COMPANY_A1");

        // 4. Fetch and Validate (Requirement 62, 63)
        List<Warehouse> result = repository.findAll();

        // Assert that only Company A1 data is returned
        assertTrue(result.stream()
                        .allMatch(w -> w.getCompanyId().equals("COMPANY_A1")),
                "Isolation Failed: Found data from another company!");

        TenantContext.clear(); // Requirement 65
    }
}