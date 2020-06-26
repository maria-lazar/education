package ubb.marial.tripsrest.tripserverrest.services;

import domain.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ubb.marial.tripsrest.tripserverrest.repository.TripRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/trips")
public class TripController {
    @Autowired
    private TripRepository tripRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Trip> getAll() {
        return tripRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable int id) {
        Trip trip = tripRepository.findOne(id);
        if (trip == null)
            return new ResponseEntity<String>("Trip not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Trip>(trip, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Trip save(@RequestBody Trip trip) {
        trip.setId(null);
        return tripRepository.save(trip);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Trip trip) {
        if (tripRepository.findOne(id) == null) {
            return new ResponseEntity<String>("Trip doesn't exist", HttpStatus.BAD_REQUEST);
        }
        Trip t = tripRepository.save(trip);
        return new ResponseEntity<Trip>(t, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int id) {
        Trip trip = tripRepository.delete(id);
        return new ResponseEntity<Trip>(trip, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String tripError(RuntimeException e) {
        return e.getMessage();
    }
}
