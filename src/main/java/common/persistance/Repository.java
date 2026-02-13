package common.persistance;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {

    void create(T entity);
    Optional<T> getById(ID id);
    List<T> getAll();
    void modify(T entity);
    void remove(ID id);
}
