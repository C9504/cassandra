package resources;


import entities.Fruit;
import entities.FruitDto;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import services.FruitService;

import java.util.List;
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
    public void add(FruitDto fruit) {
        fruitService.save(convertFromDto(fruit));
    }

    private FruitDto convertToDto(Fruit fruit) {
        return new FruitDto(fruit.getId(), fruit.getName(), fruit.getDescription());
    }

    private Fruit convertFromDto(FruitDto fruitDto) {
        return new Fruit(fruitDto.getId(), fruitDto.getName(), fruitDto.getDescription());
    }
}
