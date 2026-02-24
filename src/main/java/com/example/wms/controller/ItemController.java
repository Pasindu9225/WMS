package com.example.wms.controller;

import com.example.wms.domain.Item;
import com.example.wms.domain.Warehouse;
import com.example.wms.repository.ItemRepository;
import com.example.wms.repository.WarehouseRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemRepository repository;
    private final WarehouseRepository warehouseRepository;

    public ItemController(ItemRepository repository, WarehouseRepository warehouseRepository) {
        this.repository = repository;
        this.warehouseRepository = warehouseRepository;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_COMPANY_ADMIN')")
    public Item create(@RequestBody Item item) {
        if (item.getWarehouse() == null || item.getWarehouse().getId() == null) {
            throw new RuntimeException("Warehouse ID must be provided");
        }

        Warehouse warehouse = warehouseRepository.findById(item.getWarehouse().getId())
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + item.getWarehouse().getId()));

        item.setWarehouse(warehouse);

        return repository.save(item);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY_ADMIN', 'ROLE_OPERATOR')")
    public List<Item> getAll() {
        return repository.findAll();
    }
}