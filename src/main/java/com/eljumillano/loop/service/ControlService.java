package com.eljumillano.loop.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eljumillano.loop.dtos.control.ControlDto;
import com.eljumillano.loop.exception.ResourceNotFoundException;
import com.eljumillano.loop.mapper.ControlMapper;
import com.eljumillano.loop.repository.ControlRepository;
import com.eljumillano.loop.service.iservice.IControlService;

@Service
public class ControlService implements IControlService {

@Autowired
private ControlMapper controlMapper;

@Autowired
private ControlRepository controlRepository;


    @Override
    public List<ControlDto> getAllControls() {
        return controlRepository.findAll()
                .stream()
                .map(controlMapper::toDto)
                .toList();
    }

    @Override
    public ControlDto getControlById(Long id) {
        return controlRepository.findById(id)
                .map(controlMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Control", id));
    }

    @Override
    public ControlDto createControl(ControlDto control) {
        var entity = controlMapper.toEntity(control);
        return controlMapper.toDto(controlRepository.save(entity));
    }

    @Override
    public ControlDto updateControl(Long id, ControlDto control) {
        var existingControl = controlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Control", id));
        controlMapper.updateEntity(control, existingControl);
        return controlMapper.toDto(controlRepository.save(existingControl));
    }

    @Override
    public void deleteControl(Long id) {
        var existingControl = controlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Control", id));
        controlRepository.delete(existingControl);
    }

 
}
