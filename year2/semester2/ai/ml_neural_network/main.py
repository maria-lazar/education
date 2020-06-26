from sklearn.datasets import load_digits, load_iris
from sklearn.metrics import log_loss

from my_neural_network import MyNeuralNetwork
from repository import Repository
from service import Service

# repository = Repository(load_iris, ['sepal length (cm)', 'sepal width (cm)', 'petal length (cm)', 'petal width (cm)'])
# repository = Repository(load_digits)
# service = Service(repository)
# service.my_nn(problem="digits")
# service.my_nn(problem="iris")
# service.nn_with_tool(problem="iris")
# service.nn_with_tool(problem="digits")
repo = Repository()
service = Service(repo)
service.nn_images_sepia()
