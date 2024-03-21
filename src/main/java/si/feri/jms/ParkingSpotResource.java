package si.feri.jms;

import si.feri.jms.consumer.ParkingConsumer;
import si.feri.parkinglot.ParkingLotResource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
