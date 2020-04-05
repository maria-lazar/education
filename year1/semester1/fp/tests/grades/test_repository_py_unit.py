import unittest

from domain.course import Course
from domain.grade import Grade
from domain.student import Student
from errors import StudentError, GradeError
from repository.grade_repository import GradeRepository


class TestCourseRepository(unittest.TestCase):
    def setUp(self):
        self.grades = GradeRepository()
        self.course1 = Course(1, "Asc", "Vancea")
        self.student1 = Student(1, "Vlad")
        self.student2 = Student(2, "Ion")
        self.course2 = Course(2, "Fp", "I")
        self.grade1 = Grade(self.student1, self.course1, 10)
        self.grade2 = Grade(self.student1, self.course2, 4)
        self.grade3 = Grade(self.student2, self.course1, 7)
        self.grade4 = Grade(self.student1, self.course1, 8)
        self.grades.save(self.grade1)
        self.grades.save(self.grade2)

    def test_save(self):
        self.grades.save(self.grade3)
        self.assertEqual(self.grades.find_all()[2].get_id_student(), self.grade3.get_student().get_id())
        self.assertEqual(self.grades.find_all()[2].get_id_course(), self.grade3.get_course().get_id())
        self.assertEqual(self.grades.find_all()[2].get_grade(), self.grade3.get_grade())
        self.assertRaises(GradeError, lambda: self.grades.save(self.grade4))

    def test_find_all(self):
        self.assertEqual(self.grades.find_all()[0].get_id_student(), self.grade1.get_student().get_id())
        self.assertEqual(self.grades.find_all()[0].get_id_course(), self.grade1.get_course().get_id())
        self.assertEqual(self.grades.find_all()[0].get_grade(), self.grade1.get_grade())
        self.assertEqual(self.grades.find_all()[1].get_id_student(), self.grade2.get_student().get_id())
        self.assertEqual(self.grades.find_all()[1].get_id_course(), self.grade2.get_course().get_id())
        self.assertEqual(self.grades.find_all()[1].get_grade(), self.grade2.get_grade())

    def test_update(self):
        self.grades.update(self.grade4)
        self.assertEqual(self.grades.find_all()[0].get_id_student(), self.grade4.get_student().get_id())
        self.assertEqual(self.grades.find_all()[0].get_id_course(), self.grade4.get_course().get_id())
        self.assertEqual(self.grades.find_all()[0].get_grade(), self.grade4.get_grade())
        self.assertRaises(GradeError, lambda: self.grades.update(self.grade3))

    def test_delete(self):
        self.grades.delete(1, 1)
        self.assertEqual(self.grades.find_all()[0].get_id_student(), self.grade2.get_student().get_id())
        self.assertEqual(self.grades.find_all()[0].get_id_course(), self.grade2.get_course().get_id())
        self.assertEqual(self.grades.find_all()[0].get_grade(), self.grade2.get_grade())
        self.assertRaises(GradeError, lambda: self.grades.delete(4, 1))
        self.assertRaises(GradeError, lambda: self.grades.delete(1, 4))

    def test_find_by_id(self):
        self.assertEqual(self.grades.find_by_id(1, 1), self.grade1)
        self.assertEqual(self.grades.find_by_id(5, 1), -1)

    def test_find_index(self):
        self.assertEqual(self.grades.find_index(self.grade2), 1)
        self.assertEqual(self.grades.find_index(self.grade3), -1)

    def test_find_by_student(self):
        self.assertEqual(self.grades.find_by_student(1)[0].get_id_course(), self.course1.get_id())
        self.assertEqual(self.grades.find_by_student(1)[0].get_course_name(), self.course1.get_name())
        self.assertEqual(self.grades.find_by_student(1)[0].get_grade(), 10)
        self.assertEqual(self.grades.find_by_student(1)[1].get_id_course(), self.course2.get_id())
        self.assertEqual(self.grades.find_by_student(1)[1].get_course_name(), self.course2.get_name())
        self.assertEqual(self.grades.find_by_student(1)[1].get_grade(), 4)
        self.assertEqual(self.grades.find_by_student(5), [])
        repo = GradeRepository()
        self.assertEqual(repo.find_by_student(1), [])

    def test_find_by_course(self):
        self.grades.save(self.grade3)
        self.assertEqual(self.grades.find_by_course(1)[0].get_id_student(), self.student1.get_id())
        self.assertEqual(self.grades.find_by_course(1)[1].get_id_student(), self.student2.get_id())
        self.assertEqual(self.grades.find_by_course(1)[0].get_student_name(), self.student1.get_name())
        self.assertEqual(self.grades.find_by_course(1)[1].get_student_name(), self.student2.get_name())
        self.assertEqual(self.grades.find_by_course(1)[0].get_grade(), 10)
        self.assertEqual(self.grades.find_by_course(1)[1].get_grade(), 7)
        self.assertEqual(self.grades.find_by_course(5), [])
