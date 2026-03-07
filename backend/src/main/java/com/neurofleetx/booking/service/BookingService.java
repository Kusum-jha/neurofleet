package com.neurofleetx.booking.service;

import com.neurofleetx.fleet.entity.Vehicle;
import com.neurofleetx.fleet.repository.VehicleRepository;
import com.neurofleetx.auth.entity.User;
import com.neurofleetx.auth.repository.UserRepository;
import com.neurofleetx.booking.dto.BookingDTO;
import com.neurofleetx.booking.dto.LoyaltyPointsDTO;
import com.neurofleetx.booking.entity.Booking;
import com.neurofleetx.booking.entity.LoyaltyPoints;
import com.neurofleetx.booking.repository.BookingRepository;
import com.neurofleetx.booking.repository.LoyaltyPointsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private LoyaltyPointsRepository loyaltyPointsRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    private static final BigDecimal PRICE_PER_KM = new BigDecimal("15.0");
    private static final Integer LOYALTY_POINTS_PER_RUPEE = 1;

    /**
     * Create a new booking
     */
    public BookingDTO createBooking(BookingDTO dto) {
        Booking booking = new Booking();

        // Generate unique booking number
        booking.setBookingNumber(generateBookingNumber());

        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId()).orElse(null);
        booking.setVehicle(vehicle);

        User customer = userRepository.findById(dto.getCustomerId()).orElse(null);
        booking.setCustomer(customer);

        booking.setPickupLatitude(dto.getPickupLatitude());
        booking.setPickupLongitude(dto.getPickupLongitude());
        booking.setDropoffLatitude(dto.getDropoffLatitude());
        booking.setDropoffLongitude(dto.getDropoffLongitude());
        booking.setPickupTime(dto.getPickupTime());
        booking.setStatus("PENDING");
        booking.setPaymentStatus("PENDING");
        booking.setNotes(dto.getNotes());

        // Calculate estimated fare and duration
        if (dto.getDistanceKm() != null) {
            booking.setDistanceKm(dto.getDistanceKm());
            booking.setEstimatedFare(dto.getDistanceKm().multiply(PRICE_PER_KM));
            
            // Estimate duration: roughly 40 km/h average speed
            Integer estimatedMinutes = Math.round(dto.getDistanceKm().floatValue() * 60 / 40);
            booking.setDurationMinutes(estimatedMinutes);
        }

        Booking saved = bookingRepository.save(booking);
        return convertToDTO(saved);
    }

    /**
     * Get all bookings
     */
    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get bookings for a customer
     */
    public List<BookingDTO> getBookingsByCustomer(Long customerId) {
        return bookingRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get bookings for a vehicle
     */
    public List<BookingDTO> getBookingsByVehicle(Long vehicleId) {
        return bookingRepository.findByVehicleId(vehicleId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get bookings by status
     */
    public List<BookingDTO> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific booking
     */
    public BookingDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        return booking != null ? convertToDTO(booking) : null;
    }

    /**
     * Update booking status
     */
    public BookingDTO updateBookingStatus(Long id, String status) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null) {
            return null;
        }

        booking.setStatus(status);

        if ("COMPLETED".equals(status)) {
            booking.setCompletedAt(LocalDateTime.now());
            booking.setPaymentStatus("COMPLETED");
            
            // Add loyalty points when booking is completed
            if (booking.getActualFare() != null) {
                addLoyaltyPoints(booking.getCustomer().getId(), 
                    booking.getActualFare().intValue());
            }
        }

        Booking updated = bookingRepository.save(booking);
        return convertToDTO(updated);
    }

    /**
     * Complete booking and record actual fare
     */
    public BookingDTO completeBooking(Long id, BookingDTO dto) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null) {
            return null;
        }

        booking.setStatus("COMPLETED");
        booking.setDropoffTime(LocalDateTime.now());
        booking.setActualFare(dto.getActualFare());
        booking.setPaymentStatus("COMPLETED");
        booking.setCompletedAt(LocalDateTime.now());

        // Add loyalty points
        if (dto.getActualFare() != null) {
            addLoyaltyPoints(booking.getCustomer().getId(), 
                dto.getActualFare().intValue());
        }

        Booking updated = bookingRepository.save(booking);
        return convertToDTO(updated);
    }

    /**
     * Cancel booking
     */
    public BookingDTO cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null) {
            return null;
        }

        booking.setStatus("CANCELLED");
        booking.setPaymentStatus("PENDING");

        Booking updated = bookingRepository.save(booking);
        return convertToDTO(updated);
    }

    /**
     * Generate booking statistics
     */
    public Map<String, Object> getBookingStatistics() {
        List<Booking> allBookings = bookingRepository.findAll();

        long totalBookings = allBookings.size();
        long completedBookings = allBookings.stream()
                .filter(b -> "COMPLETED".equals(b.getStatus())).count();
        long pendingBookings = allBookings.stream()
                .filter(b -> "PENDING".equals(b.getStatus())).count();
        long cancelledBookings = allBookings.stream()
                .filter(b -> "CANCELLED".equals(b.getStatus())).count();

        BigDecimal totalRevenue = allBookings.stream()
                .filter(b -> "COMPLETED".equals(b.getStatus()) && b.getActualFare() != null)
                .map(Booking::getActualFare)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Double totalDistance = allBookings.stream()
                .filter(b -> b.getDistanceKm() != null)
                .mapToDouble(b -> b.getDistanceKm().doubleValue())
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBookings", totalBookings);
        stats.put("completedBookings", completedBookings);
        stats.put("pendingBookings", pendingBookings);
        stats.put("cancelledBookings", cancelledBookings);
        stats.put("totalRevenue", totalRevenue);
        stats.put("totalDistance", totalDistance);
        stats.put("averageBookingValue", totalBookings > 0 ? 
            totalRevenue.divide(new BigDecimal(completedBookings), 2, BigDecimal.ROUND_HALF_UP) : 
            BigDecimal.ZERO);

        return stats;
    }

    // Loyalty Points Methods

    /**
     * Initialize loyalty points for a customer
     */
    public LoyaltyPointsDTO initializeLoyaltyPoints(Long customerId) {
        User customer = userRepository.findById(customerId).orElse(null);
        if (customer == null) {
            return null;
        }

        Optional<LoyaltyPoints> existing = loyaltyPointsRepository.findByCustomerId(customerId);
        if (existing.isPresent()) {
            return convertPointsToDTO(existing.get());
        }

        LoyaltyPoints loyaltyPoints = new LoyaltyPoints();
        loyaltyPoints.setCustomer(customer);
        loyaltyPoints.setPointsBalance(0);
        loyaltyPoints.setTotalPointsEarned(0);
        loyaltyPoints.setTotalPointsRedeemed(0);

        LoyaltyPoints saved = loyaltyPointsRepository.save(loyaltyPoints);
        return convertPointsToDTO(saved);
    }

    /**
     * Add loyalty points to customer
     */
    public void addLoyaltyPoints(Long customerId, Integer amount) {
        Optional<LoyaltyPoints> loyaltyOpt = loyaltyPointsRepository.findByCustomerId(customerId);

        LoyaltyPoints loyalty;
        if (loyaltyOpt.isEmpty()) {
            User customer = userRepository.findById(customerId).orElse(null);
            loyalty = new LoyaltyPoints();
            loyalty.setCustomer(customer);
            loyalty.setPointsBalance(0);
            loyalty.setTotalPointsEarned(0);
            loyalty.setTotalPointsRedeemed(0);
        } else {
            loyalty = loyaltyOpt.get();
        }

        Integer pointsToAdd = amount * LOYALTY_POINTS_PER_RUPEE;
        loyalty.setPointsBalance(loyalty.getPointsBalance() + pointsToAdd);
        loyalty.setTotalPointsEarned(loyalty.getTotalPointsEarned() + pointsToAdd);

        loyaltyPointsRepository.save(loyalty);
    }

    /**
     * Redeem loyalty points
     */
    public LoyaltyPointsDTO redeemLoyaltyPoints(Long customerId, Integer pointsToRedeem) {
        Optional<LoyaltyPoints> loyaltyOpt = loyaltyPointsRepository.findByCustomerId(customerId);

        if (loyaltyOpt.isEmpty()) {
            return null;
        }

        LoyaltyPoints loyalty = loyaltyOpt.get();
        if (loyalty.getPointsBalance() < pointsToRedeem) {
            return null; // Insufficient points
        }

        loyalty.setPointsBalance(loyalty.getPointsBalance() - pointsToRedeem);
        loyalty.setTotalPointsRedeemed(loyalty.getTotalPointsRedeemed() + pointsToRedeem);

        LoyaltyPoints updated = loyaltyPointsRepository.save(loyalty);
        return convertPointsToDTO(updated);
    }

    /**
     * Get loyalty points for a customer
     */
    public LoyaltyPointsDTO getLoyaltyPoints(Long customerId) {
        Optional<LoyaltyPoints> loyalty = loyaltyPointsRepository.findByCustomerId(customerId);
        return loyalty.map(this::convertPointsToDTO).orElse(null);
    }

    /**
     * Get all loyalty points
     */
    public List<LoyaltyPointsDTO> getAllLoyaltyPoints() {
        return loyaltyPointsRepository.findAll().stream()
                .map(this::convertPointsToDTO)
                .collect(Collectors.toList());
    }

    // Helper methods

    private String generateBookingNumber() {
        return "BK-" + System.currentTimeMillis();
    }

    private BookingDTO convertToDTO(Booking booking) {
        return new BookingDTO(
                booking.getId(),
                booking.getBookingNumber(),
                booking.getVehicle() != null ? booking.getVehicle().getId() : null,
                booking.getVehicle() != null ? booking.getVehicle().getVehicleNumber() : null,
                booking.getCustomer() != null ? booking.getCustomer().getId() : null,
                booking.getCustomer() != null ? booking.getCustomer().getUsername() : null,
                booking.getPickupLatitude(),
                booking.getPickupLongitude(),
                booking.getDropoffLatitude(),
                booking.getDropoffLongitude(),
                booking.getPickupTime(),
                booking.getDropoffTime(),
                booking.getEstimatedFare(),
                booking.getActualFare(),
                booking.getStatus(),
                booking.getPaymentStatus(),
                booking.getDistanceKm(),
                booking.getDurationMinutes(),
                booking.getNotes(),
                booking.getCreatedAt(),
                booking.getCompletedAt()
        );
    }

    private LoyaltyPointsDTO convertPointsToDTO(LoyaltyPoints loyalty) {
        return new LoyaltyPointsDTO(
                loyalty.getId(),
                loyalty.getCustomer() != null ? loyalty.getCustomer().getId() : null,
                loyalty.getCustomer() != null ? loyalty.getCustomer().getUsername() : null,
                loyalty.getPointsBalance(),
                loyalty.getTotalPointsEarned(),
                loyalty.getTotalPointsRedeemed()
        );
    }
}
