import random
from cmath import sqrt

import matplotlib.pyplot as plt


class MyMultiTargetGDRegression:
    def __init__(self, iterations, learning_rate):
        self.b = []
        self.intercept = []
        self.iterations = iterations
        self.learning_rate = learning_rate

    def initialize_matrix(self, lx, ly):
        intercept = []
        b = []
        for i in range(ly):
            intercept.append(0.0)
            row = []
            for j in range(lx):
                row.append(0.0)
            b.append(row)
        return b, intercept

    def fit(self, x, y):
        self.b, self.intercept = self.initialize_matrix(len(x[0]), len(y[0]))
        progress = []
        for iteration in range(self.iterations):
            gradients = self.initialize_matrix(len(x[0]) + 1, len(y[0]))[0]
            for i in range(len(x)):
                computed_output = self.eval(x[i])
                error = [computed_output[j] - y[i][j] for j in range(len(y[0]))]
                for k in range(len(y[0])):
                    for j in range(len(x[0])):
                        gradients[k][j] += (1 / len(x)) * error[k] * x[i][j]
                    gradients[k][-1] += (1 / len(x)) * error[k]
            for i in range(len(y[0])):
                for j in range(len(x[0])):
                    self.b[i][j] = self.b[i][j] - gradients[i][j] * self.learning_rate
                self.intercept[i] = self.intercept[i] - gradients[i][-1] * self.learning_rate
            iteration_error = self.prediction_error_multi_target(y, self.predict(x))
            progress.append(iteration_error)
            print("iteration {} prediction error: {} ".format(iteration, iteration_error))
        plt.plot(progress)
        plt.ylabel('Cost')
        plt.xlabel('Iteration')
        plt.show()
        return progress

    def prediction_error_multi_target(self, real, computed):
        rmse = []
        for i in range(len(real[0])):
            r = [real[j][i] for j in range(len(real))]
            c = [computed[j][i] for j in range(len(computed))]
            val = self.square_error(r, c)
            rmse.append(val)
        return sum([i for i in rmse]) / len(rmse)

    def square_error(self, real, computed):
        return sum([(computed[i] - real[i]) ** 2 for i in range(len(computed))]) / len(computed)

    def predict(self, x):
        y = []
        for i in range(len(x)):
            s = self.eval(x[i])
            y.append(s)
        return y

    def eval(self, x):
        result = []
        for i in range(len(self.intercept)):
            s = self.intercept[i]
            for j in range(len(x)):
                s += x[j] * self.b[i][j]
            result.append(s)
        return result
