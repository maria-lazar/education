from errors import GradeError, ValidationError
from errors import StudentError

class CourseValidator:

    def validate(self, course):
        validate_id(course.get_id())
        validate_name(course.get_name())
        validate_name(course.get_professor())

class StudentValidator:

    def validate(self, student):
        validate_id(student.get_id())
        validate_name(student.get_name())

class GradeValidator:

    def validate(self, grade):
        errors = ""
        if grade.get_grade() < 0 or grade.get_grade() > 10:
            errors += "Grade must be a number between 0 and 10\n"
        if len(errors) > 0:
            raise GradeError(errors)

def validate_name(name):
    errors = ""
    if name == "":
        errors += "Name must be introduced"
    else:
        char = name[0]
        char = char.upper()
        if name[0] != char:
            errors += "Name must start with a capital letter\n"
        if not(name.isalpha()):
            errors += "Name must contain only letters\n"
        for i in range(1,len(name)):
            char = name[i]
            char = char.lower()
            if name[i] != char:
                errors += "Name letters must be lowercase(except the first)"
                break
    if len(errors) > 0:
        raise StudentError(errors)

def validate_id(id):
    errors = ""
    if id <= 0:
        errors += "Id must be a number greater than 0\n"
    if len(errors) > 0:
        raise ValidationError(errors)

