package org.example.currentpercentageservice.repository;

import org.example.currentpercentageservice.model.CurrentPercentage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface CurrentPercentageRepository
        extends JpaRepository<CurrentPercentage, Instant> {

}
