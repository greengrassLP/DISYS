package com.example.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface EnergyDB extends JpaRepository<EnergyDataEntity, LocalDateTime> {
    List<EnergyDataEntity> findByHourBetween(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT " +
            "SUM(community_produced) AS totalCommunityProduced, " +
            "SUM(community_used) AS totalCommunityUsed, " +
            "SUM(grid_used) AS totalGridUsed " +
            "FROM energy_data " +
            "WHERE hour BETWEEN :start AND :end", nativeQuery = true)
    Object[] findTotalValuesBetweenDates(@Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end);

    @Query(value = "SELECT SUM(community_produced) FROM energy_data", nativeQuery = true)
    Double selectCommunityProducedTotals();

    @Query(value = "SELECT SUM(community_used) FROM energy_data", nativeQuery = true)
    Double selectCommunityUsedTotals();

    @Query(value = "SELECT SUM(grid_used) FROM energy_data", nativeQuery = true)
    Double selectGridUsedTotals();
}