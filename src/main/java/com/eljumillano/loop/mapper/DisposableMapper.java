package com.eljumillano.loop.mapper;

import org.springframework.stereotype.Component;

import com.eljumillano.loop.dtos.disposable.DisposableDto;
import com.eljumillano.loop.model.Disposable;

@Component
public class DisposableMapper {

    public DisposableDto toDto(Disposable disposable) {
        if (disposable == null) {
            return null;
        }
        
        return DisposableDto.builder()
                .id(disposable.getId())
                .name(disposable.getName())
                .displayOrder(disposable.getDisplayOrder())
                .cantBulto(disposable.getCantBulto())
                .createdAt(disposable.getCreatedAt())
                .updatedAt(disposable.getUpdatedAt())
                .build();
    }

    public Disposable toEntity(DisposableDto dto) {
        if (dto == null) {
            return null;
        }
        
        Disposable disposable = new Disposable();
        disposable.setName(dto.getName());
        disposable.setDisplayOrder(dto.getDisplayOrder());
        disposable.setCantBulto(dto.getCantBulto());
        return disposable;
    }

    public void updateEntity(DisposableDto dto, Disposable disposable) {
        if (dto == null || disposable == null) {
            return;
        }
        
        disposable.setName(dto.getName());
        disposable.setDisplayOrder(dto.getDisplayOrder());
        disposable.setCantBulto(dto.getCantBulto());
    }
}
