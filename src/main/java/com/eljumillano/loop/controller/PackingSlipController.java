package com.eljumillano.loop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eljumillano.loop.dtos.packingslip.PackingSlipDto;
import com.eljumillano.loop.service.iservice.IPackingSlipService;

@RestController
@RequestMapping("/loop/api/packing-slips")
public class PackingSlipController {

    @Autowired
    private IPackingSlipService packingSlipService;

    @GetMapping("/{id}")
    public ResponseEntity<PackingSlipDto> getPackingSlipById(@PathVariable Long id) {
        return ResponseEntity.ok(packingSlipService.getPackingSlipById(id));
    }

    @GetMapping("/number/{slipNumber}")
    public ResponseEntity<PackingSlipDto> getPackingSlipByNumber(@PathVariable String slipNumber) {
        return ResponseEntity.ok(packingSlipService.getPackingSlipByNumber(slipNumber));
    }

    @GetMapping("/delivery/{deliveryId}")
    public ResponseEntity<List<PackingSlipDto>> getPackingSlipsByDelivery(@PathVariable Long deliveryId) {
        return ResponseEntity.ok(packingSlipService.getPackingSlipsByDeliveryId(deliveryId));
    }

    @GetMapping("/control/{controlId}")
    public ResponseEntity<List<PackingSlipDto>> getPackingSlipsByControl(@PathVariable Long controlId) {
        return ResponseEntity.ok(packingSlipService.getPackingSlipsByControlId(controlId));
    }

    @PostMapping
    public ResponseEntity<PackingSlipDto> createPackingSlip(
            @RequestParam Long controlId, 
            @RequestParam(required = false) Long orderId) {
        PackingSlipDto packingSlip;
        if (orderId != null) {
            packingSlip = packingSlipService.createPackingSlipFromControl(controlId, orderId);
        } else {
            packingSlip = packingSlipService.createPackingSlipFromControl(controlId);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(packingSlip);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPackingSlipPdf(@PathVariable Long id) {
        byte[] pdfContent = packingSlipService.generatePackingSlipPdf(id);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "packing-slip-" + id + ".pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfContent);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelPackingSlip(@PathVariable Long id) {
        packingSlipService.cancelPackingSlip(id);
        return ResponseEntity.noContent().build();
    }
}
