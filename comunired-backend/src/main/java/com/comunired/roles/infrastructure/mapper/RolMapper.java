package com.comunired.roles.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.comunired.roles.domain.Rol;
import com.comunired.roles.application.dto.RolDTO;
import com.comunired.roles.infrastructure.entity.RolEntity;

@Mapper(componentModel = "spring")
public interface RolMapper {

    RolMapper INSTANCE = Mappers.getMapper(RolMapper.class);

    // Mapea RolEntity -> Rol (dominio)
    @Mapping(target = "nombre", source = "nombre")
    Rol toDomain(RolEntity entity);

    // Mapea Rol -> RolEntity (activa por defecto si entity no tiene valor)
    @Mapping(target = "activo", constant = "true")
    RolEntity toEntity(Rol domain);

    RolDTO toDTO(Rol domain);

    Rol fromDTO(RolDTO dto);
}