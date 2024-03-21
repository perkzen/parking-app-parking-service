package si.feri.parkinglot;

import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/park")
public class ParkingLotResource {

    private final Logger log = Logger.getLogger(ParkingLotResource.class.getName());

    @Inject
    ParkingSpotRepository repository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getAll() {
        log.info("Getting all parking spots");
        return repository.listAllParkingSpots().await().indefinitely().stream().map(ParkingSpot::getName).collect(Collectors.toList());
    }

    @POST
    public Uni<Void> add(CreateParkingSpotDto dto) {
        log.info("Adding parking spot: %s".formatted(dto));
        return repository.add(dto.name, dto.location);
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public ParkingSpot get(@PathParam("name") String name) {
        log.info("Getting parking spot: %s".formatted(name));
        return repository.getParkingSpot(name).await().indefinitely();
    }


}
