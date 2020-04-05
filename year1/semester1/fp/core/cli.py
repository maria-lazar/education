from errors import CourseError, StudentError, GradeError


class MenuItem:
    def __init__(self, command, description, action):
        self.command = command
        self.description = description
        self.action = action

    def __str__(self):
        return self.command + " " + self.description

class Menu:
    def __init__(self, name):
        self.name = name
        self.items = []

    def add(self, command, description, action):
        '''
        add -> None
        Creates and appends a MenuItem to the list of MenuItems(items)
        :param command: string
        :param description: string
        :param action: function
        '''
        menu_item = MenuItem(command, description, action)
        self.items.append(menu_item)

    def print_menu(self):
        for i in range(0,len(self.items)):
            print(str(i + 1) + " - " + str(self.items[i]))

    def find_item(self, command):
        '''
        find_item -> MenuItem or None
        Returns the MenuItem with the specific command from the list of MenuItems(items), None if there isn't one
        :param command: string
        '''
        for i in range(0, len(self.items)):
            if self.items[i].command == command:
                return self.items[i]
        return None

    def run(self):
        self.print_menu()
        command = raw_input(">")
        while command != "exit":
            menu_item = self.find_item(command)
            if menu_item != None:
                try:
                    menu_item.action()
                except ValueError as ve:
                    print(ve.message)
                except CourseError as ce:
                    print(ce)
                except StudentError as se:
                    print(se)
                except GradeError as ge:
                    print(ge)
            else:
                print("Unknown command")
            self.print_menu()
            command = raw_input(">")

    def __str__(self):
        return self.name

class Cli:
    def __init__(self):
        self.menus = []

    def find_menu(self, name):
        '''
        find_menu -> Menu or None
        Returns the object Menu from the list of Menus(menus) that has the specific name, if there isn't one,
        returns None
        :param name: string
        '''
        for i in range(0,len(self.menus)):
            if self.menus[i].name == name:
                return self.menus[i]
        return None

    def run(self):
        self.print_menu()
        command = raw_input(">")
        while command != "exit":
            menu = self.find_menu(command)
            if menu != None:
                menu.run()
            else:
                print("Unknown command")
            self.print_menu()
            command = raw_input(">")

    def add_menu(self, menu_name):
        '''
        add_menu -> Menu
        Creates and returns the Menu with the given name(menu_name), appends the menu to the list of menus
        :param menu_name: string
        '''
        menu = Menu(menu_name)
        self.menus.append(menu)
        return menu

    def print_menu(self):
        for i in range(0,len(self.menus)):
            print(str(i + 1) + ". " + str(self.menus[i]))
