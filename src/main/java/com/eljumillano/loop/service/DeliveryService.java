package com.eljumillano.loop.service;

import com.eljumillano.loop.client.DeliveryServiceClient;
import com.eljumillano.loop.dtos.delivery.StockResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Servicio que maneja la lógica de negocio para consultas al servicio de reparto externo
 */
@Service
public class DeliveryService {

    private static final Logger log = LoggerFactory.getLogger(DeliveryService.class);
    private static final String MSG_CONSULTANDO_STOCK     = "Consultando stock para reparto ID: {}";
    private static final String MSG_STOCK_OBTENIDO        = "Stock obtenido exitosamente. Cantidad total: {}";
    private static final String MSG_ERROR_OBTENER_STOCK   = "Error al obtener stock para reparto {}: {}";
    private static final String MSG_ERROR_VERIFICAR_STOCK = "Error al verificar stock para reparto {}";
    private static final String ERROR_CONSULTAR_STOCK     = "Error al consultar el stock del reparto";
    private final DeliveryServiceClient deliveryServiceClient;

    
    public DeliveryService(DeliveryServiceClient deliveryServiceClient) {
        this.deliveryServiceClient = deliveryServiceClient;
    }

    public StockResponseDTO obtenerStockPorReparto(Long idDelivery) {
        try {
            log.info(MSG_CONSULTANDO_STOCK, idDelivery);
            StockResponseDTO response = deliveryServiceClient.getStockByDelivery(idDelivery);
            log.info(MSG_STOCK_OBTENIDO, response.getCantidad());
            return response;
        } catch (Exception e) {
            log.error(MSG_ERROR_OBTENER_STOCK, idDelivery, e.getMessage(), e);
            throw new RuntimeException(ERROR_CONSULTAR_STOCK, e);
        }
    }

    public boolean tieneStockDisponible(Long idDelivery) {
        try {
            StockResponseDTO response = obtenerStockPorReparto(idDelivery);
            return response.getData() != null && !response.getData().isEmpty();
        } catch (Exception e) {
            log.error(MSG_ERROR_VERIFICAR_STOCK, idDelivery, e);
            return false;
        }
    }
}
