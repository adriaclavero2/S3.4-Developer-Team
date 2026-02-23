package common.persistance;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {

    T create(T entity);
    Optional<T> getById(ID id);
    List<T> getAll();
    T modify(T entity);
    void remove(ID id);
}
