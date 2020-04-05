import unittest

from domain.course import Course
from errors import CourseError
from repository.course_repository import CourseRepository
from service.course_controller import CourseController


class TestCourseController(unittest.TestCase):
    def setUp(self):
        courses = CourseRepository()
        self.course1 = Course(1, "Asc", "Vancea")
        self.course2 = Course(2, "Fp", "I")
        self.course3 = Course(3, "Logica", "Pop")
        courses.save(self.course1)
        courses.save(self.course2)
        self.course_controller = CourseController(courses)

    def test_get_all(self):
        self.assertEqual(self.course_controller.get_all()[0], self.course1)
        self.assertEqual(self.course_controller.get_all()[1], self.course2)

    def test_add(self):
        self.course_controller.add(self.course3.get_id(), self.course3.get_name(), self.course3.get_professor())
        self.assertEqual(self.course_controller.get_all()[2].get_id(), self.course3.get_id())
        self.assertEqual(self.course_controller.get_all()[2].get_name(), self.course3.get_name())
        self.assertEqual(self.course_controller.get_all()[2].get_professor(), self.course3.get_professor())
        self.assertRaises(CourseError, lambda: self.course_controller.add(1, "Algebra", "Modoi"))

    def test_search_id(self):
        self.assertEqual(self.course_controller.search_by_id(1).get_name(), "Asc")
        self.assertEqual(self.course_controller.search_by_id(1).get_professor(), "Vancea")
        self.assertRaises(CourseError, lambda: self.course_controller.search_by_id(4))