import random
from math import sqrt


def euclidean_distance(x, y):
    sums = [(x[i] - y[i]) ** 2 for i in range(len(x))]
    return sqrt(sum(sums))


class MyKMeans:
    def __init__(self, clusters=3, iterations=200):
        self.clusters = clusters
        self.iterations = iterations
        self.centroids = []

    def initialize_centroids(self, x):
        indexes = [i for i in range(len(x))]
        shuffled_indexes = random.sample(indexes, self.clusters)
        self.centroids = [x[i] for i in shuffled_indexes]

    def choose_cluster(self, sample):
        distances = [euclidean_distance(sample, self.centroids[j]) for j in range(len(self.centroids))]
        return distances.index(min(distances))

    def fit(self, x):
        self.initialize_centroids(x)
        for iteration in range(self.iterations):
            cluster_elems = []
            for i in range(self.clusters):
                cluster_elems.append([])

            for i in range(len(x)):
                index = self.choose_cluster(x[i])
                cluster_elems[index].append(x[i])

            new_centroids = []
            for i in range(self.clusters):
                mean_cluster = [0.0 for j in range(len(x[0]))]
                for j in range(len(cluster_elems[i])):
                    for k in range(len(x[0])):
                        mean_cluster[k] += cluster_elems[i][j][k]
                mean_cluster = [mean_cluster[j] / len(cluster_elems[i]) for j in range(len(x[0]))]
                new_centroids.append(mean_cluster)
            stop = True
            for i in range(len(new_centroids)):
                if new_centroids[i] != self.centroids[i]:
                    stop = False
                    break
            if stop:
                break
            self.centroids = new_centroids
        return self.predict(x)

    def predict(self, x):
        return [self.choose_cluster(x[i]) for i in range(len(x))]
