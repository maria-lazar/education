class GradeDTO():

    def __init__(self, id_student, id_course, student_name, course_name, professor, grade):
        self.id_student = id_student
        self.id_course = id_course
        self.student_name = student_name
        self.course_name = course_name
        self.professor = professor
        self.grade = grade

    def __str__(self):
        return "Student: " + str(self.student_name) + " Course: " + str(self.course_name) + " Grade: " + str(self.grade)

    def get_id_student(self):
        return self.id_student

    def get_id_course(self):
        return self.id_course

    def get_student_name(self):
        return self.student_name

    def get_course_name(self):
        return self.course_name

    def get_professor(self):
        return self.professor

    def get_grade(self):
        return self.grade