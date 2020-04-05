from domain import validator
from domain.validator import GradeValidator
from errors import StudentError, GradeError


def test_validate_name():
    name = "Maria"
    validator.validate_name(name)
    try:
        validator.validate_name("ana")
        assert False
    except StudentError:
        assert True
    try:
        validator.validate_name("An.a")
        assert False
    except StudentError:
        assert True

def test_validate_int():
    number = 10
    validator.validate_id(number)
    try:
        validator.validate_id(-10)
        assert False
    except ValueError:
        assert True

def test():
    test_validate_name()
    test_validate_int()
