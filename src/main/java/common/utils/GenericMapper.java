package common.utils;

    public interface GenericMapper <T, D> {
        public D toDocument(T t);
        public T toDomain(D d);
    }

