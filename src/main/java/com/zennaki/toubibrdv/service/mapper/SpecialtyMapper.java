package com.zennaki.toubibrdv.service.mapper;


import com.zennaki.toubibrdv.domain.*;
import com.zennaki.toubibrdv.service.dto.SpecialtyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Specialty} and its DTO {@link SpecialtyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SpecialtyMapper extends EntityMapper<SpecialtyDTO, Specialty> {


    @Mapping(target = "people", ignore = true)
    @Mapping(target = "removePerson", ignore = true)
    Specialty toEntity(SpecialtyDTO specialtyDTO);

    default Specialty fromId(Long id) {
        if (id == null) {
            return null;
        }
        Specialty specialty = new Specialty();
        specialty.setId(id);
        return specialty;
    }
}
