package org.example.LLD.oops.carrental;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Station {
    private Integer stationId;
    private Integer locationPosition;
    private List<Vehicle> vehicleList = new ArrayList<>();
    private List<Vehicle> availableVehicleList = new ArrayList<>();
    private List<Vehicle> bookedVehicleList = new ArrayList<>();

    public Station(Integer stationId, Integer locationPosition) {
        this.locationPosition = locationPosition;
        this.stationId = stationId;
    }

    public void addVehicle(Vehicle vehicle) {
        availableVehicleList.add(vehicle);
        vehicleList.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) {
        vehicleList.remove(vehicle);
    }

    public void reserveVehicle(Vehicle toReserveVehicle) {
        vehicleList.stream()
                .filter(v ->
                        v.getVehicleId().equals(toReserveVehicle.getVehicleId())
                                && v.getBookingStatus().equals(BookingStatus.AVAILABLE))
                .findFirst().ifPresent(vehicle -> vehicle.setBookingStatus(BookingStatus.BOOKED));

        bookedVehicleList.add(toReserveVehicle);
    }

    public void releaseVehicle(Vehicle toReleaseVehicle) {
        vehicleList.stream()
                .filter(v ->
                        v.getVehicleId().equals(toReleaseVehicle.getVehicleId())
                                && v.getBookingStatus().equals(BookingStatus.BOOKED))
                .findFirst().ifPresent(vehicle -> vehicle.setBookingStatus(BookingStatus.AVAILABLE));

        availableVehicleList.add(toReleaseVehicle);

    }

    public void releaseVehicleAndRemove(Vehicle toReleaseVehicle) {
        vehicleList.stream()
                .filter(v ->
                        v.getVehicleId().equals(toReleaseVehicle.getVehicleId())
                                && v.getBookingStatus().equals(BookingStatus.BOOKED))
                .findFirst().ifPresent(vehicleList::remove);
        availableVehicleList.remove(toReleaseVehicle);

    }

}
