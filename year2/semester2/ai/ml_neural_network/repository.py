import numpy as np
from PIL import Image
from sklearn.datasets import load_digits


class Repository:
    def __init__(self, loader=None, input_features=None):
        self.__inputs = []
        self.__outputs = []
        self.__input_features = input_features
        self.__output_names = []
        if loader is not None:
            if loader == load_digits:
                self.load_digits()
            else:
                self.load_data(loader, input_features)
        else:
            self.load_images()

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

    def load_digits(self):
        data = load_digits()
        no = data.images.shape
        # self.__inputs = data.images.reshape(no[0], no[1] * no[2]).tolist()
        self.__inputs = data.images.reshape(no[0], no[1] * no[2])[:500].tolist()
        self.__outputs = data.target[:500].tolist()
        # self.__outputs = data.target.tolist()
        self.__output_names = data.target_names

    def load_images(self):
        for i in range(4101, 4300):
            img = Image.open("cats/cat." + str(i) + ".jpg")
            pixels = np.array(img.getdata())
            self.__inputs.append(pixels.reshape(-1).tolist())
            if i < 4202:
                self.__outputs.append(0)
            else:
                self.__outputs.append(1)
        self.__output_names = [0, 1]
