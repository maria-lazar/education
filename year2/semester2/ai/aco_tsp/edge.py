class Edge:
    def __init__(self, x, y, initial_pheromone, d=None):
        if d is None:
            # problem with coordinates
            self.weight = x.distance(y)
            self.city_x = x.i
            self.city_y = y.i
        else:
            # problem with weight graph
            self.weight = d
            self.city_x = x
            self.city_y = y
        self.pheromone = initial_pheromone

    def __str__(self):
        return str(self.city_x) + " " + str(self.city_y) + " dist: " + str(self.weight)
