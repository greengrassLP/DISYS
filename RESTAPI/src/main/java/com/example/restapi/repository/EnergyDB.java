package com.example.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EnergyDB extends JpaRepository<EnergyDataEntity, LocalDateTime> {

    List<EnergyDataEntity> findByHourBetween(LocalDateTime start, LocalDateTime end);

    @Query(value = """
        SELECT SUM(community_produced) AS totalCommunityProduced,
               SUM(community_used)     AS totalCommunityUsed,
               SUM(grid_used)          AS totalGridUsed
        FROM energy_data
        WHERE hour BETWEEN :start AND :end
        """, nativeQuery = true)
    Object[] findTotalValuesBetweenDates(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT SUM(community_produced) FROM energy_data", nativeQuery = true)
    Double selectCommunityProducedTotals();

    @Query(value = "SELECT SUM(community_used) FROM energy_data", nativeQuery = true)
    Double selectCommunityUsedTotals();

    @Query(value = "SELECT SUM(grid_used) FROM energy_data", nativeQuery = true)
    Double selectGridUsedTotals();

    // — hier die neuen Methoden mit @Query Annotationen —

    @Query(value = """
        SELECT COALESCE(SUM(community_produced), 0)
        FROM energy_data
        WHERE date_trunc('hour', hour) = date_trunc('hour', now())
        """, nativeQuery = true)
    double selectCommunityProducedForCurrentHour();

    @Query(value = """
        SELECT COALESCE(SUM(community_used), 0)
        FROM energy_data
        WHERE date_trunc('hour', hour) = date_trunc('hour', now())
        """, nativeQuery = true)
    double selectCommunityUsedForCurrentHour();

    @Query(value = """
        SELECT COALESCE(SUM(grid_used), 0)
        FROM energy_data
        WHERE date_trunc('hour', hour) = date_trunc('hour', now())
        """, nativeQuery = true)
    double selectGridUsedForCurrentHour();
}
