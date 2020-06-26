from random import randint, uniform, shuffle

import numpy.random as npr

from chromosome import Chromosome


def generate_value(lim1, lim2):
    return int(uniform(lim1, lim2))


class GA:
    def __init__(self, fitness_function, min_value, max_value, dim, pop_size, elite, m_rate):
        self.min_value = min_value
        self.max_value = max_value
        self.dim = dim
        self.fitness_function = fitness_function
        self.pop_size = pop_size
        self.elite = elite
        self.mutation_rate = m_rate
        self.population = []
        self.__selection_probabilities = []

    def initialisation(self):
        for _ in range(0, self.pop_size):
            r = [i for i in range(1, self.max_value)]
            shuffle(r)
            r = [0] + r
            r.append(0)
            cr = Chromosome(self.min_value, self.max_value, self.dim)
            cr.repres = r
            self.population.append(cr)

    def evaluation(self):
        for c in self.population:
            c.fitness = self.fitness_function(c.repres)
        self.population.sort(key=lambda x: x.fitness)

    def best_chromosome(self):
        return self.population[len(self.population) - 1]

    # roulette wheel selection
    def select(self):
        return npr.choice(self.population, p=self.__selection_probabilities)

    def selection_probabilities(self):
        max_value = sum([c.fitness for c in self.population])
        self.__selection_probabilities = [c.fitness / max_value for c in self.population]

    def next_generation_pool(self):
        self.selection_probabilities()
        new_population = []
        elite = self.elite
        for i in range(len(self.population) - elite, len(self.population)):
            new_population.append(self.population[i])
        for i in range(self.pop_size - elite):
            p1 = self.select()
            p2 = self.select()
            off = p1.crossover(p2)
            off.mutation(self.mutation_rate)
            new_population.append(off)
        self.population = new_population
        self.evaluation()

    def tournament_selection(self):
        indexes = [randint(0, self.pop_size - 1) for i in range(10)]
        c = [self.population[i] for i in indexes]
        return max(c, key=lambda x: x.fitness)

    def next_generation_tournament(self):
        new_population = []
        elite = self.elite
        for i in range(len(self.population) - elite, len(self.population)):
            new_population.append(self.population[i])
        for i in range(self.pop_size - elite):
            p1 = self.tournament_selection()
            p2 = self.tournament_selection()
            off = p1.crossover(p2)
            off.mutation(self.mutation_rate)
            new_population.append(off)
        self.population = new_population
        self.evaluation()
