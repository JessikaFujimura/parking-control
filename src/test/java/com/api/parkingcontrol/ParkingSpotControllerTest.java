package com.api.parkingcontrol;

import com.api.parkingcontrol.controllers.ParkingSpotController;
import com.api.parkingcontrol.dto.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotEntity;
import com.api.parkingcontrol.services.ParkingSpotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;


public class ParkingSpotControllerTest {

    @InjectMocks
    private ParkingSpotController parkingSpotController;

    @Mock
    private ParkingSpotService parkingSpotService;

    @Before
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOneParkingSpot(){
        UUID id = UUID.randomUUID();
        ParkingSpotEntity entity = new ParkingSpotEntity();
        entity.setId(id);
        entity.setParkingSpotNumber("1A");
        entity.setBrandCar("ford");
        entity.setModelCar("fiesta");
        entity.setColorCar("red");
        entity.setResponsibleName("Maria");
        entity.setLicensePlateCar("RRS8566");
        entity.setBlock("A");
        entity.setApartment("1305");

        Mockito.when(parkingSpotService.findById(any(UUID.class))).thenReturn(Optional.of(entity));

        ResponseEntity<Object> res = parkingSpotController.getOneParkingSpot(id);

        Assert.assertEquals(res.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(res.getBody());
    }

    @Test
    public void testGetOneParkingSpotWhenNotFound(){
        UUID id = UUID.randomUUID();
        Mockito.when(parkingSpotService.findById(any(UUID.class))).thenReturn(Optional.empty());
        ResponseEntity<Object> res = parkingSpotController.getOneParkingSpot(id);
        Assert.assertEquals(res.getStatusCode(), HttpStatus.NOT_FOUND);
        Assert.assertEquals(res.getBody(), "Parking Spot not found");
    }

}
