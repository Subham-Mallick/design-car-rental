package org.example.LLD.oops.carrental;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Vehicle {
    private Integer vehicleId;
    private VehicleType vehicleType;
    private String uniqueBarcode;
    private String parkingStallNumber;
    private Double pricePerHour;
    private BookingStatus bookingStatus;
}
