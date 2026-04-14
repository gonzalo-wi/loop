package com.eljumillano.loop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eljumillano.loop.dtos.disposable.DisposableDto;
import com.eljumillano.loop.exception.ResourceNotFoundException;
import com.eljumillano.loop.mapper.DisposableMapper;
import com.eljumillano.loop.model.Disposable;
import com.eljumillano.loop.repository.DisposableRepository;
import com.eljumillano.loop.service.iservice.IDisposableService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DisposableService implements IDisposableService {

    private static final String MESSAGE_ENTITY_NAME = "Disposable";

    private final DisposableRepository disposableRepository;
    private final DisposableMapper disposableMapper;


    @Override
    public List<DisposableDto> getAll() {
        return disposableRepository.findAllByOrderByDisplayOrderAsc()
                .stream()
                .map(disposableMapper::toDto)
                .toList();
    }


    @Override
    public DisposableDto getById(Long id) {
        return disposableRepository.findById(id)
                .map(disposableMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE_ENTITY_NAME, id));
    }


    @Override
    @Transactional
    public DisposableDto create(DisposableDto disposableDto) {
        Disposable disposable = disposableMapper.toEntity(disposableDto);
        return disposableMapper.toDto(disposableRepository.save(disposable));
    }


    @Override
    @Transactional
    public DisposableDto update(Long id, DisposableDto disposableDto) {
        Disposable disposable = disposableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE_ENTITY_NAME, id));
        disposableMapper.updateEntity(disposableDto, disposable);
        return disposableMapper.toDto(disposableRepository.save(disposable));
    }


    @Override
    @Transactional
    public void delete(Long id) {
        if (!disposableRepository.existsById(id)) {
            throw new ResourceNotFoundException(MESSAGE_ENTITY_NAME, id);
        }
        disposableRepository.deleteById(id);
    }
    
}
