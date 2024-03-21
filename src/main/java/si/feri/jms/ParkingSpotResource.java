package si.feri.jms;

import si.feri.jms.consumer.ParkingConsumer;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/park")
public class ParkingSpotResource {

    @Inject
    ParkingConsumer consumer;


    @GET
    @Path("last")
    @Produces(MediaType.TEXT_PLAIN)
    public String last() {
        return consumer.getLastParkingSpot();
    }
}
