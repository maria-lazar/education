import matplotlib.pyplot as plt
import numpy as np


# Plotting methods for linear regression problem with 1(simple) and 2(multiple) features


# plot data split(simple): training data and test data
def plot_data_split_simple(train_inputs, train_outputs, test_inputs, test_outputs, labels):
    plt.plot(train_inputs, train_outputs, 'ro',
             label="train")
    plt.plot(test_inputs, test_outputs, 'go',
             label="test")
    plt.title("Train and test data")
    plt.xlabel(labels[0])
    plt.ylabel(labels[1])
    plt.legend()
    plt.show()


# plot data split(multiple): training data and test data
def plot_data_split_multiple(train_inputs, train_outputs, test_inputs, test_outputs, labels):
    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    x1_train = [train_inputs[i][0] for i in range(len(train_inputs))]
    x2_train = [train_inputs[i][1] for i in range(len(train_inputs))]
    y_train = [train_outputs[i] for i in range(len(train_outputs))]
    x1_test = [test_inputs[i][0] for i in range(len(test_inputs))]
    x2_test = [test_inputs[i][1] for i in range(len(test_inputs))]
    y_test = [test_outputs[i] for i in range(len(test_outputs))]
    val1 = ax.scatter(x1_train, x2_train, y_train, c='r', marker='o')
    val2 = ax.scatter(x1_test, x2_test, y_test, c='g', marker='o')

    ax.set_xlabel(labels[0])
    ax.set_ylabel(labels[1])
    ax.set_zlabel(labels[2])
    plt.title("Train and test data")
    plt.legend([val1, val2], ["train", "test"])
    plt.show()


# plot training data and learnt model(simple)
def plot_model_simple(train_inputs, train_outputs, b1, b0, labels):
    inp = []
    for i in range(len(train_inputs)):
        inp = [*inp, *train_inputs[i]]

    x = np.linspace(min(inp), max(inp), 1000)
    y = [b0 + b1 * x[i] for i in range(len(x))]
    plt.plot(train_inputs, train_outputs, 'ro',
             label="train")
    plt.plot(x, y, 'b-', label="model")
    plt.title("Train data and model")
    plt.xlabel(labels[0])
    plt.ylabel(labels[1])
    plt.legend()
    plt.show()


# plot training data and learnt model(multiple)
def plot_model_multiple(train_inputs, train_outputs, b2, b1, b0, labels):
    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')

    x1 = [train_inputs[i][0] for i in range(len(train_inputs))]
    x2 = [train_inputs[i][1] for i in range(len(train_inputs))]
    y = [train_outputs[i] for i in range(len(train_outputs))]
    ax.scatter(x1, x2, y, c='r', marker='o')

    x1_l = np.linspace(min(x1), max(x1), 1000)
    x2_l = np.linspace(min(x2), max(x2), 1000)
    X1, X2 = np.meshgrid(x1_l, x2_l)
    Y = b0 + b1 * X1 + b2 * X2
    ax.plot_surface(X1, X2, Y)

    ax.set_xlabel(labels[0])
    ax.set_ylabel(labels[1])
    ax.set_zlabel(labels[2])
    plt.title("Train data and model")
    plt.show()


# plot test results(simple)
def plot_test_results_simple(test_inputs, test_outputs, computed_test_outputs, labels):
    plt.plot(test_inputs, computed_test_outputs, 'go',
             label="computed")
    plt.plot(test_inputs, test_outputs, 'bo',
             label="real")
    plt.title("Test computed and real data")
    plt.xlabel(labels[0])
    plt.ylabel(labels[1])
    plt.legend()
    plt.show()


# plot test results(multiple)
def plot_test_results_multiple(test_inputs, test_outputs, computed_test_outputs, labels):
    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    x1 = [test_inputs[i][0] for i in range(len(test_inputs))]
    x2 = [test_inputs[i][1] for i in range(len(test_inputs))]
    y = [test_outputs[i] for i in range(len(test_outputs))]
    y2 = [computed_test_outputs[i] for i in range(len(computed_test_outputs))]
    val1 = ax.scatter(x1, x2, y, c='b', marker='o')
    val2 = ax.scatter(x1, x2, y2, c='g', marker='o')

    ax.set_xlabel(labels[0])
    ax.set_ylabel(labels[1])
    ax.set_zlabel(labels[2])
    plt.title("Test computed and real data")
    plt.legend([val1, val2], ["real", "computed"])
    plt.show()

