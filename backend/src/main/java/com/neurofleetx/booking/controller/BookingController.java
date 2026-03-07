package com.neurofleetx.booking.controller;

import com.neurofleetx.common.dto.ApiResponse;
import com.neurofleetx.booking.dto.BookingDTO;
import com.neurofleetx.booking.dto.LoyaltyPointsDTO;
import com.neurofleetx.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingDTO dto) {
        BookingDTO created = bookingService.createBooking(dto);
        return ResponseEntity.ok(ApiResponse.success(created, "Booking created successfully"));
    }

    @GetMapping
    public ResponseEntity<?> getAllBookings() {
        List<BookingDTO> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(ApiResponse.success(bookings, "Retrieved all bookings"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        BookingDTO booking = bookingService.getBookingById(id);
        if (booking == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Booking not found", 400));
        }
        return ResponseEntity.ok(ApiResponse.success(booking, "Retrieved booking"));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getCustomerBookings(@PathVariable Long customerId) {
        List<BookingDTO> bookings = bookingService.getBookingsByCustomer(customerId);
        return ResponseEntity.ok(ApiResponse.success(bookings, "Retrieved customer bookings"));
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<?> getVehicleBookings(@PathVariable Long vehicleId) {
        List<BookingDTO> bookings = bookingService.getBookingsByVehicle(vehicleId);
        return ResponseEntity.ok(ApiResponse.success(bookings, "Retrieved vehicle bookings"));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getBookingsByStatus(@PathVariable String status) {
        List<BookingDTO> bookings = bookingService.getBookingsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(bookings, "Retrieved bookings by status"));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id, @RequestParam String status) {
        BookingDTO updated = bookingService.updateBookingStatus(id, status);
        if (updated == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Booking not found", 400));
        }
        return ResponseEntity.ok(ApiResponse.success(updated, "Booking status updated"));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeBooking(@PathVariable Long id, @RequestBody BookingDTO dto) {
        BookingDTO completed = bookingService.completeBooking(id, dto);
        if (completed == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Booking not found", 400));
        }
        return ResponseEntity.ok(ApiResponse.success(completed, "Booking completed"));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        BookingDTO cancelled = bookingService.cancelBooking(id);
        if (cancelled == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Booking not found", 400));
        }
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Booking cancelled"));
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getBookingStatistics() {
        Map<String, Object> stats = bookingService.getBookingStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Retrieved booking statistics"));
    }

    @PostMapping("/loyalty/initialize/{customerId}")
    public ResponseEntity<?> initializeLoyaltyPoints(@PathVariable Long customerId) {
        LoyaltyPointsDTO loyalty = bookingService.initializeLoyaltyPoints(customerId);
        if (loyalty == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Customer not found", 400));
        }
        return ResponseEntity.ok(ApiResponse.success(loyalty, "Loyalty points initialized"));
    }

    @GetMapping("/loyalty/{customerId}")
    public ResponseEntity<?> getLoyaltyPoints(@PathVariable Long customerId) {
        LoyaltyPointsDTO loyalty = bookingService.getLoyaltyPoints(customerId);
        if (loyalty == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Loyalty points not found", 400));
        }
        return ResponseEntity.ok(ApiResponse.success(loyalty, "Retrieved loyalty points"));
    }

    @GetMapping("/loyalty")
    public ResponseEntity<?> getAllLoyaltyPoints() {
        List<LoyaltyPointsDTO> loyaltyList = bookingService.getAllLoyaltyPoints();
        return ResponseEntity.ok(ApiResponse.success(loyaltyList, "Retrieved all loyalty points"));
    }

    @PostMapping("/loyalty/{customerId}/redeem/{points}")
    public ResponseEntity<?> redeemLoyaltyPoints(@PathVariable Long customerId, @PathVariable int points) {
        LoyaltyPointsDTO redeemed = bookingService.redeemLoyaltyPoints(customerId, points);
        if (redeemed == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Insufficient loyalty points or customer not found", 400));
        }
        return ResponseEntity.ok(ApiResponse.success(redeemed, "Loyalty points redeemed"));
    }
}
