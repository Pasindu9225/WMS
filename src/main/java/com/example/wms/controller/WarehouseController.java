package com.example.wms.controller;

import com.example.wms.domain.Warehouse;
import com.example.wms.repository.WarehouseRepository;
import org.springframework.security.access.prepost.PreAuthorize;
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
    // Use hasAnyAuthority for exact string match with database
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY_ADMIN', 'ROLE_OPERATOR')")
    public List<Warehouse> getAll() {
        return repository.findAll();
    }

    @PostMapping
    // Use hasAuthority for exact match with database role
    @PreAuthorize("hasAuthority('ROLE_COMPANY_ADMIN')")
    public Warehouse create(@RequestBody Warehouse warehouse) {
        return repository.save(warehouse);
    }
}