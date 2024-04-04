package si.feri.jms;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import si.feri.jms.consumer.ParkingConsumer;
import si.feri.parkinglot.ParkingLotResource;


import java.util.logging.Logger;

@Path("/park")
public class ParkingSpotResource {

    private final Logger log = Logger.getLogger(ParkingLotResource.class.getName());

    @Inject
    ParkingConsumer consumer;


    @GET
    @Path("last")
    @Produces(MediaType.TEXT_PLAIN)
    public String last() {
        log.info("Getting last parking spot");
        return consumer.getLastParkingSpot();
    }
}
