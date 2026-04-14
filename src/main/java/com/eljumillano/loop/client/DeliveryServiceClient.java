package com.eljumillano.loop.client;

import com.eljumillano.loop.config.FeignClientConfig;
import com.eljumillano.loop.dtos.delivery.StockResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(
    name = "delivery-service",
    url = "${delivery.service.url}",
    configuration = FeignClientConfig.class
)
public interface DeliveryServiceClient {

    @GetMapping("/jmobile/service/reparto/{idDelivery}/stock")
    StockResponseDTO getStockByDelivery(@PathVariable("idDelivery") Long idDelivery);
}
