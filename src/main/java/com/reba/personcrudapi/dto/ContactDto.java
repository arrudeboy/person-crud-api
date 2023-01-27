package com.reba.personcrudapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reba.personcrudapi.model.validator.ValidContact;
import lombok.*;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;

@GroupSequence({ContactDto.class, ContactDto.HighLevelCoherence.class})
@ValidContact(groups = ContactDto.HighLevelCoherence.class)
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class ContactDto {

    @NotBlank
    private String type;

    @NotBlank
    private String value;

    public interface HighLevelCoherence {}
}
