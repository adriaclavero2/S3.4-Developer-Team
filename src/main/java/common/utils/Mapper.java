package common.utils;

public interface Mapper <T, D> {
    public D toDocument(T t);
    public T toDomain(D d);
}
