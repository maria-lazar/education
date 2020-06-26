import random

import matplotlib.pyplot as plt


class MySGDRegression:
    def __init__(self, iterations, learning_rate):
        self.b = []
        self.intercept = 0.0
        self.iterations = iterations
        self.learning_rate = learning_rate

    def fit(self, x, y):
        self.b = [0.0 for i in range(len(x[0]))]
        indices = [i for i in range(len(x))]
        progress = []
        for iteration in range(self.iterations):
            ind_random = random.sample(indices, len(indices))
            x_random = [x[i] for i in ind_random]
            y_random = [y[i] for i in ind_random]
            for i in range(len(x)):
                computed_output = self.eval(x_random[i])
                error = computed_output - y_random[i]
                for j in range(len(x_random[0])):
                    self.b[j] = self.b[j] - self.learning_rate * error * x_random[i][j]
                self.intercept = self.intercept - self.learning_rate * error
            iteration_error = self.mean_square_error(y, self.predict(x))
            progress.append(iteration_error)
            print("iteration {} prediction error: {} ".format(iteration, iteration_error))
        plt.plot(progress)
        plt.ylabel('Cost')
        plt.xlabel('Iteration')
        plt.show()
        return progress

    def mean_square_error(self, real, computed):
        return sum([(computed[i] - real[i]) ** 2 for i in range(len(computed))]) / len(computed)

    def predict(self, x):
        y = []
        for i in range(len(x)):
            s = self.eval(x[i])
            y.append(s)
        return y

    def eval(self, x):
        s = self.intercept
        for j in range(len(x)):
            s += x[j] * self.b[j]
        return s
