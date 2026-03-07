package com.neurofleetx.analytics.controller;

import com.neurofleetx.common.dto.ApiResponse;
import com.neurofleetx.analytics.dto.DailyAnalyticsDTO;
import com.neurofleetx.analytics.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "http://localhost:5173")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    /**
     * Generate daily analytics for a specific date
     */
    @PostMapping("/daily/{date}")
    public ResponseEntity<ApiResponse<DailyAnalyticsDTO>> generateDailyAnalytics(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DailyAnalyticsDTO analytics = analyticsService.generateDailyAnalytics(date);
        return ResponseEntity.ok(ApiResponse.success(analytics), "Daily analytics generated");
    }

    /**
     * Get daily analytics for a specific date
     */
    @GetMapping("/daily/{date}")
    public ResponseEntity<ApiResponse<DailyAnalyticsDTO>> getDailyAnalytics(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DailyAnalyticsDTO analytics = analyticsService.getDailyAnalytics(date);
        if (analytics == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("No analytics found for the date", 400));
        }
        return ResponseEntity.ok(ApiResponse.success(analytics), "Retrieved daily analytics");
    }

    /**
     * Get analytics for a date range
     */
    @GetMapping("/range")
    public ResponseEntity<ApiResponse<List<DailyAnalyticsDTO>>> getAnalyticsByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<DailyAnalyticsDTO> analytics = analyticsService.getAnalyticsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(analytics), "Retrieved analytics for date range");
    }

    /**
     * Get monthly summary
     */
    @GetMapping("/monthly/{yearMonth}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMonthlySummary(
            @PathVariable String yearMonth) {
        try {
            YearMonth month = YearMonth.parse(yearMonth);
            Map<String, Object> summary = analyticsService.getMonthlySummary(month);
            return ResponseEntity.ok(ApiResponse.success(summary), "Retrieved monthly summary");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid date format. Use YYYY-MM", 400));
        }
    }

    /**
     * Get vehicle utilization report
     */
    @GetMapping("/reports/vehicle-utilization")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getVehicleUtilizationReport() {
        Map<String, Object> report = analyticsService.getVehicleUtilizationReport();
        return ResponseEntity.ok(ApiResponse.success(report), "Retrieved vehicle utilization report");
    }

    /**
     * Get revenue analytics for a date range
     */
    @GetMapping("/reports/revenue")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRevenueReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<String, Object> report = analyticsService.getRevenueAnalytics(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(report), "Retrieved revenue analytics");
    }

    /**
     * Get customer analytics
     */
    @GetMapping("/reports/customer")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCustomerReport() {
        Map<String, Object> report = analyticsService.getCustomerAnalytics();
        return ResponseEntity.ok(ApiResponse.success(report), "Retrieved customer analytics");
    }

    /**
     * Get operational metrics
     */
    @GetMapping("/reports/operational")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getOperationalMetrics() {
        Map<String, Object> metrics = analyticsService.getOperationalMetrics();
        return ResponseEntity.ok(ApiResponse.success(metrics), "Retrieved operational metrics");
    }

    /**
     * Get comprehensive admin dashboard report
     */
    @GetMapping("/reports/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getComprehensiveDashboard() {
        Map<String, Object> report = analyticsService.getComprehensiveDashboardReport();
        return ResponseEntity.ok(ApiResponse.success(report), "Retrieved comprehensive dashboard report");
    }
}
