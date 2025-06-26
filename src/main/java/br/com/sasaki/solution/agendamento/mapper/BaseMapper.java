package br.com.sasaki.solution.agendamento.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

public interface BaseMapper <E, Req, Res>{

    E toEntity(Req dto);

    Res toDTO(E entity);

    List<Res> toDTOList(List<E> entityList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(Req dto, @MappingTarget E entity);


}
