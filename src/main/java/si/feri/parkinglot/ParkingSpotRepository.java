package si.feri.parkinglot;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;


@ApplicationScoped
public class ParkingSpotRepository implements ReactivePanacheMongoRepository<ParkingSpot> {


    public Uni<Void> add(String name, String location) {
        ParkingSpot parkingSpot = new ParkingSpot();
        parkingSpot.name = name;
        parkingSpot.location = location;
        parkingSpot.occupied = false;
        return persist(parkingSpot).replaceWithVoid();
    }
//
//    public Uni<Void> occupy(String name, String car) {
//        ParkingSpot parkingSpot = (ParkingSpot) find("name", name).firstResult();
//        parkingSpot.occupied = true;
//        parkingSpot.car = car;
//        parkingSpot.occupiedAt = LocalDateTime.now();
//        return persist(parkingSpot).replaceWithVoid();
//    }
//
//    public Uni<Void> free(String name) {
//        ParkingSpot parkingSpot = (ParkingSpot) find("name", name).firstResult();
//        parkingSpot.occupied = false;
//        parkingSpot.car = null;
//        parkingSpot.occupiedAt = null;
//        return persist(parkingSpot).replaceWithVoid();
//
//    }

    public Uni<List<ParkingSpot>> listAllParkingSpots() {
        return findAll().list();
    }

    public Uni<ParkingSpot> getParkingSpot(String name) {
        return find("name", name).firstResult();
    }
}
