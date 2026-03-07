package com.neurofleetx.analytics.repository;

import com.neurofleetx.analytics.entity.DailyAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyAnalyticsRepository extends JpaRepository<DailyAnalytics, Long> {
    Optional<DailyAnalytics> findByAnalyticsDate(LocalDate date);
    
    @Query("SELECT d FROM DailyAnalytics d WHERE d.analyticsDate >= :startDate AND d.analyticsDate <= :endDate ORDER BY d.analyticsDate DESC")
    List<DailyAnalytics> findByDateRange(@Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);
}
