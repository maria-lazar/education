import matplotlib.pyplot as plt


# plot classification data for problem with 2 input features
def plot_classification_data(inputs, outputs, input_features, output_names):
    labels = set(outputs)
    for label in labels:
        x1 = [inputs[i][0] for i in range(len(inputs)) if outputs[i] == label]
        x2 = [inputs[i][1] for i in range(len(inputs)) if outputs[i] == label]
        plt.scatter(x1, x2, label=output_names[label])
    plt.xlabel(input_features[0])
    plt.ylabel(input_features[1])
    plt.legend()
    plt.show()


# plot predictions for problem with 2 input features
def plot_predictions(inputs, real_outputs, computed_outputs, feature_names, label_names):
    labels = list(set(real_outputs))
    no_samples = len(inputs)
    for label in labels:
        x = [inputs[i][0] for i in range(no_samples) if real_outputs[i] == label and computed_outputs[i] == label]
        y = [inputs[i][1] for i in range(no_samples) if real_outputs[i] == label and computed_outputs[i] == label]
        plt.scatter(x, y, label=label_names[label] + ' (correct)')
    for label in labels:
        x = [inputs[i][0] for i in range(no_samples) if real_outputs[i] == label and computed_outputs[i] != label]
        y = [inputs[i][1] for i in range(no_samples) if real_outputs[i] == label and computed_outputs[i] != label]
        plt.scatter(x, y, label=label_names[label] + ' (incorrect)')
    plt.xlabel(feature_names[0])
    plt.ylabel(feature_names[1])
    plt.legend()
    plt.show()