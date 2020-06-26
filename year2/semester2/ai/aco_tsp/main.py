from repository import Repository
from service import Service

repository = Repository("data/berlin.txt", True)
service = Service(repository)
service.solve_aco_tsp()

