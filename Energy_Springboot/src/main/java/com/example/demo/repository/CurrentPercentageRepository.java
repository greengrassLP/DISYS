package com.example.demo.repository;

import com.example.demo.model.CurrentPercentage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface CurrentPercentageRepository extends JpaRepository<CurrentPercentage, Instant>{
}
