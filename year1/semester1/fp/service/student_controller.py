from domain.student import Student
from domain.validator import StudentValidator


class StudentController:
    def __init__(self, repo_student):
        self.student_repository = repo_student
        self.validator = StudentValidator()

    def create(self, id, name):
        '''
        create -> Student
        Creates and returns a Student that has the given id and name
        :param id: int >= 0
        :param name: string
        '''
        student = Student(id, name)
        return student

    def add(self, id, name):
        '''
        add -> None
        Adds a Student to the list of students
        If the Student already exists or there is a validation error, raises StudentError
        :param id: int
        :param name: string
        '''
        student = self.create(id, name)
        self.validator.validate(student)
        self.student_repository.save(student)

    def get_all(self):
        '''
        get_all -> list of Students
        Returns the list of Students
        '''
        return self.student_repository.find_all()

    def search_by_id(self, id):
        '''
        search_by_id -> Student
        Returns the Student from the list of students that has the same id as the student given, if it exists,
        if not, raises StudentError
        :param id: int
        '''
        return self.student_repository.find(id)

    def search_by_name(self, name):
        '''
        search_by_name ->  list of Students
        Returns the list of students with the given name,
        raises StudentError if there isn't one
        :param name: string
        '''
        return self.student_repository.find_by_name(name)

    def search_for_generate(self, student):
        '''
        search_for_generate -> int
        Checks if the given student exists,
        if not returns -1
        :param student:
        '''
        return self.student_repository.find_index(student)
