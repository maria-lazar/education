package validator;

interface Validator<E> {
    void validate(E entity);
}
