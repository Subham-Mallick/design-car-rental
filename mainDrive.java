package org.example.LLD.oops.carrental;

import java.time.LocalDateTime;

public class mainDrive {

    public static void main(String[] args) {
        // Create some sample users
        User user1 = new User(1, 10);
        User user2 = new User(2, 20);

        // Create some sample vehicles
        Vehicle vehicle1 = new Vehicle(1, VehicleType.SUV, "B001", "P1", 11.0, BookingStatus.AVAILABLE);
        Vehicle vehicle2 = new Vehicle(2, VehicleType.BIKE, "B002", "P2", 12.0, BookingStatus.AVAILABLE);
        Vehicle vehicle3 = new Vehicle(3, VehicleType.SUV, "B003", "P3", 11.0, BookingStatus.BOOKED); // already booked

        // Create some stations and add vehicles
        Station station1 = new Station(1, 5);
        Station station2 = new Station(2, 15);

        station1.addVehicle(vehicle1);
        station1.addVehicle(vehicle2);
        station2.addVehicle(vehicle3); // this vehicle is booked

        // Initialize the rental portal with stations
        RentalPortal rentalPortal = new RentalPortal();
        rentalPortal.addStation(station1);
        rentalPortal.addStation(station2);

        // Find available vehicles of a specific type
        System.out.println("Available vehicles of type SUV:");
        rentalPortal.findByVehicleType(user1, VehicleType.SUV).forEach(vsi -> {
            System.out.println("Station: " + vsi.getStation().getStationId() + ", Vehicle ID: " + vsi.getVehicle().getVehicleId());
        });

        // Reserve a vehicle
        try {
            rentalPortal.reserveVehicle(user1, station1, vehicle1);
            System.out.println("Vehicle reserved successfully.");
        } catch (IllegalStateException e) {
            System.out.println("Failed to reserve vehicle: " + e.getMessage());
        }

        // Try to reserve a vehicle that is already booked
        try {
            rentalPortal.reserveVehicle(user1, station2, vehicle3); // This should fail
            System.out.println("Vehicle reserved successfully.");
        } catch (IllegalStateException e) {
            System.out.println("Failed to reserve vehicle: " + e.getMessage());
        }

        // Release the reservation
        try {
            ReservationInfo reservationInfo = ReservationInfo.builder()
                    .user(user1)
                    .vehicle(vehicle1)
                    .pickUpStation(station1)
                    .dropStation(station2)
                    .pickUpDate(LocalDateTime.now())
                    .reservationId(12345) // Assign a dummy reservation ID
                    .build();
            rentalPortal.releaseVehicle(reservationInfo);
            System.out.println("Vehicle released successfully.");
        } catch (Exception e) {
            System.out.println("Failed to release vehicle: " + e.getMessage());
        }
    }
}
