package com.neurofleetx.analytics.service;

import com.neurofleetx.analytics.dto.DailyAnalyticsDTO;
import com.neurofleetx.analytics.entity.DailyAnalytics;
import com.neurofleetx.analytics.repository.DailyAnalyticsRepository;
import com.neurofleetx.booking.entity.Booking;
import com.neurofleetx.booking.repository.BookingRepository;
import com.neurofleetx.fleet.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private DailyAnalyticsRepository dailyAnalyticsRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    /**
     * Generate daily analytics report
     */
    public DailyAnalyticsDTO generateDailyAnalytics(LocalDate date) {
        // Check if already exists
        Optional<DailyAnalytics> existing = dailyAnalyticsRepository.findByAnalyticsDate(date);
        if (existing.isPresent()) {
            return convertToDTO(existing.get());
        }

        DailyAnalytics analytics = new DailyAnalytics();
        analytics.setAnalyticsDate(date);

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        // Get bookings for the day
        List<Booking> dayBookings = bookingRepository.findBookingsBetween(startOfDay, endOfDay);

        // Calculate metrics
        analytics.setTotalBookings(dayBookings.size());
        long completedCount = dayBookings.stream()
                .filter(b -> "COMPLETED".equals(b.getStatus())).count();
        analytics.setCompletedBookings((int) completedCount);

        BigDecimal totalDistance = dayBookings.stream()
                .filter(b -> b.getDistanceKm() != null)
                .map(Booking::getDistanceKm)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        analytics.setTotalDistanceKm(totalDistance);

        BigDecimal totalRevenue = dayBookings.stream()
                .filter(b -> "COMPLETED".equals(b.getStatus()) && b.getActualFare() != null)
                .map(Booking::getActualFare)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        analytics.setTotalRevenue(totalRevenue);

        // Vehicle metrics
        long activeVehicles = vehicleRepository.findAll().stream()
                .filter(v -> !"MAINTENANCE".equals(v.getStatus())).count();
        analytics.setActiveVehicles((int) activeVehicles);

        long availableVehicles = vehicleRepository.findByStatus("AVAILABLE").size();
        analytics.setAvailableVehicles((int) availableVehicles);

        long maintenanceVehicles = vehicleRepository.findByStatus("MAINTENANCE").size();
        analytics.setMaintenanceVehicles((int) maintenanceVehicles);

        // Calculate utilization
        int totalVehicles = vehicleRepository.findAll().size();
        if (totalVehicles > 0) {
            BigDecimal utilization = new BigDecimal(activeVehicles)
                    .divide(new BigDecimal(totalVehicles), 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100));
            analytics.setAvgVehicleUtilization(utilization);
        }

        DailyAnalytics saved = dailyAnalyticsRepository.save(analytics);
        return convertToDTO(saved);
    }

    /**
     * Get daily analytics for a specific date
     */
    public DailyAnalyticsDTO getDailyAnalytics(LocalDate date) {
        Optional<DailyAnalytics> analytics = dailyAnalyticsRepository.findByAnalyticsDate(date);
        return analytics.map(this::convertToDTO).orElse(null);
    }

    /**
     * Get analytics for a date range
     */
    public List<DailyAnalyticsDTO> getAnalyticsByDateRange(LocalDate startDate, LocalDate endDate) {
        return dailyAnalyticsRepository.findByDateRange(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get monthly summary report
     */
    public Map<String, Object> getMonthlySummary(YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<DailyAnalytics> monthlyData = dailyAnalyticsRepository.findByDateRange(startDate, endDate);

        Map<String, Object> summary = new HashMap<>();
        summary.put("month", yearMonth.toString());
        summary.put("totalDays", monthlyData.size());

        if (!monthlyData.isEmpty()) {
            Integer totalBookings = monthlyData.stream()
                    .mapToInt(DailyAnalytics::getTotalBookings).sum();
            Integer completedBookings = monthlyData.stream()
                    .mapToInt(DailyAnalytics::getCompletedBookings).sum();
            BigDecimal totalRevenue = monthlyData.stream()
                    .map(DailyAnalytics::getTotalRevenue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalDistance = monthlyData.stream()
                    .map(DailyAnalytics::getTotalDistanceKm)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            summary.put("totalBookings", totalBookings);
            summary.put("completedBookings", completedBookings);
            summary.put("totalRevenue", totalRevenue);
            summary.put("totalDistance", totalDistance);
            summary.put("avgDailyBookings", (double) totalBookings / monthlyData.size());
            summary.put("avgDailyRevenue", totalRevenue.divide(new BigDecimal(monthlyData.size()), 2, BigDecimal.ROUND_HALF_UP));
        }

        return summary;
    }

    /**
     * Get vehicle utilization report
     */
    public Map<String, Object> getVehicleUtilizationReport() {
        List<Booking> allBookings = bookingRepository.findAll();

        Map<Long, Integer> vehicleBookingCount = new HashMap<>();
        allBookings.forEach(b -> {
            if (b.getVehicle() != null) {
                vehicleBookingCount.put(b.getVehicle().getId(),
                    vehicleBookingCount.getOrDefault(b.getVehicle().getId(), 0) + 1);
            }
        });

        int totalVehicles = vehicleRepository.findAll().size();
        double avgBookingsPerVehicle = allBookings.size() / Math.max(totalVehicles, 1);

        Map<String, Object> report = new HashMap<>();
        report.put("totalVehicles", totalVehicles);
        report.put("totalBookings", allBookings.size());
        report.put("avgBookingsPerVehicle", avgBookingsPerVehicle);
        report.put("vehicleBookingDetails", vehicleBookingCount);

        return report;
    }

    /**
     * Get revenue analytics
     */
    public Map<String, Object> getRevenueAnalytics(LocalDate startDate, LocalDate endDate) {
        List<Booking> bookings = bookingRepository.findBookingsBetween(
                startDate.atStartOfDay(), endDate.atTime(23, 59, 59));

        BigDecimal totalRevenue = bookings.stream()
                .filter(b -> "COMPLETED".equals(b.getStatus()) && b.getActualFare() != null)
                .map(Booking::getActualFare)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long completedCount = bookings.stream()
                .filter(b -> "COMPLETED".equals(b.getStatus())).count();

        BigDecimal avgFare = completedCount > 0 ? 
                totalRevenue.divide(new BigDecimal(completedCount), 2, BigDecimal.ROUND_HALF_UP) : 
                BigDecimal.ZERO;

        Map<String, Object> analytics = new HashMap<>();
        analytics.put("totalRevenue", totalRevenue);
        analytics.put("completedBookings", completedCount);
        analytics.put("avgFarePerBooking", avgFare);
        analytics.put("totalDistance", bookings.stream()
                .filter(b -> b.getDistanceKm() != null)
                .map(Booking::getDistanceKm)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return analytics;
    }

    /**
     * Get customer analytics
     */
    public Map<String, Object> getCustomerAnalytics() {
        List<Booking> allBookings = bookingRepository.findAll();

        Map<Long, Integer> customerBookingCount = new HashMap<>();
        Map<Long, BigDecimal> customerSpending = new HashMap<>();

        allBookings.forEach(b -> {
            if (b.getCustomer() != null) {
                Long customerId = b.getCustomer().getId();
                customerBookingCount.put(customerId,
                    customerBookingCount.getOrDefault(customerId, 0) + 1);
                
                if ("COMPLETED".equals(b.getStatus()) && b.getActualFare() != null) {
                    customerSpending.put(customerId,
                        customerSpending.getOrDefault(customerId, BigDecimal.ZERO)
                            .add(b.getActualFare()));
                }
            }
        });

        BigDecimal totalSpending = customerSpending.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> analytics = new HashMap<>();
        analytics.put("totalCustomers", customerBookingCount.size());
        analytics.put("totalSpending", totalSpending);
        analytics.put("avgSpendingPerCustomer", customerBookingCount.size() > 0 ?
                totalSpending.divide(new BigDecimal(customerBookingCount.size()), 2, BigDecimal.ROUND_HALF_UP) :
                BigDecimal.ZERO);
        analytics.put("customerDetails", customerBookingCount);

        return analytics;
    }

    /**
     * Get operational metrics - comprehensive KPIs
     */
    public Map<String, Object> getOperationalMetrics() {
        List<Booking> allBookings = bookingRepository.findAll();
        int totalVehicles = vehicleRepository.findAll().size();

        long completedBookings = allBookings.stream()
                .filter(b -> "COMPLETED".equals(b.getStatus())).count();
        long cancelledBookings = allBookings.stream()
                .filter(b -> "CANCELLED".equals(b.getStatus())).count();

        BigDecimal totalRevenue = allBookings.stream()
                .filter(b -> "COMPLETED".equals(b.getStatus()) && b.getActualFare() != null)
                .map(Booking::getActualFare)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double completionRate = allBookings.size() > 0 ?
                (double) completedBookings / allBookings.size() * 100 :
                0;

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalBookings", allBookings.size());
        metrics.put("completedBookings", completedBookings);
        metrics.put("cancelledBookings", cancelledBookings);
        metrics.put("completionRate", completionRate);
        metrics.put("totalRevenue", totalRevenue);
        metrics.put("totalVehicles", totalVehicles);
        metrics.put("activeVehicles", vehicleRepository.findAll().stream()
                .filter(v -> !"MAINTENANCE".equals(v.getStatus())).count());

        return metrics;
    }

    /**
     * Generate comprehensive admin dashboard report
     */
    public Map<String, Object> getComprehensiveDashboardReport() {
        Map<String, Object> report = new HashMap<>();

        // Get operational metrics
        report.put("operationalMetrics", getOperationalMetrics());

        // Get customer analytics
        report.put("customerAnalytics", getCustomerAnalytics());

        // Get vehicle utilization
        report.put("vehicleUtilization", getVehicleUtilizationReport());

        // Get recent daily analytics (last 7 days)
        LocalDate today = LocalDate.now();
        List<DailyAnalyticsDTO> recentData = getAnalyticsByDateRange(
                today.minusDays(7), today);
        report.put("recentDailyData", recentData);

        return report;
    }

    // Helper methods

    private DailyAnalyticsDTO convertToDTO(DailyAnalytics analytics) {
        return new DailyAnalyticsDTO(
                analytics.getId(),
                analytics.getAnalyticsDate(),
                analytics.getTotalBookings(),
                analytics.getCompletedBookings(),
                analytics.getTotalDistanceKm(),
                analytics.getTotalRevenue(),
                analytics.getActiveVehicles(),
                analytics.getAvailableVehicles(),
                analytics.getMaintenanceVehicles(),
                analytics.getAvgVehicleUtilization()
        );
    }
}
