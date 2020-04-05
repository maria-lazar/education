import unittest

from domain.student import Student
from errors import StudentError
from repository.student_repository import StudentRepository
from service.student_controller import StudentController


class TestStudentController(unittest.TestCase):
    def setUp(self):
        students = StudentRepository()
        self.student1 = Student(1, "Maria")
        self.student2 = Student(2, "Alex")
        self.student3 = Student(3, "Maria")
        students.save(self.student1)
        students.save(self.student2)
        self.student_controller = StudentController(students)

    def test_get_all(self):
        self.assertEqual(self.student_controller.get_all()[0], self.student1)
        self.assertEqual(self.student_controller.get_all()[1], self.student2)

    def test_add(self):
        self.student_controller.add(self.student3.get_id(), self.student3.get_name())
        self.assertEqual(self.student_controller.get_all()[2].get_id(), self.student3.get_id())
        self.assertEqual(self.student_controller.get_all()[2].get_name(), self.student3.get_name())
        self.assertRaises(StudentError, lambda: self.student_controller.add(1, "Vlad"))

    def test_search_id(self):
        self.assertEqual(self.student_controller.search_by_id(1).get_name(), "Maria")
        self.assertRaises(StudentError, lambda: self.student_controller.search_by_id(4))

    def test_search_name(self):
        self.student_controller.add(self.student3.get_id(), self.student3.get_name())
        self.assertEqual(self.student_controller.search_by_name("Maria")[0].get_id(), 1)
        self.assertEqual(self.student_controller.search_by_name("Maria")[1].get_id(), 3)
        self.assertRaises(StudentError, lambda: self.student_controller.search_by_name("Ion"))