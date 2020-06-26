from sklearn.datasets import load_breast_cancer, load_iris

from repository import Repository
from service import Service

# repository = Repository(load_breast_cancer, ['mean radius', 'mean texture'])
repository = Repository(load_iris, ['sepal length (cm)', 'sepal width (cm)', 'petal length (cm)', 'petal width (cm)'])
service = Service(repository)
# service.solve_binary_classification()
service.solve_multiclass_classification()
