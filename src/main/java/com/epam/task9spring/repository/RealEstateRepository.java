package com.epam.task9spring.repository;

import com.epam.task9spring.model.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RealEstateRepository extends JpaRepository<RealEstate, Long> {
    Optional<RealEstate> findByName(String name);
}