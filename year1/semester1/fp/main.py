from errors import StudentError
from repository.course_repository import CourseRepository, CourseFileRepository, RepoCourses
from repository.grade_repository import GradeRepository, GradeFileRepository
from repository.student_repository import StudentRepository, StudentFileRepository
from service.course_controller import CourseController
from service.grade_controller import GradeController
from service.student_controller import StudentController
from ui.ui import UI




def run():
    try:
        #course_repository = RepoCourses()
        course_repository = CourseFileRepository()
        #course_repository = CourseRepository()
        course_controller = CourseController(course_repository)
        student_repository = StudentFileRepository()
        #student_repository = StudentRepository()
        student_controller = StudentController(student_repository)
        #grade_repository = GradeRepository()
        grade_repository = GradeFileRepository(course_repository.find_all(), student_repository.find_all())
        grade_controller = GradeController(course_repository, student_repository, grade_repository)
        ui = UI(course_controller, student_controller, grade_controller)
        ui.run()
    except ValueError:
        print("Cannot read file data")
    except StudentError:
        print('Cannot read file data')


run()



