
from repository import Repository
from service import Service


def modularity(communities):
    no_nodes = repository.num_nodes
    mat = repository.mat
    degrees = repository.degrees
    no_edges = repository.edges
    m = float(2 * no_edges)
    q = 0.0
    for i in range(0, no_nodes):
        for j in range(0, no_nodes):
            c = float((degrees[i] * degrees[j]) / m)
            var = mat[i][j] - c
            if communities[i] == communities[j]:
                q = q + var
    return q / m


repository = Repository("data/football.gml", True)
# repository = Repository("data/dolphins.gml", True)
service = Service(repository)
service.find_communities(modularity, pop_size=100, gen=200, m_rate=0.01, elite=10)
# in network_out.txt sunt rezultatele

