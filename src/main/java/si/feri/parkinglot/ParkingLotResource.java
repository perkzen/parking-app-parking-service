package si.feri.parkinglot;

import io.smallrye.mutiny.Uni;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.logging.Logger;

@Path("/park")
public class ParkingLotResource {

    private final Logger log = Logger.getLogger(ParkingLotResource.class.getName());

    @Inject
    ParkingSpotRepository repository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<ParkingSpot>> getAll() {
        log.info("Getting all parking spots");
        return repository.listAllParkingSpots();
    }

    @POST
    public Uni<Void> add(CreateParkingSpotDto dto) {
        log.info("Adding parking spot: %s".formatted(dto));
        return repository.add(dto.name, dto.location);
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ParkingSpot> get(@PathParam("name") String name) {
        log.info("Getting parking spot: %s".formatted(name));
        return repository.getParkingSpot(name);
    }


}
