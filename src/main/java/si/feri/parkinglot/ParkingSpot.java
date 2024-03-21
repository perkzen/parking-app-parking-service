package si.feri.parkinglot;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpot extends ReactivePanacheMongoEntity {
    public String name;
    public String location;
    public Boolean occupied;
    public String car;
    public LocalDateTime occupiedAt;
}
