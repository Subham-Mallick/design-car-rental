package org.example.LLD.oops.carrental;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehicleStationInfo {
    private Vehicle vehicle;
    private Station station;
}
