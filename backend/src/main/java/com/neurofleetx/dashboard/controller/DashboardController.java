package com.neurofleetx.dashboard.controller;

import com.neurofleetx.dashboard.service.DashboardService;
import com.neurofleetx.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/metrics/admin")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAdminMetrics() {
        Map<String, Object> metrics = dashboardService.getAdminMetrics();
        return ResponseEntity.ok(ApiResponse.success(metrics, "Admin metrics retrieved"));
    }

    @GetMapping("/metrics/manager")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getManagerMetrics() {
        Map<String, Object> metrics = dashboardService.getManagerMetrics();
        return ResponseEntity.ok(ApiResponse.success(metrics, "Manager metrics retrieved"));
    }

    @GetMapping("/metrics/driver")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDriverMetrics() {
        Map<String, Object> metrics = dashboardService.getDriverMetrics();
        return ResponseEntity.ok(ApiResponse.success(metrics, "Driver metrics retrieved"));
    }

    @GetMapping("/metrics/customer")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCustomerMetrics() {
        Map<String, Object> metrics = dashboardService.getCustomerMetrics();
        return ResponseEntity.ok(ApiResponse.success(metrics, "Customer metrics retrieved"));
    }

    @GetMapping("/{role}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardByRole(@PathVariable String role) {
        Map<String, Object> metrics = switch (role.toUpperCase()) {
            case "ADMIN" -> dashboardService.getAdminMetrics();
            case "FLEET_MANAGER" -> dashboardService.getManagerMetrics();
            case "DRIVER" -> dashboardService.getDriverMetrics();
            case "CUSTOMER" -> dashboardService.getCustomerMetrics();
            default -> Map.of();
        };
        return ResponseEntity.ok(ApiResponse.success(metrics, "Dashboard metrics retrieved"));
    }
}
