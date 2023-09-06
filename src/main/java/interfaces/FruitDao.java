package interfaces;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.*;
import entities.Fruit;

import java.util.UUID;

@Dao
public interface FruitDao {

    @Update
    void save(Fruit fruit);

    @Select
    PagingIterable<Fruit> findAll();

    @Select(customWhereClause = "name = :name", allowFiltering = true)
    PagingIterable<Fruit> getByName(@CqlName("name") String name);

    @Select(customWhereClause = "id = :id", allowFiltering = true)
    Fruit getById(@CqlName("id") UUID id);

    @Update
    void update(Fruit fruit);

    @Delete
    void delete(Fruit fruit);

}
