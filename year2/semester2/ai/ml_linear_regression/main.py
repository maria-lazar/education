from repository import Repository

from service import Service

# repository = Repository("data/2017.csv", ["Economy..GDP.per.Capita."])
repository = Repository("data/2017.csv", ["Economy..GDP.per.Capita.", "Freedom"])
# repository = Repository("data/2017.csv", ["Economy..GDP.per.Capita.", "Family", "Freedom"])
# repository = Repository("data/2017.csv", ["Economy..GDP.per.Capita.", "Family", "Freedom", "Health..Life.Expectancy."])
service = Service(repository)
service.solve_linear_regression()
