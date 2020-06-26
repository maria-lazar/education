from repository import Repository
from service import Service

# repository = Repository()
repository = Repository("data/2017.csv", ["Economy..GDP.per.Capita."])
# repository = Repository("data/2017.csv", ["Economy..GDP.per.Capita.", "Freedom"])
# repository = Repository("data/2017.csv", ["Economy..GDP.per.Capita.", "Family"])
# repository = Repository("data/2017.csv", ["Economy..GDP.per.Capita.", "Family", "Freedom"])
# repository = Repository("data/2017.csv", ["Economy..GDP.per.Capita.", "Family", "Freedom",
# "Health..Life.Expectancy."])
service = Service(repository)
# service.solve_gd_regression(gd_type="b", iterations=1500, learning_rate=0.001)
# service.solve_gd_regression(gd_type="m", iterations=500, learning_rate=0.01, batch_size=32)
service.solve_gd_regression(iterations=500, learning_rate=0.001)
# service.solve_gd_multi_regression(iterations=1500, learning_rate=0.01)
