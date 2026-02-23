package com.example.wms.controller;

import com.example.wms.domain.Warehouse;
import com.example.wms.repository.WarehouseRepository;
import org.springframework.security.access.prepost.PreAuthorize; // Required for RBAC
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseRepository repository;

    public WarehouseController(WarehouseRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_COMPANY_ADMIN', 'ROLE_OPERATOR')")
    public List<Warehouse> getAll() {
        // The TenantAspect automatically applies the Hibernate filter.
        return repository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public Warehouse create(@RequestBody Warehouse warehouse) {
        return repository.save(warehouse);
    }
}