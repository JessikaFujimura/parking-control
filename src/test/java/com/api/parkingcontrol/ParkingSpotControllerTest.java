package com.api.parkingcontrol;

import com.api.parkingcontrol.controllers.ParkingSpotController;
import com.api.parkingcontrol.models.ParkingSpotEntity;
import com.api.parkingcontrol.services.ParkingSpotService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


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

    @Test
    public void testGetAllParkingSpot() {
        Pageable pageable = mock(Pageable.class);
        ParkingSpotEntity ps1 = new ParkingSpotEntity();
        ps1.setId(UUID.randomUUID());
        ps1.setParkingSpotNumber("number");
        ps1.setResponsibleName("responsible");
        ps1.setColorCar("black");
        ps1.setModelCar("model");
        ps1.setApartment("ape");
        ps1.setLicensePlateCar("license");
        List<ParkingSpotEntity> list = new ArrayList<>();
        list.add(ps1);
        Page<ParkingSpotEntity> page = new PageImpl<>(list);

        when(parkingSpotService.findAll(any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<ParkingSpotEntity>> response = parkingSpotController.getAllParkingSpot(pageable);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void testCreateParkingSpot(){

    }

}
