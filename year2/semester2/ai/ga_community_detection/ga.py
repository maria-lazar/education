from random import randint, uniform

from numpy import interp

from chromosome import Chromosome
import numpy.random as npr

class GA:
    def __init__(self, fitness_function, min_value, max_value, dim, pop_size, elite, mutation_rate):
        self.min_value = min_value
        self.max_value = max_value
        self.dim = dim
        self.fitness_function = fitness_function
        self.pop_size = pop_size
        self.elite = elite
        self.mutation_rate = mutation_rate
        self.population = []
        self.__mating_pool = []

    # for 20% nodes of each chromosome chosen randomly
    # set the community label of their neighbours with theirs
    def initialisation(self, mat):
        num_nodes = len(mat[0])
        for _ in range(0, self.pop_size):
            c = Chromosome(self.min_value, self.max_value, self.dim)
            self.population.append(c)
        p = int(0.2 * num_nodes)
        for c in self.population:
            nodes = [int(uniform(0, num_nodes)) for j in range(p)]
            for j in nodes:
                for k in range(num_nodes):
                    if mat[j][k] == 1:
                        c.repres[k] = c.repres[j]

    def evaluation(self):
        for c in self.population:
            c.fitness = self.fitness_function(c.repres)
        self.population.sort(key=lambda x: x.fitness)

    def best_chromosome(self):
        return self.population[len(self.population) - 1]

    def worst_chromosome(self):
        worst = self.population[0]
        for c in self.population:
            if c.fitness < worst.fitness:
                worst = c
        return worst

    def tournament_selection(self):
        indexes = [randint(0, self.pop_size - 1) for i in range(5)]
        c = [self.population[i] for i in indexes]
        return max(c, key=lambda x: x.fitness)

    def select_from_pool(self):
        pos1 = randint(0, len(self.__mating_pool) - 1)
        return self.__mating_pool[pos1]

    # generate mating pool based on fitness
    def generate_pool(self):
        self.__mating_pool = []
        max_fitness = self.best_chromosome().fitness
        for i in range(len(self.population)):
            fitness = interp(self.population[i].fitness, [0, max_fitness], [0, 1])
            n = int(fitness * 100)
            for j in range(n):
                self.__mating_pool.append(self.population[i])

    # get next generation based on roulette wheel selection
    def next_generation_with_pool(self):
        self.generate_pool()
        new_population = []
        for i in range(len(self.population) - self.elite, len(self.population)):
            new_population.append(self.population[i])
        for _ in range(self.pop_size - self.elite):
            # p1 = self.select_from_pool()
            p1 = self.select_from_pool()
            p2 = self.select_from_pool()
            # p2 = self.select_from_pool()
            off = p1.crossover(p2)
            off.mutation(self.mutation_rate)
            new_population.append(off)
        self.population = new_population
        self.evaluation()

    def next_generation_with_tournament(self):
        new_population = []
        for i in range(len(self.population) - self.elite, len(self.population)):
            new_population.append(self.population[i])
        for _ in range(self.pop_size - self.elite):
            p1 = self.tournament_selection()
            p2 = self.tournament_selection()
            off = p1.crossover(p2)
            off.mutation(self.mutation_rate)
            new_population.append(off)
        self.population = new_population
        self.evaluation()

