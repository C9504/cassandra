package entities;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;

import java.util.UUID;

@Entity(defaultKeyspace = "idtolu")
@PropertyStrategy(mutable = false)
public class Fruit {

    @PartitionKey
    private final UUID id;

    private final String name;

    private  final String description;

    public Fruit(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
