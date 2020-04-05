from domain.course import Course
from domain.validator import CourseValidator
from errors import CourseError

class CourseRepository:
    def __init__(self):
        self.courses = []

    def find_all(self):
        '''
        find_all -> list of Courses
        Returns the list of Courses
        '''
        return self.courses

    def save(self, course):
        '''
        save -> None
        Searches if the given course id exists in the list of Courses, if not adds the course to the list
        If the id already exists, raises CourseError
        :param course: Course
        '''
        index = self.find_index(course)
        if index == -1:
            self.courses.append(course)
        else:
            raise CourseError("Duplicated id's")

    def update(self, course):
        '''
        update_course -> None
        Searches if the given course id exists in the list of Courses, if it exists, replaces the course from the list with the given course
        If the id doesn't exist, raises CourseError
        :param course: Course
        '''
        index = self.find_index(course)
        if index != -1:
            self.courses[index] = course
        else:
            raise CourseError("Course not found")

    def delete(self, id):
        '''
        delete -> None
        Searches if the given id exists in the list of Courses, if it exists, deletes the course with the given id
        If the id doesn't exist, raises CourseError
        :param id: int
        '''
        c = self.find_by_id(id)
        if c != -1:
            index = self.find_index(c)
            del self.courses[index]
        else:
            raise CourseError("Course not found")

    def find(self, id):
        '''
        find -> Course
        Returns the Course from the list of courses that has the same id as the one given if it exists,
        if not, raises CourseError
        :param id: int
        '''
        c = self.find_by_id(id)
        if c != -1:
            return c
        else:
            raise CourseError("Course not found")

    def find_by_id(self, id):
        '''
        find_id -> course or -1
        Returns Course from the list of courses that has the same course id as id,
        -1 if there isn't one
        :param id: int
        '''
        for i in range(0, len(self.courses)):
            if id == self.courses[i].get_id():
                return self.courses[i]
        return -1

    def find_by_professor(self, professor):
        '''
        find_by_professor -> list of courses
        Returns Course from the list of courses that has the same course professor as the one given,
        -1 if there isn't one
        :param professor: string
        '''
        courses = []
        for i in range(0, len(self.courses)):
            if professor == self.courses[i].get_professor():
                courses.append(self.courses[i])
        return courses

    def find_index(self, course):
        '''
            find_index ->  int
            Returns the position in the list of courses of the given course,
            -1 if there isn't one
            :param course: Course
        '''
        for i in range(0, len(self.courses)):
            if course.get_id() == self.courses[i].get_id():
                return i
        return -1

class CourseFileRepository(CourseRepository):

    def __init__(self):
        CourseRepository.__init__(self)
        self.validator = CourseValidator()
        self.courses = self.read_from_file()

    def read_from_file(self):
        '''
        read_from_file -> list of Courses
        Reads information about courses written in a file and puts it in a list,
        raises IOError if the file is corrupted
        '''
        courses = []
        try:
            f = open("data/courses", "r")
            line = f.readline()
            while line != "":
                object = line.split(",")
                course = Course(int(object[0]), object[1], object[2])
                for i in range(0, len(courses)):
                    if int(object[0]) == courses[i].get_id():
                        raise IOError
                self.validator.validate(course)
                courses.append(course)
                line = f.readline()
            f.close()
            return courses
        except IOError:
            raise ValueError("Cannot read courses")

    def save_to_file(self):
        '''
        save_to_file -> None
        Saves into a file information about the existing courses,
        raises IOError if the file is corrupted
        '''
        try:
            f = open("data/courses", "w")
            for i in range(0, len(self.courses)):
                s = str(self.courses[i].get_id()) + "," + self.courses[i].get_name() + "," + self.courses[
                    i].get_professor() + ",\n"
                f.write(s)
            f.close()
        except IOError:
            raise ValueError("Cannot save courses")

    def save(self, course):
        CourseRepository.save(self, course)
        self.save_to_file()

    def update(self, course):
        CourseRepository.update(self, course)
        self.save_to_file()

    def delete(self, id):
        CourseRepository.delete(self, id)
        self.save_to_file()

