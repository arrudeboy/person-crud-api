package com.reba.personcrudapi.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reba.personcrudapi.model.validator.ValidDocumentType;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class PersonCreateDto {

    @ValidDocumentType
    private String documentType;

    @NotBlank
    private String documentNumber;

    @NotBlank
    private String country;

    @Min(value = 18, message = "A person must to be at least 18 y/o")
    private int age;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Valid
    @NotEmpty
    private List<ContactDto> contacts;
}
