import random

import matplotlib.pyplot as plt


class MyMBGDRegression:
    def __init__(self, iterations, learning_rate, batch_size):
        self.b = []
        self.intercept = 0.0
        self.iterations = iterations
        self.learning_rate = learning_rate
        self.batch_size = batch_size

    def fit(self, x, y):
        self.b = [0.0 for i in range(len(x[0]))]
        indices = [i for i in range(len(x))]
        progress = []
        for iteration in range(self.iterations):
            ind_random = random.sample(indices, len(indices))
            x_random = [x[i] for i in ind_random]
            y_random = [y[i] for i in ind_random]
            batches_x, batches_y = self.split_batch(x_random, y_random)
            for i in range(len(batches_x)):
                gradients = self.fit_batch(batches_x[i], batches_y[i])
                for j in range(len(x[0])):
                    self.b[j] = self.b[j] - gradients[j] * self.learning_rate
                self.intercept = self.intercept - gradients[-1] * self.learning_rate
            iteration_error = self.square_error(y, self.predict(x))
            progress.append(iteration_error)
            print("iteration {} prediction error: {} ".format(iteration, iteration_error))
        plt.plot(progress)
        plt.ylabel('Cost')
        plt.xlabel('Iteration')
        plt.show()
        return progress

    def fit_batch(self, x, y):
        gradients = [0.0 for i in range(len(x[0]) + 1)]
        for i in range(len(x)):
            computed_output = self.eval(x[i])
            error = computed_output - y[i]
            for j in range(len(x[0])):
                gradients[j] += (1 / len(x)) * error * x[i][j]
            gradients[-1] += (1 / len(x)) * error
        return gradients

    def split_batch(self, x, y):
        batches_x = [x[i:i + self.batch_size] for i in range(0, len(x), self.batch_size)]
        batches_y = [y[i:i + self.batch_size] for i in range(0, len(x), self.batch_size)]
        return batches_x, batches_y

    def square_error(self, real, computed):
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
