import csv
import pandas as pd

from sklearn import datasets


class Repository:
    def __init__(self, loader, input_features):
        self.__inputs = []
        self.__outputs = []
        self.__input_features = input_features
        self.__output_names = []
        self.load_data(loader, input_features)

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
        feature_names = list(data['feature_names'])
        selected_features_index = []
        for i in range(len(input_features)):
            selected_feature_index = feature_names.index(input_features[i])
            selected_features_index.append(selected_feature_index)
        for i in range(len(inputs)):
            row = []
            for j in range(len(selected_features_index)):
                row.append(float(inputs[i][selected_features_index[j]]))
            self.__inputs.append(row)
