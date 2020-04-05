from domain.course import Course
from domain.validator import CourseValidator


class CourseController:
    def __init__(self, repo_course):
        self.course_repository = repo_course
        self.validator = CourseValidator()

    def get_all(self):
        '''
        get_all -> list of Courses
        Returns the list of Courses
        '''
        return self.course_repository.find_all()

    def add(self, id, name, professor):
        '''
        add -> None
        Adds a Course to the list of courses
        If the course already exists, raises CourseError
        :param course: Course
        '''
        course = self.create(id, name, professor)
        self.validator.validate(course)
        self.course_repository.save(course)

    def search_by_id(self, id):
        '''
        find -> Course
        Returns a Course with the same id as the course id given, if it exists
        If not raises CourseError
        :param id: int
        '''
        return self.course_repository.find(id)

    def create(self, id, name, professor):
        '''
        create -> Course
        Creates and returns a Course that has the given id, name, professor
        :param id: int >= 0
        :param name: string
        :param professor: string
        '''
        course = Course(id, name, professor)
        return course

    def search_for_generate(self, course):
        '''
        search_for_generate -> int
        Checks if the given course exists,
        if not returns -1
        :param course: Course
        '''
        return self.course_repository.find_index(course)
