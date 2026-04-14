package com.eljumillano.loop.service.iservice;
import java.time.LocalDate;
import java.util.List;
import com.eljumillano.loop.dtos.control.ControlDto;

public interface IControlService {
    List<ControlDto> getAllControls();
    ControlDto getControlById(Long id);
    ControlDto createControl(ControlDto control);
    ControlDto updateControl(Long id, ControlDto control);
    void deleteControl(Long id);
    
    void setGlobalDeliveryDate(LocalDate date);
    LocalDate getGlobalDeliveryDate();
}

