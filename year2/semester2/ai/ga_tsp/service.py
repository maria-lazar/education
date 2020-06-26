from random import uniform

import matplotlib.pyplot as plt

from ga import GA


def generate_value(lim1, lim2):
    return int(uniform(lim1, lim2))


class Service:
    def __init__(self, repo):
        self.repository = repo

    def find_tsp_solution(self, fitness_function, dist_function, pop_size, gen, m_rate, elite):
        min_value = 1
        max_value = self.repository.num_nodes
        dim = self.repository.num_nodes + 1

        ga = GA(fitness_function, min_value, max_value, dim, pop_size, elite, m_rate)
        ga.initialisation()
        ga.evaluation()

        overall_best = ga.best_chromosome()
        progress = []
        for g in range(gen):
            best_chromosome = ga.best_chromosome()
            progress.append(dist_function(best_chromosome.repres))
            ga.next_generation_pool()
            best_chromosome = ga.best_chromosome()
            if best_chromosome.fitness > overall_best.fitness:
                overall_best = best_chromosome
            print('Best solution in generation ' + str(g) + ' is: x = ' + str(
                best_chromosome.repres) + ' f(x) = ' + str(
                best_chromosome.fitness))

        print [i + 1 for i in overall_best.repres]
        print dist_function(overall_best.repres)

        self.repository.write_network("data/graph_out.txt", overall_best.repres,
                                      dist_function(overall_best.repres))

        plt.plot(progress)
        plt.ylabel('Distance')
        plt.xlabel('Generation')
        plt.show()
