import random
import matplotlib.pyplot as plt
import numpy as np


def sigmoid(x):
    return 1 / (1 + np.exp(-x))


class MyLogisticRegression:
    def __init__(self, iterations, learning_rate, multi_class=False):
        self.b = []
        self.iterations = iterations
        self.learning_rate = learning_rate
        self.multi_class = multi_class
        self.classifiers = []

    def get_intercept(self):
        return self.b[-1]

    def get_coef(self):
        return self.b[:-1]

    def fit(self, x, y):
        if not self.multi_class:
            self.fit_binary_stochastic(x, y)
            # self.fit_binary_batch(x, y)
        else:
            self.fit_multiclass(x, y)

    def predict(self, x):
        if not self.multi_class:
            comp_prob = self.predict_prob(x)
            return [self.sample_label(prob) for prob in comp_prob]
        else:
            computed = []
            for i in range(len(x)):
                val = [self.predict_prob_sample(x[i], self.classifiers[label]) for label in
                       range(len(self.classifiers))]
                index = val.index(max(val))
                computed.append(index)
            return computed

    def fit_binary_stochastic(self, x, y):
        self.b = [0.0 for i in range(len(x[0]) + 1)]
        indices = [i for i in range(len(x))]
        progress = []
        for iteration in range(self.iterations):
            ind_random = random.sample(indices, len(indices))
            x_random = [x[i] for i in ind_random]
            y_random = [y[i] for i in ind_random]
            for i in range(len(x)):
                val = self.eval(x_random[i])
                computed_output = sigmoid(val)
                error = computed_output - y_random[i]
                for j in range(len(x_random[0])):
                    self.b[j] = self.b[j] - self.learning_rate * error * x_random[i][j]
                self.b[-1] = self.b[-1] - self.learning_rate * error
            iteration_error = self.mean_square_error(y, self.predict_prob(x))
            progress.append(iteration_error)
        plt.plot(progress)
        plt.ylabel('Cost')
        plt.xlabel('Iteration')
        plt.show()

    def fit_binary_batch(self, x, y):
        self.b = [0.0 for i in range(len(x[0]) + 1)]
        for iteration in range(self.iterations):
            gradients = [0.0 for i in range(len(x[0]) + 1)]
            for i in range(len(x)):
                computed_output = sigmoid(self.eval(x[i]))
                error = computed_output - y[i]
                for j in range(len(x[0])):
                    gradients[j] += (1 / len(x)) * error * x[i][j]
                gradients[-1] += (1 / len(x)) * error
            for j in range(len(x[0])):
                self.b[j] = self.b[j] - gradients[j] * self.learning_rate
            self.b[-1] = self.b[-1] - gradients[-1] * self.learning_rate

    def fit_multiclass(self, x, y):
        output_names = list(set(y))
        for label in range(len(output_names)):
            train_outputs_label = self.transform_data_by_label(y, label)
            self.fit_binary_stochastic(x, train_outputs_label)
            # self.fit_binary_batch(x, train_outputs_label)
            self.classifiers.append(self.b)

    def transform_data_by_label(self, y, label):
        train_outputs = y[:]
        for i in range(len(train_outputs)):
            if train_outputs[i] == label:
                train_outputs[i] = 1
            else:
                train_outputs[i] = 0
        return train_outputs

    def predict_prob_sample(self, x, coef=None):
        return sigmoid(self.eval(x, coef))

    def predict_prob(self, x, coef=None):
        return [self.predict_prob_sample(x[i], coef) for i in range(len(x))]

    def eval(self, x, coef=None):
        if coef is None:
            coef = self.b
        s = coef[-1]
        for j in range(len(x)):
            s += x[j] * coef[j]
        return s

    def sample_label(self, computed_prob):
        threshold = 0.5
        if computed_prob < threshold:
            computed_label = 0
        else:
            computed_label = 1
        return computed_label

    def mean_square_error(self, real, computed):
        return sum([(computed[i] - real[i]) ** 2 for i in range(len(computed))]) / len(computed)

    def cross_entropy_loss(self, real, computed):
        sum = 0
        for i in range(len(computed)):
            y = real[i]
            sum += -y * np.log(computed[i] + 1e-15) - (1 - y) * np.log(1 - computed[i] + 1e-15)
        return sum / len(computed)
