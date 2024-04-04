package si.feri.jms.producer;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import si.feri.parkinglot.ParkingSpot;
import si.feri.parkinglot.ParkingSpotRepository;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.jms.*;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@ApplicationScoped
public class ParkingProducer implements Runnable {

    private final Logger log = Logger.getLogger(ParkingProducer.class.getName());

    @Inject
    ConnectionFactory connectionFactory;

    @Inject
    ParkingSpotRepository parkingSpotRepository;

    private final Random random = new Random();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    void onStart(@Observes StartupEvent ev) {
        scheduler.scheduleWithFixedDelay(this, 0L, 10L, TimeUnit.SECONDS);
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    @Override
    public void run() {
        try (JMSContext context = connectionFactory.createContext(JMSContext.AUTO_ACKNOWLEDGE)) {
            ParkingSpot parkingSpot = parkingSpotRepository.listAllParkingSpots().await().indefinitely().get(random.nextInt(3));

            context.createProducer().send(context.createQueue("parking"), parkingSpot.getName());
            log.info("sent parking spot: %s".formatted(parkingSpot.getName()));
        } catch (Exception e) {
            log.severe(e.getMessage());
        }
    }
}
