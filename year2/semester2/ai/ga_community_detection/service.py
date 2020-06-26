from random import uniform

import matplotlib.pyplot as plt
import networkx as nx
import numpy as np

from ga import GA


def generate_value(lim1, lim2):
    return int(uniform(lim1, lim2))


class Service:
    def __init__(self, repo):
        self.repository = repo

    def find_communities(self, fitness_function, pop_size, gen, m_rate, elite):
        min_value = 1
        max_value = self.repository.num_nodes + 1
        dim = self.repository.num_nodes
        # draw initial network
        self.draw_network()
        # list with the best fitness of every generation
        all_best_fitness = []
        # list with the best community num of every generation
        all_best_community_num = []

        ga = GA(fitness_function, min_value, max_value, dim, pop_size, elite, m_rate)
        ga.initialisation(self.repository.mat)
        ga.evaluation()
        gen_best_chromosome = ga.best_chromosome()
        overall_best = gen_best_chromosome
        for g in range(gen):
            gen_best_chromosome = ga.best_chromosome()
            all_best_community_num.append(len(set(gen_best_chromosome.repres)))
            all_best_fitness.append(gen_best_chromosome.fitness)
            # ga.next_generation_with_pool()
            ga.next_generation_with_tournament()
            gen_best_chromosome = ga.best_chromosome()
            if gen_best_chromosome.fitness > overall_best.fitness:
                overall_best = gen_best_chromosome
            print('Best solution in generation ' + str(g) + ' is: x = ' + str(
                gen_best_chromosome.repres) + ' f(x) = ' + str(
                gen_best_chromosome.fitness) + " communities: " + str(all_best_community_num[len(all_best_community_num) - 1]))
        self.repository.write_network("data/network_out.txt", overall_best, all_best_fitness, all_best_community_num)
        plt.plot(all_best_fitness)
        plt.ylabel('Fitness')
        plt.xlabel('Generation')
        plt.show()
        # draw best community detection
        self.draw_network(color=gen_best_chromosome.repres)

    def draw_network(self, color=None):
        A = np.matrix(self.repository.mat)
        G = nx.from_numpy_matrix(A)
        pos = nx.spring_layout(G)
        plt.figure(figsize=(4, 4))
        if color is None:
            nx.draw_networkx_nodes(G, pos, node_size=600, cmap=plt.cm.RdYlBu)
        else:
            nx.draw_networkx_nodes(G, pos, node_size=600, cmap=plt.cm.RdYlBu, node_color=color)
        nx.draw_networkx_edges(G, pos, alpha=0.3)
        plt.show(G)

