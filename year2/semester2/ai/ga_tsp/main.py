from repository import Repository
from service import Service


# graph route distance
def route_distance(route):
    d = 0.0
    mat = repository.mat
    for i in range(len(route) - 1):
        a = mat[route[i]][route[i + 1]]
        d = d + a
    return d


# euclidean route distance
def coord_route_distance(route):
    dict = repository.dict_cities
    path_distance = 0
    for i in range(0, len(route) - 1):
        from_city = dict[route[i]]
        to_city = dict[route[i + 1]]
        path_distance += from_city.distance(to_city)
    return path_distance


# fitness function for euclidean distance
def coord_fitness(route):
    d = float(coord_route_distance(route))
    return 1 / d


# fitness function for graph route distance
def fitness(route):
    return 1 / route_distance(route)


repository = Repository("data/graph_in2.txt")
service = Service(repository)
service.find_tsp_solution(fitness_function=fitness, dist_function=route_distance, pop_size=50, gen=250,
                          m_rate=0.1, elite=5)