class RepoCourses:

    def __init__(self):
        self.validator = CourseValidator()

    def read_from_file(self):
        '''
        read_from_file -> list of Courses
        Reads information about courses written in a file and puts it in a list,
        raises IOError if the file is corrupted
        '''
        courses = []
        try:
            f = open("data/courses", "r")
            line = f.readline()
            while line != "":
                object = line.split(",")
                course = Course(int(object[0]), object[1], object[2])
                for i in range(0, len(courses)):
                    if int(object[0]) == courses[i].get_id():
                        raise IOError
                self.validator.validate(course)
                courses.append(course)
                line = f.readline()
            f.close()
            return courses
        except IOError:
            raise ValueError("Cannot read courses")

    def find_all(self):
        return self.read_from_file()

    def save(self, course_to_add):
        try:
            f = open("data/courses", "a+")
            for line in f:
                object = line.split(",")
                course = Course(int(object[0]), object[1], object[2])
                self.validator.validate(course)
                if course.get_id() == course_to_add.get_id():
                    raise CourseError("Duplicated Id's")
            s = str(course_to_add.get_id()) + "," + course_to_add.get_name() + "," + course_to_add.get_professor() + ",\n"
            f.write(s)
            f.close()
        except IOError:
            raise ValueError("Cannot read course data")

    def update(self, course_to_add):
        try:
            f = open("data/courses", "r")
            f1 = open("data/temp", "w")
            found = False
            for line in f:
                object = line.split(",")
                course = Course(int(object[0]), object[1], object[2])
                self.validator.validate(course)
                if course.get_id() == course_to_add.get_id():
                    s = str(course_to_add.get_id()) + "," + course_to_add.get_name() + "," + course_to_add.get_professor() + ",\n"
                    f1.write(s)
                    found = True
                else:
                    s = str(course.get_id()) + "," + course.get_name() + "," + course.get_professor() + ",\n"
                    f1.write(s)
            f.close()
            f1.close()
            if found == False:
                raise CourseError("Course not found")
            f1 = open("data/temp", "r")
            f = open("data/courses", "w")
            for line in f1:
                f.write(line)
            f.close()
            f1.close()
        except IOError:
            raise ValueError("Cannot read course data")

    def delete(self, id):
        try:
            f = open("data/courses", "r")
            f1 = open("data/temp", "w")
            found = False
            for line in f:
                object = line.split(",")
                course = Course(int(object[0]), object[1], object[2])
                self.validator.validate(course)
                if course.get_id() != id:
                    s = str(course.get_id()) + "," + course.get_name() + "," + course.get_professor() + ",\n"
                    f1.write(s)
                else:
                    found = True
            f.close()
            f1.close()
            if found == False:
                raise CourseError("Course not found")
            f1 = open("data/temp", "r")
            f = open("data/courses", "w")
            for line in f1:
                f.write(line)
            f.close()
            f1.close()
        except IOError:
            raise ValueError("Cannot read course data")

    def find_by_id(self, id):
        try:
            f = open("data/courses", "r")
            for line in f:
                object = line.split(",")
                course = Course(int(object[0]), object[1], object[2])
                self.validator.validate(course)
                if course.get_id() == id:
                    f.close()
                    return course
            return -1
        except IOError:
            raise ValueError("Cannot read course data")

    def find(self, id):
        course = self.find_by_id(id)
        if course != -1:
            return course
        else:
            raise CourseError("No course found")

    def find_index(self, course_to_add):
        try:
            f = open("data/courses", "r")
            index = 0
            for line in f:
                object = line.split(",")
                course = Course(int(object[0]), object[1], object[2])
                self.validator.validate(course)
                if course.get_id() == course_to_add.get_id():
                    f.close()
                    return index
                else:
                    index = index + 1
            return -1
        except IOError:
            raise ValueError("Cannot read course data")

    def find_by_professor(self, professor):
        try:
            f = open("data/courses", "r")
            courses = []
            for line in f:
                object = line.split(",")
                course = Course(int(object[0]), object[1], object[2])
                self.validator.validate(course)
                if course.get_professor() == professor:
                    courses.append(course)
            return courses
        except IOError:
            raise ValueError("Cannot read course data")


