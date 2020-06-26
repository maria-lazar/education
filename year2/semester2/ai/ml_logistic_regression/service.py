import random

import sklearn
from sklearn import linear_model

from my_logistic_regression import MyLogisticRegression
from plot import plot_predictions, plot_classification_data
from standard_scaler import MyStandardScaler


class Service:
    def __init__(self, repo):
        self.__repository = repo
        self.input_features = self.__repository.get_input_features()
        self.output_names = self.__repository.get_output_names()
        self.train_inputs, self.train_outputs, self.test_inputs, self.test_outputs = self.split_data()
        self.train_inputs, self.test_inputs = self.normalisation(self.train_inputs, self.test_inputs)

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

    def solve_binary_classification(self, iterations=100, learning_rate=0.01):
        nr_features = self.train_inputs[0]
        if len(nr_features) == 2:
            plot_classification_data(self.train_inputs, self.train_outputs, self.input_features, self.output_names)

        classifier = MyLogisticRegression(iterations, learning_rate)
        classifier.fit(self.train_inputs, self.train_outputs)
        b = classifier.get_coef()
        intercept = classifier.get_intercept()
        f = "f(x) = " + str(intercept)
        for i in range(len(b)):
            f += " + " + str(b[i]) + "*x" + str(i + 1)
        print("model: " + f)

        computed_test_outputs = classifier.predict(self.test_inputs)
        if len(nr_features) == 2:
            plot_predictions(self.test_inputs, self.test_outputs, computed_test_outputs, self.input_features,
                             self.output_names)
        self.eval_classification(computed_test_outputs)

        # sklearn results
        classifier_sk = linear_model.LogisticRegression()
        classifier_sk.fit(self.train_inputs, self.train_outputs)
        b = classifier_sk.coef_.tolist()[0]
        intercept = classifier_sk.intercept_[0]
        f = "f(x) = " + str(intercept)
        for i in range(len(b)):
            f += " + " + str(b[i]) + "*x" + str(i + 1)
        print("model sk: " + f)
        computed_test_outputs_sk = classifier_sk.predict(self.test_inputs)
        print(sklearn.metrics.classification_report(self.test_outputs, computed_test_outputs_sk,
                                                    target_names=self.output_names))

    def solve_multiclass_classification(self, iterations=500, learning_rate=0.01):
        classifier = MyLogisticRegression(iterations, learning_rate, multi_class=True)
        classifier.fit(self.train_inputs, self.train_outputs)

        computed_test_outputs = classifier.predict(self.test_inputs)
        print(self.test_outputs)
        print(computed_test_outputs)
        self.eval_classification(computed_test_outputs)

        # sklearn results
        classifier_sk = linear_model.LogisticRegression(multi_class="ovr")
        classifier_sk.fit(self.train_inputs, self.train_outputs)
        computed_test_outputs_sk = classifier_sk.predict(self.test_inputs)
        print(sklearn.metrics.classification_report(self.test_outputs, computed_test_outputs_sk,
                                                    target_names=self.output_names))

    def eval_classification(self, computed):
        real = self.test_outputs
        labels = self.output_names
        # calculate accuracy
        accuracy = sum(1 for i in range(len(real)) if real[i] == computed[i]) / len(real)
        # build confusion matrix
        mat = [[0] * len(labels) for i in range(len(labels))]
        for i in range(len(computed)):
            t_index = real[i]
            c_index = computed[i]
            mat[c_index][t_index] += 1
        # calculate precision and recall for each label
        eval_mat = []
        for i in range(len(labels)):
            el = []
            prec = mat[i][i] / sum(mat[i])
            el.append(prec)
            rec = mat[i][i] / sum([mat[j][i] for j in range(len(labels))])
            el.append(rec)
            eval_mat.append(el)
        print("accuracy: " + str(accuracy))
        for i in range(len(labels)):
            print("{} prec: {} rec: {}".format(labels[i], eval_mat[i][0], eval_mat[i][1]))
