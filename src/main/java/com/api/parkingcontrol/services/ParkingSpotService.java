package com.api.parkingcontrol.services;

import com.api.parkingcontrol.dto.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotEntity;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParkingSpotService {

    final ParkingSpotRepository parkingSpotRepository;

    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional
    public ParkingSpotEntity save(ParkingSpotEntity parkingSpotEntity) {

        return parkingSpotRepository.save(parkingSpotEntity);
    }

    public boolean existsByLicensePlateCar(String licensePlateCar) {
        return parkingSpotRepository.existsByLicensePlateCar(licensePlateCar);
    }

    public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
        return parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber);
    }

    public boolean existsByApartmentAndBlock(String apartment, String block) {
        return parkingSpotRepository.existsByApartmentAndBlock(apartment, block);
    }

    public Page<ParkingSpotEntity> findAll(Pageable pageable) {
        return parkingSpotRepository.findAll(pageable);
    }

    public Optional<ParkingSpotEntity> findById(UUID id) {
        return parkingSpotRepository.findById(id);
    }

    @Transactional
    public void delete(ParkingSpotEntity parkingSpotEntity) {
        parkingSpotRepository.delete(parkingSpotEntity);
    }
}
