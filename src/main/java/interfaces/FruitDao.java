package interfaces;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.datastax.oss.driver.api.mapper.annotations.Update;
import entities.Fruit;

@Dao
public interface FruitDao {

    @Update
    void update(Fruit fruit);

    @Select
    PagingIterable<Fruit> findAll();

    @Select(customWhereClause = "name = :name", allowFiltering = true)
    PagingIterable<Fruit> getByName(@CqlName("name") String name);

}
