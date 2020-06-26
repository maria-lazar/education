from sklearn.datasets import load_iris

from repository import Repository
from service import Service

repository = Repository(load_iris, ['sepal length (cm)', 'sepal width (cm)', 'petal length (cm)', 'petal width (cm)'])
# repository = Repository("data/reviews_mixed.csv", file=True)
service = Service(repository)
# service.classification_text()
service.my_kmeans_numeric()
# service.kmeans_with_tool_numeric()
