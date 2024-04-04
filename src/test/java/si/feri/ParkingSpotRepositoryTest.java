package si.feri;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import si.feri.parkinglot.ParkingSpotRepository;

import jakarta.inject.Inject;

@QuarkusTest
public class ParkingSpotRepositoryTest {

    @Inject
    ParkingSpotRepository parkingSpotRepository;

    @AfterEach
    void clean() {
        parkingSpotRepository.deleteAll().await().indefinitely();
    }

    @Test
    void testAdd() {
        parkingSpotRepository.add("test", "test").await().indefinitely();
        var spot = parkingSpotRepository.find("name", "test").firstResult().await().indefinitely();

        assert spot != null;
        assert spot.name.equals("test");
    }


    @Test
    void testListAllParkingSpots() {
        parkingSpotRepository.add("test", "test").await().indefinitely();
        var spots = parkingSpotRepository.listAllParkingSpots().await().indefinitely();

        assert spots.size() == 1;

    }
}
