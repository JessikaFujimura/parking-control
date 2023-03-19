package com.api.parkingcontrol.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParkingController {

    @GetMapping("/")
    public String getParking(){
        return "Hello World";
    }
}
