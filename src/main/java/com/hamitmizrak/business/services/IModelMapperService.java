package com.hamitmizrak.business.services;

// D:Dto
// E:Entity
public interface IModelMapperService<D,E>  {

    // Model Mapper (Dto <==> Entity)
    public D entityToDto(E entity);
    public E dtoToEntity(D dto);
}
