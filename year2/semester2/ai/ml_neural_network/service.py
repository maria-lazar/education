import random

import matplotlib.pyplot as plt
import numpy as np
from sklearn.metrics import classification_report
from sklearn.neural_network import MLPClassifier

from my_neural_network import MyNeuralNetwork
from standard_scaler import MyStandardScaler


class Service:
    def __init__(self, repo):
        self.__repository = repo
        self.input_features = self.__repository.get_input_features()
        self.output_names = self.__repository.get_output_names()
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

    def transform_output(self, output):
        transformed = []
        nr_classes = len(self.output_names)
        for i in range(len(output)):
            row = [1 if self.train_outputs[i] == j else 0 for j in range(nr_classes)]
            transformed.append(row)
        return transformed

    def my_nn(self, problem):
        self.train_inputs, self.test_inputs = self.normalisation(self.train_inputs, self.test_inputs)
        if problem == "iris":
            classifier = MyNeuralNetwork(4, 3, (10,), iterations=150, learning_rate=0.01)
        else:
            classifier = MyNeuralNetwork(64, 10, (64,), iterations=200, learning_rate=0.01)
        self.train_outputs = self.transform_output(self.train_outputs)
        classifier.fit(self.train_inputs, self.train_outputs)
        predictions = classifier.predict(self.test_inputs)
        self.eval_classification(predictions)

    def nn_with_tool(self, problem):
        self.train_inputs, self.test_inputs = self.normalisation(self.train_inputs, self.test_inputs)
        if problem == "iris":
            classifier = MLPClassifier(hidden_layer_sizes=(5,), max_iter=100,
                                       solver='sgd', verbose=10, random_state=1,
                                       learning_rate_init=.1)
        else:
            classifier = MLPClassifier(hidden_layer_sizes=(64,), max_iter=100,
                                       solver='sgd', verbose=10, random_state=1,
                                       learning_rate_init=.1)
        # self.train_outputs = self.transform_output(self.train_outputs)
        classifier.fit(self.train_inputs, self.train_outputs)
        predicted_prob = classifier.predict_proba(self.test_inputs)
        predicted_labels = []
        for i in range(len(predicted_prob)):
            index = np.where(predicted_prob[i] == (max(predicted_prob[i])))[0]
            predicted_labels.append(index.tolist()[0])
        print(classification_report(self.test_outputs, predicted_labels))
        loss_values = classifier.loss_curve_
        plt.plot(loss_values)
        plt.show()

    def confusion_matrix(self, computed):
        real = self.test_outputs
        labels = self.output_names
        mat = [[0] * len(labels) for i in range(len(labels))]
        for i in range(len(computed)):
            t_index = real[i]
            c_index = computed[i]
            mat[c_index][t_index] += 1
        return mat

    def eval_classification(self, computed):
        real = self.test_outputs
        labels = self.output_names
        # calculate accuracy
        accuracy = sum(1 for i in range(len(real)) if real[i] == computed[i]) / len(real)
        mat = self.confusion_matrix(computed)
        # calculate precision and recall for each label
        eval_mat = []
        for i in range(len(labels)):
            el = []
            prec = mat[i][i] / (sum(mat[i]) + 2e-15)
            el.append(prec)
            rec = mat[i][i] / (sum([mat[j][i] for j in range(len(labels))]) + 2e-15)
            el.append(rec)
            eval_mat.append(el)
        print("accuracy: " + str(accuracy))
        for i in range(len(labels)):
            print("{} prec: {} rec: {}".format(labels[i], eval_mat[i][0], eval_mat[i][1]))

    def normalize_images(self, input):
        inputs = []
        for i in range(len(input)):
            inputs.append([input[i][j] / 255 for j in range(len(input[0]))])
        return inputs

    def nn_images_sepia(self):
        self.train_inputs = self.normalize_images(self.train_inputs)
        self.test_inputs = self.normalize_images(self.test_inputs)
        self.train_outputs = self.transform_output(self.train_outputs)
        classifier = MyNeuralNetwork(192, 2, (192, 192), iterations=100, learning_rate=0.001)
        classifier.fit(self.train_inputs, self.train_outputs)
        predictions = classifier.predict(self.test_inputs)
        self.eval_classification(predictions)
        # classifier = MLPClassifier(hidden_layer_sizes=(192,), max_iter=100,
        #                            solver='sgd', verbose=10, random_state=1,
        #                            learning_rate_init=.01)
        # classifier.fit(self.train_inputs, self.train_outputs)

