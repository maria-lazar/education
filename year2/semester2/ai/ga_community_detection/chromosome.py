from random import randint, uniform, random


def generate_value(lim1, lim2):
    return int(uniform(lim1, lim2))


class Chromosome:
    def __init__(self, min_value, max_value, dim):
        self.min_value = min_value
        self.max_value = max_value
        self.dim = dim
        self.repres = [generate_value(min_value, max_value) for _ in
                       range(dim)]
        self.__fitness = 0.0

    @property
    def fitness(self):
        return self.__fitness

    def crossover(self, c):
        r = randint(0, len(self.repres) - 1)
        new_representation = []
        for i in range(r):
            new_representation.append(self.repres[i])
        for i in range(r, len(self.repres)):
            new_representation.append(c.repres[i])
        offspring = Chromosome(self.min_value, self.max_value, self.dim)
        offspring.repres = new_representation
        return offspring

    def mutation(self, rate):
        for gene in range(len(self.repres)):
            if random() < rate:
                pos = gene
                pos2 = randint(0, len(self.repres) - 1)
                self.repres[pos], self.repres[pos2] = self.repres[pos2], self.repres[pos]
                # self.__representation[pos] = generate_value(self.__prob_param['min'], self.__prob_param['max'])

