import unittest

from domain.course import Course
from errors import CourseError
from repository.course_repository import CourseRepository


class TestCourseRepository(unittest.TestCase):
    def setUp(self):
        self.courses = CourseRepository()
        self.course1 = Course(1, "Asc", "Vancea")
        self.courses.save(self.course1)
        self.course2 = Course(2, "Fp", "Is")
        self.course3 = Course(3, "Logica", "Pop")
        self.course4 = Course(1, "L", "Pop")
        self.courses.save(self.course2)

    def test_save(self):
        self.courses.save(self.course3)
        self.assertEqual(self.courses.find_all()[2], self.course3)
        self.assertRaises(CourseError, lambda: self.courses.save(self.course4))

    def test_find_all(self):
        self.assertEqual(self.courses.find_all()[0], self.course1)
        self.assertEqual(self.courses.find_all()[1], self.course2)

    def test_update(self):
        self.courses.update(self.course4)
        self.assertEqual(self.courses.find_all()[0], self.course4)
        self.assertRaises(CourseError, lambda: self.courses.update(self.course3))

    def test_delete(self):
        self.courses.delete(1)
        self.assertEqual(self.courses.find_all()[0], self.course2)
        self.assertRaises(CourseError, lambda: self.courses.delete(4))

    def test_find_by_id(self):
        self.assertEqual(self.courses.find_by_id(1), self.course1)
        self.assertEqual(self.courses.find_by_id(5), -1)

    def test_find(self):
        self.assertEqual(self.courses.find(1), self.course1)
        self.assertRaises(CourseError, lambda: self.courses.find(5))

    def test_find_index(self):
        self.assertEqual(self.courses.find_index(self.course2), 1)
        self.assertEqual(self.courses.find_index(self.course3), -1)