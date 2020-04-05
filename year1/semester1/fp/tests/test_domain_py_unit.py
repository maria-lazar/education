import unittest

from domain.course import Course
from domain.grade import Grade
from domain.student import Student
from domain.validator import CourseValidator, StudentValidator, GradeValidator
from errors import ValidationError


class TestCourse(unittest.TestCase):
    def setUp(self):
        self.course = Course(10, "Asc", "Vancea")
        self.invalid_course = Course(-1, "", "v")
        self.validator_c = CourseValidator()

    def test_course(self):
        self.validator_c.validate(self.course)
        self.assertRaises(ValidationError, lambda: self.validator_c.validate(self.invalid_course))

class TestStudent(unittest.TestCase):
    def setUp(self):
        self.student = Student(10, "Vlad")
        self.invalid_student = Student(-1, "V")
        self.invalid_student2 = Student(1, "v")
        self.validator_s = StudentValidator()

    def test_student(self):
        self.validator_s.validate(self.student)
        self.assertRaises(ValidationError, lambda: self.validator_s.validate(self.invalid_student))
        self.assertRaises(ValidationError, lambda: self.validator_s.validate(self.invalid_student2))

class TestGrade(unittest.TestCase):
    def setUp(self):
        self.student = Student(1, "Vlad")
        self.course = Course(10, "Asc", "Vancea")
        self.grade = Grade(self.student, self.course, 10)
        self.invalid_grade = Grade(self.student, self.course, 30)
        self.invalid_grade2 = Grade(self.student, self.course, -30)
        self.validator_g = GradeValidator()

    def test_grade(self):
        self.validator_g.validate(self.grade)
        self.assertRaises(ValidationError, lambda: self.validator_g.validate(self.invalid_grade))
        self.assertRaises(ValidationError, lambda: self.validator_g.validate(self.invalid_grade2))

suite = unittest.TestSuite()
suite.addTest(TestCourse('test_course'))
suite.addTest(TestStudent('test_student'))
suite.addTest(TestGrade('test_grade'))
