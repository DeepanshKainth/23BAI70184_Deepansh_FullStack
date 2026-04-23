package com.example.demo.repository;

import com.example.demo.entity.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("""
	    select p
	    from Patient p
	    where lower(p.fullName) like lower(concat('%', :keyword, '%'))
	       or lower(p.email) like lower(concat('%', :keyword, '%'))
	    order by p.id asc
	    """)
    List<Patient> searchPatients(@Param("keyword") String keyword);

    @Query("""
	    select p
	    from Patient p
	    where (:afterId is null or p.id > :afterId)
	    order by p.id asc
	    """)
    List<Patient> findPatientsPageAfterId(@Param("afterId") Long afterId, Pageable pageable);

    @EntityGraph(attributePaths = "healthRecords")
    @Query("select distinct p from Patient p order by p.id asc")
    List<Patient> findAllWithHealthRecords();

	@Query("""
			select p as patient, count(hr.id) as recordCount
			from Patient p
			left join p.healthRecords hr
			group by p
			order by p.id asc
			""")
	List<PatientSummaryProjection> findPatientSummaries();
}