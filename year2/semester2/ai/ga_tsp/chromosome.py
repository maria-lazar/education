from random import randint, uniform, random


def generate_value(lim1, lim2):
    return int(uniform(lim1, lim2))


class Chromosome:
    def __init__(self, min_value, max_value, dim):
        self.min_value = min_value
        self.max_value = max_value
        self.dim = dim
        self.repres = []
        self.fitness = 0.0

    def invert(self, i, j):
        while i < j:
            self.repres[i], self.repres[j] = self.repres[j], self.repres[i]
            i = i + 1
            j = j - 1

    # Order Crossover
    def crossover(self, c):
        childP1 = []
        childP2 = []
        gene1 = generate_value(1, self.dim - 1)
        gene2 = generate_value(1, self.dim - 1)
        start_gene = min(gene1, gene2)
        end_gene = max(gene1, gene2)
        for i in range(start_gene, end_gene):
            childP1.append(self.repres[i])
        childP2 = [item for item in c.repres if item not in childP1]
        childP2[start_gene:start_gene] = childP1
        if childP2[len(childP2) - 1] != 0:
            childP2.append(0)
        ch = Chromosome(self.min_value, self.max_value, self.dim)
        ch.repres = childP2
        return ch

    # Reverse Sequence Mutation
    def mutation(self, rate):
        rand = random()
        if rand < rate:
            gene1 = generate_value(1, len(self.repres) - 1)
            gene2 = generate_value(1, len(self.repres) - 1)
            start_gene = min(gene1, gene2)
            end_gene = max(gene1, gene2)
            self.invert(start_gene, end_gene)
        # self.__representation[gene], self.__representation[gene2] = self.__representation[gene2], \
        #                                                             self.__representation[gene]
