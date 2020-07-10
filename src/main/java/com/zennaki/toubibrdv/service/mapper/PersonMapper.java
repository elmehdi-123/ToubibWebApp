package com.zennaki.toubibrdv.service.mapper;


import com.zennaki.toubibrdv.domain.*;
import com.zennaki.toubibrdv.service.dto.PersonDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Person} and its DTO {@link PersonDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, SpecialtyMapper.class})
public interface PersonMapper extends EntityMapper<PersonDTO, Person> {

    
    PersonDTO toDto(Person person);

    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "removeAddress", ignore = true)
    @Mapping(target = "removeSpecialty", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "removeAppointment", ignore = true)
    @Mapping(target = "disponibilties", ignore = true)
    @Mapping(target = "removeDisponibilties", ignore = true)
    Person toEntity(PersonDTO personDTO);

    default Person fromId(Long id) {
        if (id == null) {
            return null;
        }
        Person person = new Person();
        person.setId(id);
        return person;
    }
}
