package si.feri.jms.consumer;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.Getter;
import si.feri.jms.producer.ParkingProducer;

import jakarta.jms.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@ApplicationScoped
public class ParkingConsumer implements Runnable {

    private final Logger log = Logger.getLogger(ParkingProducer.class.getName());


    @Inject
    ConnectionFactory connectionFactory;

    @Getter
    private volatile String lastParkingSpot;

    private final ExecutorService scheduler = Executors.newSingleThreadExecutor();

    void onStart(@Observes StartupEvent ev) {
        scheduler.submit(this);
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    @Override
    public void run() {
        try (JMSContext context = connectionFactory.createContext(JMSContext.AUTO_ACKNOWLEDGE)) {
            JMSConsumer consumer = context.createConsumer(context.createQueue("parking"));
            while (true) {
                Message message = consumer.receive();
                if (message == null) return;
                lastParkingSpot = message.getBody(String.class);
                log.info("last parking spot: %s".formatted(lastParkingSpot));
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
