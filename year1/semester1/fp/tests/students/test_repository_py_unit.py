import unittest

from domain.student import Student
from errors import StudentError
from repository.student_repository import StudentRepository


class TestCourseRepository(unittest.TestCase):
    def setUp(self):
        self.students = StudentRepository()
        self.student1 = Student(1, "Alex")
        self.students.save(self.student1)
        self.student2 = Student(2, "Filip")
        self.student3 = Student(3, "Paul")
        self.student4 = Student(1, "Luca")
        self.students.save(self.student2)

    def test_save(self):
        self.students.save(self.student3)
        self.assertEqual(self.students.find_all()[2], self.student3)
        self.assertRaises(StudentError, lambda: self.students.save(self.student4))

    def test_find_all(self):
        self.assertEqual(self.students.find_all()[0], self.student1)
        self.assertEqual(self.students.find_all()[1], self.student2)

    def test_update(self):
        self.students.update(self.student4)
        self.assertEqual(self.students.find_all()[0], self.student4)
        self.assertRaises(StudentError, lambda: self.students.update(self.student3))

    def test_delete(self):
        self.students.delete(1)
        self.assertEqual(self.students.find_all()[0], self.student2)
        self.assertRaises(StudentError, lambda: self.students.delete(4))

    def test_find_by_id(self):
        self.assertEqual(self.students.find_by_id(1), self.student1)
        self.assertEqual(self.students.find_by_id(5), -1)

    def test_find(self):
        self.assertEqual(self.students.find(1), self.student1)
        self.assertRaises(StudentError, lambda: self.students.find(5))

    def test_find_index(self):
        self.assertEqual(self.students.find_index(self.student2), 1)
        self.assertEqual(self.students.find_index(self.student3), -1)

    def test_find_name(self):
        student = Student(5, "Alex")
        self.students.save(student)
        self.assertEqual(self.students.find_by_name("Alex")[0], self.student1 )
        self.assertEqual(self.students.find_by_name("Alex")[1], student )
        self.assertRaises(StudentError, lambda: self.students.find_by_name("Maria"))