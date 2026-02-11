package common.persistance;

import java.util.List;
import java.util.Optional;
/**
@param <T> entity type (Task, Note, Event, ...)
 @param <ID> identifier type (String, Long, Integer, ...)
 **/

public interface GenericDAO<T, ID> {

    void save(T entity);
    Optional<T> findByID(ID id);
    List<T> findAll();
    void update(T entity);
    void delete(ID id);
}
