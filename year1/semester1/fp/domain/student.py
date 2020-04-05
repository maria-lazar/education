class Student:

    def __init__(self, id, name):
        self._id = id
        self._name = name

    def get_id(self):
        '''
        get_id -> int
        Returns the student id
        '''
        return self._id

    def get_name(self):
        '''
        get_name -> string
        Returns the student name
        '''
        return self._name

    def __str__(self):
        return "registration number: " + str(self._id) + " name: " + self._name
