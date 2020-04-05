package validators;

public interface Validator<E> {
    /**
     * Validates the given entity
     * @param entity the entity to validate
     * @throws ValidationException if the entity is not valid
     */
    void validate(E entity) throws ValidationException;
}
