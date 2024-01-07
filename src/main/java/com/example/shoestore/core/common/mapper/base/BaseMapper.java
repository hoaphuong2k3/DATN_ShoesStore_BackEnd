package com.example.shoestore.core.common.mapper.base;

public interface BaseMapper<E, D> {

    E DTOToEntity(D dto);

    D entityToDTO(E entity);

}
