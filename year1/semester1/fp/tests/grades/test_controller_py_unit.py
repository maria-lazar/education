import unittest

from domain.course import Course
from domain.grade import Grade
from domain.student import Student
from errors import GradeError, CourseError, StudentError
from repository.course_repository import CourseRepository
from repository.grade_repository import GradeRepository
from repository.student_repository import StudentRepository
from service.grade_controller import GradeController


class TestGradeController(unittest.TestCase):
    def setUp(self):
        self.students = StudentRepository()
        courses = CourseRepository()
        grades = GradeRepository()
        self.course1 = Course(1, "Asc", "Vancea")
        self.student1 = Student(1, "Vlad")
        self.student2 = Student(2, "Alex")
        courses.save(self.course1)
        self.students.save(self.student1)
        self.students.save(self.student2)
        self.course2 = Course(2, "Fp", "I")
        courses.save(self.course2)
        self.grade1 = Grade(self.student1, self.course1, 10)
        self.grade2 = Grade(self.student1, self.course2, 4)
        grades.save(self.grade1)
        grades.save(self.grade2)
        self.grade_controller = GradeController(courses, self.students, grades)

    def test_get_all(self):
        self.assertEqual(self.grade_controller.get_all()[0].get_id_student(), self.grade1.get_student().get_id())
        self.assertEqual(self.grade_controller.get_all()[0].get_id_course(), self.grade1.get_course().get_id())
        self.assertEqual(self.grade_controller.get_all()[0].get_grade(), self.grade1.get_grade())
        self.assertEqual(self.grade_controller.get_all()[1].get_id_student(), self.grade2.get_student().get_id())
        self.assertEqual(self.grade_controller.get_all()[1].get_id_course(), self.grade2.get_course().get_id())
        self.assertEqual(self.grade_controller.get_all()[1].get_grade(), self.grade2.get_grade())

    def test_add(self):
        self.grade_controller.add(self.student2.get_id(), self.course1.get_id(), 10)
        self.assertEqual(self.grade_controller.get_all()[2].get_id_student(), self.student2.get_id())
        self.assertEqual(self.grade_controller.get_all()[2].get_id_course(), self.course1.get_id())
        self.assertEqual(self.grade_controller.get_all()[2].get_grade(), 10)
        self.assertRaises(GradeError, lambda: self.grade_controller.add(1, 1, 3.5))
        self.assertRaises(GradeError, lambda: self.grade_controller.add(10, 1, 3.5))
        self.assertRaises(GradeError, lambda: self.grade_controller.add(1, 10, 3.5))
        self.assertRaises(GradeError, lambda: self.grade_controller.add(1, 10, "a"))

    def test_update_course(self):
        self.grade_controller.update_course(1, "Analiza", "Berinde")
        self.assertEqual(self.grade_controller.get_all()[0].get_course_name(), "Analiza")
        self.assertEqual(self.grade_controller.get_all()[0].get_professor(), "Berinde")
        self.assertRaises(CourseError, lambda: self.grade_controller.update_course(10, "Analiza", "Berinde"))
        self.assertRaises(CourseError, lambda: self.grade_controller.update_course("a", "Analiza", "Berinde"))

    def test_update_student(self):
        self.grade_controller.update_student(1, "Berinde")
        self.assertEqual(self.grade_controller.get_all()[0].get_student_name(), "Berinde")
        self.assertRaises(StudentError, lambda: self.grade_controller.update_student(10, "Berinde"))
        self.assertRaises(StudentError, lambda: self.grade_controller.update_student("a", "Berinde"))
        self.assertRaises(StudentError, lambda: self.grade_controller.update_student(1, "berinde"))

    def test_update_grade(self):
        self.grade_controller.update_grade(1, 1, 9)
        self.assertEqual(self.grade_controller.get_all()[0].get_grade(), 9)
        self.assertRaises(GradeError, lambda: self.grade_controller.update_grade(10, 1, 10))
        self.assertRaises(GradeError, lambda: self.grade_controller.update_grade(1, 10, 10))
        self.assertRaises(GradeError, lambda: self.grade_controller.update_grade("a", 10, 10))
        self.assertRaises(GradeError, lambda: self.grade_controller.update_grade(1, 1, -10))

    def test_remove_course(self):
        self.grade_controller.remove_course(1)
        self.assertEqual(self.grade_controller.get_all()[0].get_grade(), 4)
        self.assertEqual(self.grade_controller.get_all()[0].get_id_student(), 1)
        self.assertEqual(self.grade_controller.get_all()[0].get_id_course(), 2)
        self.assertRaises(CourseError, lambda: self.grade_controller.remove_course(10))

    def test_remove_student(self):
        self.grade_controller.remove_student(1)
        self.assertEqual(self.grade_controller.get_all(), [])
        self.assertRaises(StudentError, lambda: self.grade_controller.remove_student(10))

    def test_remove_grade(self):
        self.grade_controller.remove_grade(1, 1)
        self.assertEqual(self.grade_controller.get_all()[0].get_id_student(), 1)
        self.assertEqual(self.grade_controller.get_all()[0].get_id_course(), 2)
        self.assertRaises(GradeError, lambda: self.grade_controller.remove_grade(10, 1))

    def test_best_students(self):
        student3 = Student(3, "Maria")
        self.students.save(student3)
        self.grade_controller.add(self.student2.get_id(), self.course1.get_id(), 10)
        self.grade_controller.add(self.student2.get_id(), self.course2.get_id(), 5)
        self.grade_controller.add(student3.get_id(), self.course1.get_id(), 7)
        self.grade_controller.add(student3.get_id(), self.course2.get_id(), 7)
        self.assertEqual(len(self.grade_controller.search_highest_grades()), 1)
        self.assertEqual(self.grade_controller.search_highest_grades()[0][0].get_id(), 2)
        self.assertEqual(self.grade_controller.search_highest_grades()[0][0].get_name(), "Alex")
        self.assertEqual(self.grade_controller.search_highest_grades()[0][1], 7.5)

    def test_failed_students(self):
        student3 = Student(3, "Maria")
        self.students.save(student3)
        self.grade_controller.add(self.student2.get_id(), self.course1.get_id(), 4)
        self.grade_controller.add(self.student2.get_id(), self.course2.get_id(), 5)
        self.grade_controller.add(student3.get_id(), self.course1.get_id(), 4)
        self.grade_controller.add(student3.get_id(), self.course2.get_id(), 7)
        self.assertEqual(self.grade_controller.failed_students()[0][0], "Vancea")
        self.assertEqual(self.grade_controller.failed_students()[1][0], "I")
        self.assertEqual(self.grade_controller.failed_students()[0][1], 2)
        self.assertEqual(self.grade_controller.failed_students()[1][1], 1)

    def test_search_course_grades(self):
        self.grade_controller.add(self.student2.get_id(), self.course1.get_id(), 4)
        self.assertEqual(self.grade_controller.search_course_grades(1)[1].get_id_student(), 1)
        self.assertEqual(self.grade_controller.search_course_grades(1)[0].get_student_name(), "Alex")
        self.assertEqual(self.grade_controller.search_course_grades(1)[1].get_student_name(), "Vlad")
        self.assertEqual(self.grade_controller.search_course_grades(1)[0].get_id_student(), 2)
        self.assertEqual(self.grade_controller.search_course_grades(4), [])

