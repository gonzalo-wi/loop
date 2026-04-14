package com.eljumillano.loop.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eljumillano.loop.dtos.disposable.DisposableDto;
import com.eljumillano.loop.service.iservice.IDisposableService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/loop/api/disposables")
public class DisposableController {

    private final IDisposableService disposableService;

    public DisposableController(IDisposableService disposableService) {
        this.disposableService = disposableService;
    }

    @GetMapping
    public ResponseEntity<List<DisposableDto>> getAll() {
        return ResponseEntity.ok(disposableService.getAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<DisposableDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(disposableService.getById(id));
    }


    @PostMapping
    public ResponseEntity<DisposableDto> create(@Valid @RequestBody DisposableDto disposableDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(disposableService.create(disposableDto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<DisposableDto> update(@PathVariable Long id, @Valid @RequestBody DisposableDto disposableDto) {
        return ResponseEntity.ok(disposableService.update(id, disposableDto));
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        disposableService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
