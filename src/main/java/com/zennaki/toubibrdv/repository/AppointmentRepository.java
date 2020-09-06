package com.zennaki.toubibrdv.repository;

import com.zennaki.toubibrdv.domain.Appointment;
import com.zennaki.toubibrdv.domain.Person;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Appointment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

	 @Query("select app from Appointment app left join fetch app.person person where person.specialties in (:speciality)")
	 Optional<Appointment> findOneWithEagerRelationships(@Param("speciality") String speciality);

}
