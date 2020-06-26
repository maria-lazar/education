from aco import Aco
from aco2 import Aco2


class Service:
    def __init__(self, repo):
        self.repository = repo

    def solve_aco_tsp(self):
        graph = self.repository.graph
        params = self.repository.get_aco_params()
        aco_params = {'edges': graph['edges'], 'num_nodes': graph['num_nodes'], 'colony_size': params['col_size'],
                      'steps': params['gen'], 'initial_pheromone': params['initial_pheromone'],
                      'rho': params['rho'], 'pheromone_deposit_weight': params['pheromone_deposit_weight'],
                      'alpha': params['alpha'],
                      'beta': params['beta']}
        aco = Aco2(aco_params)
        aco.solve()

        # print solution
        sol = [i + 1 for i in aco.global_best_tour]
        sol.append(sol[0])
        print(sol)
        print(aco.global_best_distance)
        # save results to file
        self.repository.write_to_file("data/graph_out.txt", aco.global_best_tour, aco.global_best_distance)
