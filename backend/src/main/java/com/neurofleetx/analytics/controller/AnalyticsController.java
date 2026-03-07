package com.neurofleetx.analytics.controller;

import com.neurofleetx.analytics.dto.DailyAnalyticsDTO;
import com.neurofleetx.analytics.service.AnalyticsService;
import com.neurofleetx.common.dto.ApiResponse;
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

    @PostMapping("/generate")
    public ResponseEntity<?> generateDailyAnalytics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate targetDate = date != null ? date : LocalDate.now();
        DailyAnalyticsDTO analytics = analyticsService.generateDailyAnalytics(targetDate);
        return ResponseEntity.ok(ApiResponse.success(analytics, "Daily analytics generated"));
    }

    @GetMapping("/daily/{date}")
    public ResponseEntity<?> getDailyAnalytics(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DailyAnalyticsDTO analytics = analyticsService.getDailyAnalytics(date);
        if (analytics == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("No analytics found for the date", 400));
        }
        return ResponseEntity.ok(ApiResponse.success(analytics, "Retrieved daily analytics"));
    }

    @GetMapping("/range")
    public ResponseEntity<?> getAnalyticsRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<DailyAnalyticsDTO> analytics = analyticsService.getAnalyticsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(analytics, "Retrieved analytics for date range"));
    }

    @GetMapping("/reports/monthly/{yearMonth}")
    public ResponseEntity<?> getMonthlySummary(@PathVariable String yearMonth) {
        try {
            YearMonth ym = YearMonth.parse(yearMonth);
            Map<String, Object> summary = analyticsService.getMonthlySummary(ym);
            return ResponseEntity.ok(ApiResponse.success(summary, "Retrieved monthly summary"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid date format. Use YYYY-MM", 400));
        }
    }

    @GetMapping("/reports/vehicle-utilization")
    public ResponseEntity<?> getVehicleUtilizationReport() {
        Map<String, Object> report = analyticsService.getVehicleUtilizationReport();
        return ResponseEntity.ok(ApiResponse.success(report, "Retrieved vehicle utilization report"));
    }

    @GetMapping("/reports/revenue")
    public ResponseEntity<?> getRevenueAnalytics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<String, Object> report = analyticsService.getRevenueAnalytics(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(report, "Retrieved revenue analytics"));
    }

    @GetMapping("/reports/customer")
    public ResponseEntity<?> getCustomerAnalytics() {
        Map<String, Object> report = analyticsService.getCustomerAnalytics();
        return ResponseEntity.ok(ApiResponse.success(report, "Retrieved customer analytics"));
    }

    @GetMapping("/reports/operational")
    public ResponseEntity<?> getOperationalMetrics() {
        Map<String, Object> metrics = analyticsService.getOperationalMetrics();
        return ResponseEntity.ok(ApiResponse.success(metrics, "Retrieved operational metrics"));
    }

    @GetMapping("/reports/dashboard")
    public ResponseEntity<?> getComprehensiveDashboardReport() {
        Map<String, Object> report = analyticsService.getComprehensiveDashboardReport();
        return ResponseEntity.ok(ApiResponse.success(report, "Retrieved comprehensive dashboard report"));
    }
}
