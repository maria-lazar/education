

namespace TripCommon.Validator
{
    public interface IValidator<E>
    {
        void Validate(E entity);
    }
}
