package com.reba.personcrudapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatsDto {

    private String country;
    private String percentage;
}
