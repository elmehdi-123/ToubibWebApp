package com.zennaki.toubibrdv.service.mapper;


import com.zennaki.toubibrdv.domain.*;
import com.zennaki.toubibrdv.service.dto.AddressDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {

    @Mapping(source = "person.id", target = "personId")
    AddressDTO toDto(Address address);

    @Mapping(source = "personId", target = "person")
    Address toEntity(AddressDTO addressDTO);

    default Address fromId(Long id) {
        if (id == null) {
            return null;
        }
        Address address = new Address();
        address.setId(id);
        return address;
    }
}
