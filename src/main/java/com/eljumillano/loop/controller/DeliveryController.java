package com.eljumillano.loop.controller;

import com.eljumillano.loop.dtos.delivery.StockResponseDTO;
import com.eljumillano.loop.service.DeliveryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }


    @GetMapping("/{idDelivery}/stock")
    public ResponseEntity<StockResponseDTO> getStock(@PathVariable Long idDelivery) {
        ResponseEntity<StockResponseDTO> response;
        try {
            StockResponseDTO stock = deliveryService.obtenerStockPorReparto(idDelivery);
            response = ResponseEntity.ok(stock);
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }


    @GetMapping("/{idDelivery}/has-stock")
    public ResponseEntity<Boolean> hasStock(@PathVariable Long idDelivery) {
        boolean hasStock = deliveryService.tieneStockDisponible(idDelivery);
        return ResponseEntity.ok(hasStock);
    }
}
