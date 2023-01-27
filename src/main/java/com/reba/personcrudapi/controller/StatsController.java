package com.reba.personcrudapi.controller;

import com.reba.personcrudapi.dto.StatsDto;
import com.reba.personcrudapi.model.Person;
import com.reba.personcrudapi.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private static final Logger logger = LoggerFactory.getLogger(StatsController.class);
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Autowired
    private PersonService personService;

    @GetMapping
    public ResponseEntity<List<StatsDto>> getStats() {

        logger.debug("Calculating stats");
        var stats = new ArrayList<StatsDto>();
        var people = personService.findAll();
        var peopleCount = people.size();
        if (peopleCount != 0) {
            var peopleByCountry = people.stream().collect(Collectors.groupingBy(Person::getCountry));
            peopleByCountry.forEach((k,v) ->
                    stats.add(StatsDto.builder().country(k).percentage(decimalFormat.format((double) v.size() * 100 / people.size())).build()));
        }

        return ResponseEntity.ok(stats);
    }
}
