import csv

from sklearn import datasets
from sklearn.datasets import load_digits
from sklearn.datasets import load_iris


class Repository:
    def __init__(self, loader, input_features=None, file=False):
        self.__inputs = []
        self.__outputs = []
        self.__input_features = input_features
        self.__output_names = []
        if not file:
            self.load_data(loader, input_features)
        else:
            self.load_from_file(loader)


    def get_inputs(self):
        return self.__inputs

    def get_outputs(self):
        return self.__outputs

    def get_input_features(self):
        return self.__input_features

    def get_output_names(self):
        return self.__output_names

    def load_data(self, loader, input_features):
        data = loader()
        inputs = data['data']
        self.__outputs = data['target']
        self.__output_names = data['target_names']
        selected_features_index = []
        feature_names = list(data['feature_names'])
        for i in range(len(input_features)):
            selected_feature_index = feature_names.index(input_features[i])
            selected_features_index.append(selected_feature_index)
        for i in range(len(inputs)):
            row = []
            for j in range(len(selected_features_index)):
                row.append(float(inputs[i][selected_features_index[j]]))
            self.__inputs.append(row)
        self.__output_names = list(range(len(self.__output_names)))

    def load_from_file(self, file):
        data = []
        data_names = []
        with open(file) as csv_file:
            csv_reader = csv.reader(csv_file, delimiter=',')
            line_count = 0
            for row in csv_reader:
                if line_count == 0:
                    data_names = row
                else:
                    data.append(row)
                line_count += 1
        self.__input_features = data_names[0]
        self.__inputs = [data[i][0] for i in range(len(data))]
        self.__outputs = [data[i][1] for i in range(len(data))]
        output_names = ['negative', 'positive']
        dict = {}
        for i in range(len(output_names)):
            dict[output_names[i]] = i
        for i in range(len(self.__outputs)):
            self.__outputs[i] = dict[self.__outputs[i]]
        self.__output_names = output_names


