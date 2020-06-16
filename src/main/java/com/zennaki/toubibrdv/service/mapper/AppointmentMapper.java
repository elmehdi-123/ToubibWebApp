package com.zennaki.toubibrdv.service.mapper;


import com.zennaki.toubibrdv.domain.*;
import com.zennaki.toubibrdv.service.dto.AppointmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Appointment} and its DTO {@link AppointmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface AppointmentMapper extends EntityMapper<AppointmentDTO, Appointment> {

    @Mapping(source = "person.id", target = "personId")
    @Mapping(source = "docteur.id", target = "docteurId")
    AppointmentDTO toDto(Appointment appointment);

    @Mapping(source = "personId", target = "person")
    @Mapping(source = "docteurId", target = "docteur")
    Appointment toEntity(AppointmentDTO appointmentDTO);

    default Appointment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Appointment appointment = new Appointment();
        appointment.setId(id);
        return appointment;
    }
}
