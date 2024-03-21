package si.feri.parkinglot;

import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/park")
public class ParkingLotResource {

    @Inject
    ParkingSpotRepository repository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getAll() {
        return repository.listAllParkingSpots().await().indefinitely().stream().map(ParkingSpot::getName).collect(Collectors.toList());
    }

    @POST
    public Uni<Void> add(CreateParkingSpotDto dto) {
        return repository.add(dto.name, dto.location);
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public ParkingSpot get(@PathParam("name") String name) {
        return repository.getParkingSpot(name).await().indefinitely();
    }


}
