package resources;


import com.datastax.oss.driver.api.core.uuid.Uuids;
import entities.Fruit;
import entities.FruitDto;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.jboss.logging.Logger;
import services.FruitService;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Path("/fruits")
public class FruitResource {

    private static final Logger LOGGER = Logger.getLogger(FruitResource.class);

    private AtomicLong counter = new AtomicLong(0);

    @Inject
    FruitService fruitService;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(250)
    @Retry(maxRetries = 4)
    @Fallback(fallbackMethod = "fallbackGetAllList")
    @CircuitBreaker(requestVolumeThreshold = 4)
    public List<FruitDto> getAllList() {
        final Long invocationNumber = counter.getAndIncrement();
        maybeFail();
        maybeFail(String.format("FruitResource#getAllList() invocation #%d failed", invocationNumber));

        LOGGER.infof("FruitResource#getAllList() invocation #%d returning successfully", invocationNumber);
        return fruitService.getAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<FruitDto> fallbackGetAllList() {
        LOGGER.infof("Falling back to FruitResource#fallbackGetAllList()");
        // safe bet, return something that everybody likes
        return fruitService.getAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void create(FruitDto fruit) {
        fruitService.save(convertFromDto(fruit));
    }

    @GET
    @Path("/name/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<FruitDto> getByName(@PathParam("name") String name) {
        return fruitService.getByName(name).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Fruit getById(@PathParam("id") UUID id) {
        return fruitService.getById(id);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") UUID id, FruitDto fruitDto) {
        Fruit fruit = fruitService.getById(id);
        if (fruit != null) {
            fruitService.update(convertFromToDtoToUpdate(fruitDto));
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") UUID id) {
        Fruit fruit = fruitService.getById(id);
        if (fruit != null) {
            fruitService.delete(fruit);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    private FruitDto convertToDto(Fruit fruit) {
        return new FruitDto(fruit.getId(), fruit.getName(), fruit.getDescription());
    }

    private Fruit convertFromDto(FruitDto fruitDto) {
        return new Fruit(Uuids.random(), fruitDto.getName(), fruitDto.getDescription());
    }

    private Fruit convertFromToDtoToUpdate(FruitDto fruitDto) {
        return new Fruit(fruitDto.getId(), fruitDto.getName(), fruitDto.getDescription());
    }

    private void maybeFail(String failureLogMessage) {
        if (new Random().nextBoolean()) {
            LOGGER.error(failureLogMessage);
            throw new RuntimeException("Resource failure.");
        }
    }

    private void maybeFail() {
        // introduce some artificial failures
        final Long invocationNumber = counter.getAndIncrement();
        if (invocationNumber % 4 > 1) { // alternate 2 successful and 2 failing invocations
            throw new RuntimeException("Service failed.");
        }
    }
}
