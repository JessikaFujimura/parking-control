package com.api.parkingcontrol;

import com.api.parkingcontrol.Exception.BusinessException;
import com.api.parkingcontrol.Exception.NotFoundException;
import com.api.parkingcontrol.controllers.ParkingSpotController;
import com.api.parkingcontrol.dto.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotEntity;
import com.api.parkingcontrol.services.ParkingSpotService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

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

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            parkingSpotController.getOneParkingSpot(id);
        }, "Parking Spot not found" );
        Assert.assertEquals(exception.getErrorMessages(), Map.of("Attention", List.of("Parking Spot not found", HttpStatus.NOT_FOUND.toString())));
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
        ParkingSpotDto dto = new ParkingSpotDto();
        dto.setParkingSpotNumber("number");
        dto.setLicensePlateCar("license");
        dto.setModelCar("model");
        dto.setResponsibleName("responsible");
        dto.setApartment("ape");
        dto.setBlock("B");
        dto.setBrandCar("brand");
        dto.setColorCar("black");

        ParkingSpotEntity ps1 = new ParkingSpotEntity();
        ps1.setId(UUID.randomUUID());
        ps1.setParkingSpotNumber("number");
        ps1.setResponsibleName("responsible");
        ps1.setColorCar("black");
        ps1.setModelCar("model");
        ps1.setApartment("ape");
        ps1.setLicensePlateCar("license");
        ps1.setRegistrationDate(LocalDateTime.now());
        ps1.setBlock("B");
        ps1.setBrandCar("brand");

        when(parkingSpotService.save(any(ParkingSpotEntity.class))).thenReturn(ps1);

        ResponseEntity<Object> response = parkingSpotController.createParkingSpot(dto);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void testCreateParkingSpotWithLicensePlateCarInUsed(){
        ParkingSpotDto dto = new ParkingSpotDto();
        dto.setParkingSpotNumber("number");
        dto.setLicensePlateCar("license");
        dto.setModelCar("model");
        dto.setResponsibleName("responsible");
        dto.setApartment("ape");
        dto.setBlock("B");
        dto.setBrandCar("brand");
        dto.setColorCar("black");

        Mockito.when(parkingSpotService.existsByLicensePlateCar("license")).thenReturn(true);

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            parkingSpotController.createParkingSpot(dto);
        }, "License Plate Car is already in use!" );
        Assert.assertEquals(exception.getErrorMessages(), Map.of("Conflict", List.of("License Plate Car is already in use!", HttpStatus.CONFLICT.toString())));
    }

    @Test
    public void testCreateParkingSpotWithParkingSpotNumberInUsed(){
        ParkingSpotDto dto = new ParkingSpotDto();
        dto.setParkingSpotNumber("number");
        dto.setLicensePlateCar("license");
        dto.setModelCar("model");
        dto.setResponsibleName("responsible");
        dto.setApartment("ape");
        dto.setBlock("B");
        dto.setBrandCar("brand");
        dto.setColorCar("black");

        Mockito.when(parkingSpotService.existsByParkingSpotNumber("number")).thenReturn(true);

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            parkingSpotController.createParkingSpot(dto);
        }, "Parking Spot is already in use!" );
        Assert.assertEquals(exception.getErrorMessages(), Map.of("Conflict", List.of("Parking Spot is already in use!", HttpStatus.CONFLICT.toString())));
    }

    @Test
    public void testCreateParkingSpotWithApartmentOrBlockInUsed(){
        ParkingSpotDto dto = new ParkingSpotDto();
        dto.setParkingSpotNumber("number");
        dto.setLicensePlateCar("license");
        dto.setModelCar("model");
        dto.setResponsibleName("responsible");
        dto.setApartment("ape");
        dto.setBlock("B");
        dto.setBrandCar("brand");
        dto.setColorCar("black");

        Mockito.when(parkingSpotService.existsByApartmentAndBlock("ape", "B")).thenReturn(true);

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            parkingSpotController.createParkingSpot(dto);
        }, "Parking Spot already registered for this apartment/block!" );
        Assert.assertEquals(exception.getErrorMessages(), Map.of("Conflict", List.of("Parking Spot already registered for this apartment/block!", HttpStatus.CONFLICT.toString())));
    }



}
