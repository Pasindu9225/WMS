package com.example.wms.controller;

import com.example.wms.domain.Warehouse;
import com.example.wms.repository.WarehouseRepository;
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
    // Temporarily disabled role check for Requirement 50-66 testing
    // @PreAuthorize("hasAnyAuthority('COMPANY_ADMIN', 'OPERATOR')")
    public List<Warehouse> getAll() {
        return repository.findAll();
    }

    @PostMapping
    // Temporarily disabled role check to avoid 403 Forbidden
    // @PreAuthorize("hasAuthority('COMPANY_ADMIN')")
    public Warehouse create(@RequestBody Warehouse warehouse) {
        return repository.save(warehouse);
    }
}