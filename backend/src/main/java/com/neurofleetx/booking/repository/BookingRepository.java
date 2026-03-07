package com.neurofleetx.booking.repository;

import com.neurofleetx.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingNumber(String bookingNumber);
    List<Booking> findByCustomerId(Long customerId);
    List<Booking> findByVehicleId(Long vehicleId);
    List<Booking> findByStatus(String status);
    List<Booking> findByCustomerIdAndStatus(Long customerId, String status);
    
    @Query("SELECT b FROM Booking b WHERE b.pickupTime >= :startTime AND b.pickupTime <= :endTime")
    List<Booking> findBookingsBetween(@Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.customer.id = :customerId AND b.status = 'COMPLETED'")
    Long countCompletedBookingsByCustomer(@Param("customerId") Long customerId);
    
    @Query("SELECT SUM(b.actualFare) FROM Booking b WHERE b.customer.id = :customerId AND b.status = 'COMPLETED'")
    Double getTotalSpendingByCustomer(@Param("customerId") Long customerId);
}
