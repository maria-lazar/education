import random

from sklearn.linear_model import LinearRegression

from my_linear_regression import MyLinearRegression
from plot import plot_model_simple, plot_test_results_simple, plot_model_multiple, plot_test_results_multiple, \
    plot_data_split_simple, plot_data_split_multiple


class Service:
    def __init__(self, repo):
        self.__repository = repo
        self.input_features = self.__repository.get_input_features()
        self.train_inputs, self.train_outputs, self.test_inputs, self.test_outputs = self.split_data()

    def split_data(self):
        inputs = self.__repository.get_inputs()
        outputs = self.__repository.get_outputs()
        indexes = [i for i in range(len(inputs))]
        k = int(0.8 * len(inputs))
        train_indexes = random.sample(indexes, k)
        test_indexes = [i for i in indexes if not i in train_indexes]

        train_inputs = [inputs[i] for i in train_indexes]
        train_outputs = [outputs[i] for i in train_indexes]

        test_inputs = [inputs[i] for i in test_indexes]
        test_outputs = [outputs[i] for i in test_indexes]
        return train_inputs, train_outputs, test_inputs, test_outputs

    def mean_square_error(self, computed):
        return sum([(computed[i] - self.test_outputs[i]) ** 2 for i in range(len(computed))]) / len(computed)

    def solve_linear_regression(self):
        nr_features = len(self.train_inputs[0])
        # for problem with 1 or 2 features plot data split
        if nr_features == 1:
            plot_data_split_simple(self.train_inputs, self.train_outputs, self.test_inputs, self.test_outputs,
                                   [self.input_features[0], "Happiness"])
        elif nr_features == 2:
            plot_data_split_multiple(self.train_inputs, self.train_outputs, self.test_inputs, self.test_outputs,
                                     [self.input_features[0], self.input_features[1], "Happiness"])
        # find model
        regression = MyLinearRegression()
        regression.fit(self.train_inputs, self.train_outputs)
        b = regression.b
        f = "f(x) = " + str(regression.intercept)
        for i in range(len(b)):
            f += " + " + str(b[i]) + "*x" + str(i + 1)
        print("model: " + f)
        # test model
        computed_test_results = regression.predict(self.test_inputs)
        print("prediction error: " + str(self.mean_square_error(computed_test_results)))

        # for problem with 1 or 2 features plot model and test results
        if nr_features == 1:
            plot_model_simple(self.train_inputs, self.train_outputs, b[0], regression.intercept,
                              [self.input_features[0], "Happiness"])
            plot_test_results_simple(self.test_inputs, self.test_outputs, computed_test_results,
                                     [self.input_features[0], "Happiness"])
        elif nr_features == 2:
            plot_model_multiple(self.train_inputs, self.train_outputs, b[1], b[0], regression.intercept,
                                [self.input_features[0], self.input_features[1], "Happiness"])
            plot_test_results_multiple(self.test_inputs, self.test_outputs, computed_test_results,
                                       [self.input_features[0], self.input_features[1], "Happiness"])

        # compare with sklearn results
        regression_sk = LinearRegression()
        regression_sk.fit(self.train_inputs, self.train_outputs)
        b = regression_sk.coef_
        f = "f(x) = " + str(regression_sk.intercept_)
        for i in range(len(b)):
            f += " + " + str(b[i]) + "*x" + str(i + 1)
        print("model sk: " + f)
        computed_test_results_sk = regression_sk.predict(self.test_inputs)
        print("prediction error sk: " + str(self.mean_square_error(computed_test_results_sk)))
