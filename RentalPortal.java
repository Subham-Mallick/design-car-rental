package org.example.LLD.oops.carrental;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalPortal {
    private List<Station> stationList = new ArrayList<>();
    private Map<User, Set<ReservationInfo>> userReservationInfoMap = new HashMap<>();
    private Map<User, Set<ReservationInfo>> auditUserReservationInfoMap = new HashMap<>();
    private PriorityQueue<VehicleStationInfo> vehicleStationInfoQueue;

    public List<VehicleStationInfo> findByVehicleType(User user, VehicleType vehicleType) {
        List<VehicleStationInfo> results = new ArrayList<>();
        initializeVehicleStationInfoQueue(user);

        vehicleStationInfoQueue.forEach(vsi -> {
            if(vsi.getVehicle().getVehicleType().equals(vehicleType)) {
                results.add(vsi);
            }
        });

        return results;
    }

    private void initializeVehicleStationInfoQueue(User user) {
        Comparator<VehicleStationInfo> costAndProximityComparator = Comparator
                .comparingDouble((VehicleStationInfo vi) -> vi.getVehicle().getPricePerHour())
                .thenComparing((VehicleStationInfo vi1, VehicleStationInfo vi2) -> {
                    int distance1 = Math.abs(vi1.getStation().getLocationPosition() - user.getLocationPosition());
                    int distance2 = Math.abs(vi2.getStation().getLocationPosition() - user.getLocationPosition());
                    return Integer.compare(distance1, distance2);
                });

        vehicleStationInfoQueue = new PriorityQueue<>(costAndProximityComparator);

        for (Station station : stationList) {
            List<Vehicle> vehicleList = station.getVehicleList();
            for (Vehicle vehicle : vehicleList) {
                if(vehicle.getBookingStatus().equals(BookingStatus.AVAILABLE)) {
                    VehicleStationInfo vehicleStationInfo = VehicleStationInfo.builder()
                            .station(station)
                            .vehicle(vehicle).build();

                    vehicleStationInfoQueue.add(vehicleStationInfo);
                }
            }
        }
    }

    public void reserveVehicle(User user, Station station, Vehicle vehicle) {
        station.reserveVehicle(vehicle);
        ReservationInfo reservationInfo = ReservationInfo.builder()
                .user(user)
                .vehicle(vehicle)
                .pickUpDate(LocalDateTime.now())
                .reservationId(new Random().nextInt())
                .pickUpStation(station).build();

        userReservationInfoMap.computeIfAbsent(user, k -> new HashSet<>()).add(reservationInfo);
    }

    public void releaseVehicle(ReservationInfo reservationInfo) {
        Station pickUpStation = reservationInfo.getPickUpStation();
        Station dropStation = reservationInfo.getDropStation();
        Vehicle vehicle = reservationInfo.getVehicle();
        User user = reservationInfo.getUser();

        Set<ReservationInfo> existingReservationInfos = userReservationInfoMap.get(user);
        if(existingReservationInfos != null) {
            boolean removed = existingReservationInfos.remove(reservationInfo);
            if(removed){
                if(pickUpStation==dropStation){
                    pickUpStation.releaseVehicle(vehicle);
                } else {
                    vehicle.setBookingStatus(BookingStatus.AVAILABLE);
                    pickUpStation.releaseVehicleAndRemove(vehicle);
                    dropStation.addVehicle(vehicle);
                }

            }
            reservationInfo.setDropDate(LocalDateTime.now());
            auditUserReservationInfoMap.computeIfAbsent(user, k -> new HashSet<>()).add(reservationInfo);
        }

    }

    public void addStation(Station station) {
        stationList.add(station);
    }
}
