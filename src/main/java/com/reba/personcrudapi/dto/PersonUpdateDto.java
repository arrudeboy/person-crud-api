package com.reba.personcrudapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reba.personcrudapi.model.validator.ValidDocumentType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class PersonUpdateDto {

    @ValidDocumentType
    private String documentType;

    @NotBlank
    private String documentNumber;

    @NotBlank
    private String country;

    @Min(value = 18, message = "must have at least 18 y/o")
    private int age;

    private String firstName;

    private String lastName;
}
