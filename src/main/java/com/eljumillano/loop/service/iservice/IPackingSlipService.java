package com.eljumillano.loop.service.iservice;

import java.util.List;

import com.eljumillano.loop.dtos.packingslip.PackingSlipDto;

public interface IPackingSlipService {
    
    PackingSlipDto getPackingSlipById(Long id);
    
    PackingSlipDto getPackingSlipByNumber(String slipNumber);
    
    List<PackingSlipDto> getPackingSlipsByDeliveryId(Long deliveryId);
    
    List<PackingSlipDto> getPackingSlipsByControlId(Long controlId);
    
    PackingSlipDto createPackingSlipFromControl(Long controlId, Long orderId);
    
    PackingSlipDto createPackingSlipFromControl(Long controlId);  // Sin orden asociada
    
    byte[] generatePackingSlipPdf(Long packingSlipId);
    
    void cancelPackingSlip(Long id);
}
