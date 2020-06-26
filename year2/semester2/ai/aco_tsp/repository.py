from city import City
from edge import Edge


class Repository:
    def __init__(self, file_name, e=False):
        self.__graph = {}
        self.__aco_params = {}
        if not e:
            # problem with graph
            self.load_from_file(file_name)
        else:
            # problem with coordinates
            self.load_from_file_e(file_name)

    def load_from_file(self, file_name):
        f = open(file_name, "r")
        n = int(f.readline())
        self.__graph['num_nodes'] = n
        mat = []
        for i in range(n):
            mat.append([])
            line = f.readline()
            elements = line.split(",")
            for j in range(n):
                mat[-1].append(int(elements[j]))
        initial_pheromone = float(f.readline())
        # matrix with all edges
        edges = [[None] * n for _ in range(n)]
        for i in range(n):
            for j in range(i + 1, n):
                edges[i][j] = edges[j][i] = Edge(i, j, initial_pheromone, mat[i][j])
        self.__graph['edges'] = edges
        size = int(f.readline())
        gen = int(f.readline())
        rho = float(f.readline())
        phe_dep = float(f.readline())
        alpha = float(f.readline())
        beta = float(f.readline())
        self.__aco_params = {'col_size': size, 'gen': gen, 'initial_pheromone': initial_pheromone,
                             'rho': rho, 'pheromone_deposit_weight': phe_dep,
                             'alpha': alpha, 'beta': beta
                             }
        f.close()

    @property
    def graph(self):
        return self.__graph

    def write_to_file(self, file_name, solution, distance):
        f = open(file_name, "w")
        result = ''
        result += str(distance) + "\n"
        for i in solution:
            result += str(i + 1) + " "
        f.writelines(result)
        f.close()

    def get_aco_params(self):
        return self.__aco_params

    def load_from_file_e(self, file_name):
        f = open(file_name, "r")
        n = int(f.readline())
        self.__graph['num_nodes'] = n
        cities = []
        for i in range(n):
            line = f.readline()
            elements = line.split(" ")
            c = City(i, float(elements[1]), float(elements[2]))
            cities.append(c)
        initial_pheromone = float(f.readline())
        # matrix with all edges
        edges = [[None] * n for _ in range(n)]
        for i in range(n):
            for j in range(i + 1, n):
                edges[i][j] = edges[j][i] = Edge(cities[i], cities[j], initial_pheromone)
        self.__graph['edges'] = edges
        size = int(f.readline())
        gen = int(f.readline())
        rho = float(f.readline())
        phe_dep = float(f.readline())
        alpha = float(f.readline())
        beta = float(f.readline())
        self.__aco_params = {'col_size': size, 'gen': gen, 'initial_pheromone': initial_pheromone,
                             'rho': rho, 'pheromone_deposit_weight': phe_dep,
                             'alpha': alpha, 'beta': beta
                             }
        f.close()
