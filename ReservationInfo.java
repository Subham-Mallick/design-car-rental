package org.example.LLD.oops.carrental;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationInfo {
    private Integer reservationId;
    private User user;
    private Station pickUpStation;
    private Station dropStation;
    private Vehicle vehicle;
    private LocalDateTime pickUpDate;
    private LocalDateTime dropDate;
}
