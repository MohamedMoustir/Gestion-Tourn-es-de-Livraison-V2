package com.livraison.demo.presentation.controller;

import com.livraison.demo.application.dto.TourDTO;
import com.livraison.demo.application.mapper.TourMapper;
import com.livraison.demo.application.service.TourService;
import com.livraison.demo.domain.entity.Tour;
import com.livraison.demo.domain.exception.TourExccdsException;
import com.livraison.demo.domain.exception.TourNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/tours")
public class TourController {

    private TourMapper tourMapper ;
    private TourService tourService ;

    public TourController(TourMapper tourMapper ,TourService tourService){
        this.tourMapper = tourMapper ;
        this.tourService = tourService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> create(@RequestBody TourDTO tourDTO){
        try{
            Tour tour =  this.tourMapper.toEntity(tourDTO);
            this.tourService.createTour(tour);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("success", "Tour created"));
        }catch (TourExccdsException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Une erreur interne est survenue : " + e.getMessage()));
        }

    }

    @GetMapping(produces=APPLICATION_JSON_VALUE)
    public ResponseEntity<?> recherchertour(){
        try{
            List<TourDTO> tours = this.tourService.recherchertour();
            return ResponseEntity.ok(tours);
        }catch(TourNotFoundException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Une erreur interne est survenue : " + e.getMessage()));
        }

    }

    @GetMapping(path="{id}",produces=APPLICATION_JSON_VALUE )
    public ResponseEntity<?>  getTourById(@PathVariable Long id){
        try{
            List<TourDTO> tour = Collections.singletonList(this.tourService.getTourById(id));
            return ResponseEntity.ok(tour);
        }catch(TourNotFoundException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Une erreur interne est survenue : " + e.getMessage()));
        }
    }

    @GetMapping(path="{id}/optimized",produces=APPLICATION_JSON_VALUE )
    public ResponseEntity<?> getOptimizedTour(@PathVariable Long id){
        try{
            List<Map<String, Object>> tours = this.tourService.getOptimizedTour(id);
            return ResponseEntity.ok(tours);
        }catch(TourNotFoundException e){
            ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error",e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Une erreur interne est survenue : " + e.getMessage()));
        }
        return null;
    }

    @DeleteMapping(path="{id}",produces=APPLICATION_JSON_VALUE )
    public ResponseEntity<?> deleteTourById(@PathVariable Long id){
        try{
            this.tourService.deleteTourById(id);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("success", "Suppression effectuée avec succès"));
        }catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Une erreur interne est survenue : " + e.getMessage()));
        }
    }

    @PutMapping(path="{id}",produces=APPLICATION_JSON_VALUE )
    public ResponseEntity<Map<String, String>> updateTour(@RequestBody Tour tour, @PathVariable Long id ){
        try {
            this.tourService.updateTour(tour, id);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("success", "Modification effectuée avec succès"));
        }catch (TourNotFoundException e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Une erreur sur modification des Tour : " + e.getMessage()));
        }
    }



}
