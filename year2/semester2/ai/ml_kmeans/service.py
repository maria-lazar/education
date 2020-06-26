import random

import matplotlib.pyplot as plt
from sklearn.cluster import KMeans
from sklearn.feature_extraction.text import CountVectorizer, TfidfVectorizer

from my_kmeans import MyKMeans
from my_logistic_regression import MyLogisticRegression
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

    def my_kmeans_numeric(self):
        self.train_inputs, self.test_inputs = self.normalisation(self.train_inputs, self.test_inputs)
        unsupervised_classifier = MyKMeans(clusters=3)
        train = unsupervised_classifier.fit(self.train_inputs)
        predicted = unsupervised_classifier.predict(self.test_inputs)
        predicted = self.verify_labels(predicted)
        self.eval_classification(predicted)
        print("{} - True".format(self.test_outputs))
        print("{} - Predicted unsupervised".format(predicted))
        self.plot_results(train, self.train_inputs, unsupervised_classifier.centroids)
        self.plot_results(predicted, self.test_inputs, unsupervised_classifier.centroids)

    def kmeans_with_tool_numeric(self):
        self.train_inputs, self.test_inputs = self.normalisation(self.train_inputs, self.test_inputs)
        unsupervised_classifier = KMeans(n_clusters=3)
        train = unsupervised_classifier.fit_predict(self.train_inputs)
        predicted = unsupervised_classifier.predict(self.test_inputs)
        predicted = self.verify_labels(predicted)
        self.plot_results(train, self.train_inputs, unsupervised_classifier.cluster_centers_)
        self.plot_results(predicted, self.test_inputs, unsupervised_classifier.cluster_centers_)
        self.eval_classification(predicted)

    def plot_results(self, predicted, inputs, clusters):
        x1 = [inputs[i][0] for i in range(len(inputs))]
        x2 = [inputs[i][1] for i in range(len(inputs))]
        clusters1 = [clusters[i][0] for i in range(len(clusters))]
        clusters2 = [clusters[i][1] for i in range(len(clusters))]
        plt.scatter(clusters1, clusters2, c='black')
        plt.scatter(x1, x2, c=predicted, cmap='rainbow')
        plt.show()

    def bag_of_words(self):
        vectorizer = CountVectorizer()
        vectorizer.fit(self.train_inputs)
        self.train_inputs = vectorizer.fit_transform(self.train_inputs).toarray().tolist()
        self.test_inputs = vectorizer.transform(self.test_inputs).toarray().tolist()

    def tf_idf(self):
        vectorizer = TfidfVectorizer(max_features=50)
        self.train_inputs = vectorizer.fit_transform(self.train_inputs).toarray().tolist()
        self.test_inputs = vectorizer.transform(self.test_inputs).toarray().tolist()

    def classification_text(self):
        self.bag_of_words()
        # self.tf_idf()
        unsupervised_classifier = MyKMeans(clusters=2, iterations=500)
        unsupervised_classifier.fit(self.train_inputs)
        predicted = unsupervised_classifier.predict(self.test_inputs)

        # self.verify_labels(predicted)
        self.eval_classification(predicted)

        # classifier = MyLogisticRegression(learning_rate=0.01, iterations=200)
        # classifier.fit(self.train_inputs, self.train_outputs)
        # predicted_supervised = classifier.predict(self.test_inputs)
        # self.eval_classification(predicted_supervised)

        print("{} - True".format(self.test_outputs))
        # print("{} - Predicted supervised".format(predicted_supervised))
        print("{} - Predicted unsupervised".format(predicted))

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

    def verify_labels(self, predicted):
        for i in range(len(self.output_names)):
            for j in range(len(self.output_names)):
                c = 0
                for k in range(len(predicted)):
                    if predicted[k] == i and self.test_outputs[k] == j:
                        c += 1
                if c >= self.test_outputs.count(self.output_names[i]) / 2:
                    for k in range(len(predicted)):
                        if predicted[k] == self.output_names[i]:
                            predicted[k] = self.output_names[j]
                        elif predicted[k] == self.output_names[j]:
                            predicted[k] = self.output_names[i]
        return predicted
