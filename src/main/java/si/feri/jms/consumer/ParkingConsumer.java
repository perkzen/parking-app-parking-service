package si.feri.jms.consumer;

import io.quarkus.logging.Log;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import lombok.Getter;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class ParkingConsumer implements Runnable {

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
                Log.info("last parking spot: %s".formatted(lastParkingSpot));
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
