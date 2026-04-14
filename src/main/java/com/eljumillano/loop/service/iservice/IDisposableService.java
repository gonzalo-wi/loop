package com.eljumillano.loop.service.iservice;
import java.util.List;
import com.eljumillano.loop.dtos.disposable.DisposableDto;

public interface IDisposableService {
    public List<DisposableDto> getAll();
    public DisposableDto getById(Long id);
    public DisposableDto create(DisposableDto disposableDto);   
    public DisposableDto update(Long id, DisposableDto disposableDto);
    public void delete(Long id);
}
