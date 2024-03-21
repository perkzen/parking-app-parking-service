package si.feri.jms.producer;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import si.feri.parkinglot.ParkingSpotRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class ParkingProducer implements Runnable {

    @Inject
    ConnectionFactory connectionFactory;

    @Inject
    ParkingSpotRepository parkingSpotRepository;

    private final Random random = new Random();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    void onStart(@Observes StartupEvent ev) {
        scheduler.scheduleWithFixedDelay(this, 0L, 5L, TimeUnit.SECONDS);
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    @Override
    public void run() {
        try (JMSContext context = connectionFactory.createContext(JMSContext.AUTO_ACKNOWLEDGE)) {
            var parkingSpot = parkingSpotRepository.getRandomParkingSpot().await().indefinitely();

            var car = Integer.toString(random.nextInt(10));

            parkingSpotRepository.freeOrOccupy(parkingSpot.name, car).await().indefinitely();

            context.createProducer().send(context.createQueue("parking"), parkingSpot.name);
        }
    }
}
