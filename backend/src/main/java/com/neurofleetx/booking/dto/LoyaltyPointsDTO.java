package com.neurofleetx.booking.dto;

public class LoyaltyPointsDTO {
    private Long id;
    private Long customerId;
    private String customerName;
    private Integer pointsBalance;
    private Integer totalPointsEarned;
    private Integer totalPointsRedeemed;

    public LoyaltyPointsDTO() {}

    public LoyaltyPointsDTO(Long id, Long customerId, String customerName,
                           Integer pointsBalance, Integer totalPointsEarned,
                           Integer totalPointsRedeemed) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.pointsBalance = pointsBalance;
        this.totalPointsEarned = totalPointsEarned;
        this.totalPointsRedeemed = totalPointsRedeemed;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getPointsBalance() {
        return pointsBalance;
    }

    public void setPointsBalance(Integer pointsBalance) {
        this.pointsBalance = pointsBalance;
    }

    public Integer getTotalPointsEarned() {
        return totalPointsEarned;
    }

    public void setTotalPointsEarned(Integer totalPointsEarned) {
        this.totalPointsEarned = totalPointsEarned;
    }

    public Integer getTotalPointsRedeemed() {
        return totalPointsRedeemed;
    }

    public void setTotalPointsRedeemed(Integer totalPointsRedeemed) {
        this.totalPointsRedeemed = totalPointsRedeemed;
    }
}
