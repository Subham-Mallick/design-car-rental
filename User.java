package org.example.LLD.oops.carrental;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private Integer userId;
    private Integer locationPosition;
}
