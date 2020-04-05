class ValidationError(ValueError):
    pass

class CourseError(ValidationError):
    pass

class StudentError(ValidationError):
    pass

class GradeError(ValidationError):
    pass