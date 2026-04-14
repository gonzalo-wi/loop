package com.eljumillano.loop.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.eljumillano.loop.dtos.control.ControlDto;
import com.eljumillano.loop.service.iservice.IControlService;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/loop/api/controls")
public class ControlController {
    
    @Autowired
    private IControlService controlService;

    @GetMapping
    public ResponseEntity<List<ControlDto>> getAllControls() {
        return ResponseEntity.ok(controlService.getAllControls());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ControlDto> getControlById(@PathVariable Long id) {
        return ResponseEntity.ok(controlService.getControlById(id));
    }


    @PostMapping
    public ResponseEntity<ControlDto> createControl(@Valid @RequestBody ControlDto control) {
        return ResponseEntity.ok(controlService.createControl(control));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ControlDto> updateControl(@PathVariable Long id, @Valid @RequestBody ControlDto control) {
        return ResponseEntity.ok(controlService.updateControl(id, control));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteControl(@PathVariable Long id) {
        controlService.deleteControl(id);
        return ResponseEntity.noContent().build();
    }
}
