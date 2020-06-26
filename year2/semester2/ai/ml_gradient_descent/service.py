import random

from sklearn.linear_model import SGDRegressor

from my_bgd_regression import MyBGDRegression
from my_mbgd_regression import MyMBGDRegression
from my_multi_target_gd_regression import MyMultiTargetGDRegression
from my_sgd_regression import MySGDRegression
from plot import plot_model_simple, plot_test_results_simple, plot_model_multiple, plot_test_results_multiple, \
    plot_data_split_simple, plot_data_split_multiple
from standard_scaler import MyStandardScaler


class Service:
    def __init__(self, repo):
        self.__repository = repo
        self.input_features = self.__repository.get_input_features()
        self.train_inputs, self.train_outputs, self.test_inputs, self.test_outputs = self.split_data()
        self.train_inputs, self.test_inputs = self.normalisation(self.train_inputs, self.test_inputs)
        self.train_outputs, self.test_outputs = self.normalisation(self.train_outputs, self.test_outputs)

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

    def normalisation(self, train, test):
        if not isinstance(train[0], list):
            train = [[el] for el in train]
            test = [[el] for el in test]
            scaler = MyStandardScaler()
            scaler.fit(train)
            normalised_train_data = scaler.transform(train)
            normalised_test_data = scaler.transform(test)
            return [el[0] for el in normalised_train_data], [el[0] for el in normalised_test_data]
        else:
            scaler = MyStandardScaler()
            scaler.fit(train)
            normalised_train_data = scaler.transform(train)
            normalised_test_data = scaler.transform(test)
            return normalised_train_data, normalised_test_data

    def mean_square_error(self, real, computed):
        return sum([(computed[i] - real[i]) ** 2 for i in range(len(computed))]) / len(computed)

    def plot_data_split(self):
        # for problem with 1 or 2 features plot data split
        nr_features = len(self.train_inputs[0])
        if nr_features == 1:
            plot_data_split_simple(self.train_inputs, self.train_outputs, self.test_inputs, self.test_outputs,
                                   [self.input_features[0], "Happiness"])
        elif nr_features == 2:
            plot_data_split_multiple(self.train_inputs, self.train_outputs, self.test_inputs, self.test_outputs,
                                     [self.input_features[0], self.input_features[1], "Happiness"])

    def plot_model(self, coef, intercept):
        # for problem with 1 or 2 features plot model
        nr_features = len(self.train_inputs[0])
        if nr_features == 1:
            plot_model_simple(self.train_inputs, self.train_outputs, coef[0], intercept,
                              [self.input_features[0], "Happiness"])
        elif nr_features == 2:
            plot_model_multiple(self.train_inputs, self.train_outputs, coef[1], coef[0], intercept,
                                [self.input_features[0], self.input_features[1], "Happiness"])

    def plot_test_results(self, computed_test_results):
        # for problem with 1 or 2 features plot test results
        nr_features = len(self.train_inputs[0])
        if nr_features == 1:
            plot_test_results_simple(self.test_inputs, self.test_outputs, computed_test_results,
                                     [self.input_features[0], "Happiness"])
        elif nr_features == 2:
            plot_test_results_multiple(self.test_inputs, self.test_outputs, computed_test_results,
                                       [self.input_features[0], self.input_features[1], "Happiness"])

    # problem with one output
    def solve_gd_regression(self, gd_type="s", iterations=100, learning_rate=0.01, batch_size=32):
        # for problem with 1 or 2 features plot data split
        self.plot_data_split()

        # find model -> gd_type: "s"(stochastic gd), "m"(mini batch gd), "b"(batch gd)
        if gd_type == "m":
            regression = MyMBGDRegression(iterations=iterations, learning_rate=learning_rate, batch_size=batch_size)
        elif gd_type == "b":
            regression = MyBGDRegression(iterations=iterations, learning_rate=learning_rate)
        else:
            regression = MySGDRegression(iterations=iterations, learning_rate=learning_rate)
        progress = regression.fit(self.train_inputs, self.train_outputs)
        b = regression.b
        intercept = regression.intercept
        f = "f(x) = " + str(intercept)
        for i in range(len(b)):
            f += " + " + str(b[i]) + "*x" + str(i + 1)
        print("model: " + f)
        # for problem with 1 or 2 features plot model
        self.plot_model(b, intercept)

        # test model
        computed_test_results = regression.predict(self.test_inputs)
        # for problem with 1 or 2 features plot test results
        self.plot_test_results(computed_test_results)
        print("prediction error: " + str(self.mean_square_error(self.test_outputs, computed_test_results)))

        # compare with sklearn results with stochastic gradient descent
        regression_sk = SGDRegressor(alpha=learning_rate, max_iter=iterations)
        regression_sk.fit(self.train_inputs, self.train_outputs)
        b = regression_sk.coef_
        f = "f(x) = " + str(regression_sk.intercept_[0])
        for i in range(len(b)):
            f += " + " + str(b[i]) + "*x" + str(i + 1)
        print("model sk: " + f)
        computed_test_results_sk = regression_sk.predict(self.test_inputs)
        print("prediction error sk: " + str(self.mean_square_error(self.test_outputs, computed_test_results_sk)))
        return progress

    def prediction_error_multi_target(self, computed):
        mse = []
        real = self.test_outputs
        for i in range(len(real[0])):
            r = [real[j][i] for j in range(len(real))]
            c = [computed[j][i] for j in range(len(computed))]
            val = self.mean_square_error(r, c)
            mse.append(val)
        return sum([i for i in mse]) / len(mse)

    # problem with multiple outputs
    def solve_gd_multi_regression(self, iterations=1000, learning_rate=0.01):
        regression = MyMultiTargetGDRegression(iterations=iterations, learning_rate=learning_rate)
        regression.fit(self.train_inputs, self.train_outputs)
        computed = regression.predict(self.test_inputs)
        print("prediction error: " + str(self.prediction_error_multi_target(computed)))
