package services;

import entities.Fruit;
import interfaces.FruitDao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class FruitService {

    @Inject
    FruitDao dao;

    public void save(Fruit fruit) {
        dao.save(fruit);
    }

    public List<Fruit> getAll() {
        return dao.findAll().all();
    }

    public List<Fruit> getByName(String name) {
        return StreamSupport.stream(dao.getByName(name).spliterator(), false).collect(Collectors.toList());
    }

    public Fruit getById(UUID id) {
        return dao.getById(id);
    }

    public void update(Fruit fruit) {
        dao.update(fruit);
    }

    public void delete(Fruit fruit) {
        dao.delete(fruit);
    }

}
