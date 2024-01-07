package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.account.client.model.request.CreateClientDTO;
import com.example.shoestore.core.account.client.model.request.DeleteClienDTO;
import com.example.shoestore.core.account.client.model.request.UpdateClientDTO;
import com.example.shoestore.core.account.client.service.AdminClientService;
import com.example.shoestore.core.common.dto.response.ResponseFactory;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/client/admin")
public class AdminClientController {

    @Autowired
    private AdminClientService clientService;

    @GetMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> page(@RequestParam(name = "page", defaultValue = "0") int page,
                                       @RequestParam(name = "size", defaultValue = "5") int size,
                                       @RequestParam(name = "sortField", required = false) String sortField,
                                       @RequestParam(name = "sortOrder", required = false) String sortOrder,
                                       @RequestParam(name = "input", required = false) String input,
                                       @RequestParam(name = "createdTime", required = false) String createdTime,
                                       @RequestParam(name = "dateOfBirth", required = false) String dateOfBirth,
                                       @RequestParam(name = "status", required = false) Integer status,
                                       @RequestParam(name = "gender", required = false) Boolean gender) {
        return ResponseEntity.ok(clientService.pageClient(input, createdTime, gender, dateOfBirth, status, page, size, sortField, sortOrder));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> create(@Valid @RequestBody CreateClientDTO clientDTO) {
        return ResponseFactory.data(clientService.create(clientDTO));
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> update(@Valid @RequestBody UpdateClientDTO clientDTO) {
        return ResponseFactory.data(clientService.update(clientDTO));
    }

    @PatchMapping("/delete")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> delete(@RequestBody DeleteClienDTO deleteClientDTO) {
        return ResponseFactory.data(clientService.delete(deleteClientDTO));
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> detail(@PathVariable("id") Long id) {
        return ResponseFactory.data(clientService.detail(id));
    }

    @PutMapping("/update-status/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> updateStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        return ResponseFactory.data(clientService.updateStatus(status, id));
    }


}
