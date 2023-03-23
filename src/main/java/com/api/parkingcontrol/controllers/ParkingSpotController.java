package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.Exception.BusinessException;
import com.api.parkingcontrol.Exception.NotFoundException;
import com.api.parkingcontrol.dto.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotEntity;
import com.api.parkingcontrol.services.ParkingSpotService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }


    @PostMapping
    public ResponseEntity<Object> createParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto) throws BusinessException {
        if (parkingSpotService.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar()))
            throw new BusinessException(Map.of("Conflict", List.of("License Plate Car is already in use!", HttpStatus.CONFLICT.toString())));
        if (parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber()))
            throw new BusinessException(Map.of("Conflict", List.of("Parking Spot is already in use!", HttpStatus.CONFLICT.toString())));
        if (parkingSpotService.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock()))
            throw new BusinessException(Map.of("Conflict", List.of("Parking Spot already registered for this apartment/block!", HttpStatus.CONFLICT.toString())));
        var parkingSpotModel = new ParkingSpotEntity();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
    }


    @GetMapping
    public ResponseEntity<Page<ParkingSpotEntity>> getAllParkingSpot(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable
            ) {
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") UUID id){
        Optional<ParkingSpotEntity> parkingSpotEntityOptional = parkingSpotService.findById(id);
        if(parkingSpotEntityOptional.isEmpty())
            throw new NotFoundException(Map.of("Attention", List.of("Parking Spot not found", HttpStatus.NOT_FOUND.toString())));
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotEntityOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id){
        Optional<ParkingSpotEntity> parkingSpotEntityOptional = parkingSpotService.findById(id);
        if(parkingSpotEntityOptional.isEmpty())
            throw new NotFoundException(Map.of("Attention", List.of("Parking Spot not found", HttpStatus.NOT_FOUND.toString())));

        parkingSpotService.delete(parkingSpotEntityOptional.get());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Parking Spot deleted successfully!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id ,
                                                    @RequestBody ParkingSpotDto parkingSpotDto) {
        Optional<ParkingSpotEntity> parkingSpotEntityOptional = parkingSpotService.findById(id);
        if(parkingSpotEntityOptional.isEmpty())
            throw new NotFoundException(Map.of("Attention", List.of("Parking Spot not found", HttpStatus.NOT_FOUND.toString())));
        var parkingSpotEntity = parkingSpotEntityOptional.get();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotEntity);
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotEntity));
    }


}