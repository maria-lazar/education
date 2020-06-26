import matplotlib.pyplot as plt

from ant import Ant


class Aco:
    def __init__(self, aco_params):
        self.steps = aco_params['steps']
        self.colony_size = aco_params['colony_size']
        self.rho = aco_params['rho']
        self.pheromone_deposit_weight = aco_params['pheromone_deposit_weight']
        alpha = aco_params['alpha']
        beta = aco_params['beta']
        self.num_nodes = aco_params['num_nodes']
        self.edges = aco_params['edges']
        self.initial_pheromone = aco_params['initial_pheromone']
        self.colony = [Ant(alpha, beta, self.num_nodes, self.edges) for _ in
                       range(self.colony_size)]
        # best tour
        self.global_best_tour = None
        # best distance
        self.global_best_distance = float("inf")

    def add_pheromone(self, tour, distance):
        pheromone_to_add = self.pheromone_deposit_weight / distance
        for i in range(self.num_nodes):
            self.edges[tour[i]][tour[(i + 1) % self.num_nodes]].pheromone += pheromone_to_add

    def update_local(self, tour):
        for i in range(self.num_nodes):
            self.edges[tour[i]][tour[(i + 1) % self.num_nodes]].pheromone += self.rho * self.initial_pheromone

    def solve(self):
        # best ant from each step
        progress = []
        for step in range(self.steps):
            for ant in self.colony:
                ant.find_tour()
                ant.calculate_distance()
                # update local pheromone
                self.update_local(ant.tour)
                if ant.distance < self.global_best_distance:
                    self.global_best_tour = ant.tour
                    self.global_best_distance = ant.distance
            # best ant adds pheromone
            self.add_pheromone(self.global_best_tour, self.global_best_distance)
            # evaporation of pheromone
            for i in range(self.num_nodes):
                for j in range(self.num_nodes):
                    if self.edges[i][j] is not None:
                        self.edges[i][j].pheromone *= (1.0 - self.rho)
            progress.append(self.global_best_distance)
            print("Best solution at step " + str(step) + " is " + str(self.global_best_tour) + " with distance " + str(
                self.global_best_distance))
        plt.plot(progress)
        plt.ylabel('Distance')
        plt.xlabel('Generation')
        plt.savefig('figure')
        plt.show()
