package resources;


import com.datastax.oss.driver.api.core.uuid.Uuids;
import entities.Fruit;
import entities.FruitDto;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import services.FruitService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/fruits")
public class FruitResource {

    @Inject
    FruitService fruitService;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<FruitDto> getAlList() {
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
}
