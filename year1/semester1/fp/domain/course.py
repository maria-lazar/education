class Course:
    def __init__(self, id, name, professor):
        self._id = id
        self._name = name
        self.professor = professor

    def __str__(self):
        return "id: " + str(self._id) + " name: " + self._name + " professor: " + self.professor

    def get_id(self):
        '''
        get_id -> int
        Returns the course id
        '''
        return self._id

    def get_name(self):
        '''
        get_name -> string
        Returns the course name
        '''
        return self._name

    def get_professor(self):
        '''
        get_professor -> string
        Returns the course professor
        '''
        return self.professor

