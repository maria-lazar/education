import csv
from sklearn import datasets


class Repository:
    def __init__(self, file=None, features=None):
        self.__inputs = []
        self.__outputs = []
        self.__input_features = features
        if file is not None:
            self.load_data(file, features, "Happiness.Score")
        else:
            # multi-target data
            self.load_multi_data()

    def load_data(self, file, features, output_var_name):
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
        selected_variables_index = []
        for i in range(len(features)):
            selected_variable_index = data_names.index(features[i])
            selected_variables_index.append(selected_variable_index)
        for i in range(len(data)):
            row = []
            for j in range(len(selected_variables_index)):
                row.append(float(data[i][selected_variables_index[j]]))
            self.__inputs.append(row)
        selected_output_index = data_names.index(output_var_name)
        self.__outputs = [float(data[i][selected_output_index]) for i in range(len(data))]

    def get_inputs(self):
        return self.__inputs

    def get_outputs(self):
        return self.__outputs

    def get_input_features(self):
        return self.__input_features

    def load_multi_data(self):
        x, y = datasets.load_linnerud(return_X_y=True)
        self.__inputs = x.tolist()
        self.__outputs = y.tolist()
