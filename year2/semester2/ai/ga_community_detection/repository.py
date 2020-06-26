import networkx as nx


def renumber(best_chr):
    dict = {best_chr[0]: 1}
    k = 1
    for i in range(len(best_chr)):
        if dict.has_key(best_chr[i]):
            best_chr[i] = dict[best_chr[i]]
        else:
            k += 1
            dict[best_chr[i]] = k
            best_chr[i] = k


class Repository:
    def __init__(self, file_name, gml=False):
        self.mat = []
        self.degrees = []
        self.edges = []
        self.num_nodes = 0
        if not gml:
            self.load_from_file(file_name)
        else:
            self.load_from_gml_file(file_name)

    # from txt file
    def load_from_file(self, file_name):
        f = open(file_name, "r")
        n = int(f.readline())
        self.num_nodes = n
        mat = []
        for i in range(n):
            mat.append([])
            line = f.readline()
            elements = line.split(" ")
            for j in range(n):
                mat[-1].append(int(elements[j]))
        self.mat = mat
        degrees = []
        num_edges = 0
        for i in range(n):
            d = 0
            for j in range(n):
                if mat[i][j] == 1:
                    d += 1
                if j > i:
                    num_edges += mat[i][j]
            degrees.append(d)
        self.edges = num_edges
        self.degrees = degrees
        f.close()

    def write_network(self, file_name, best_chr, all_best_fitness, all_best_com):
        f = open(file_name, "w")
        result = str(len(list(set(best_chr.repres)))) + "\n"
        renumber(best_chr.repres)
        for i in range(len(best_chr.repres)):
            result += str(i + 1) + " " + str(best_chr.repres[i]) + "\n"
        for el in all_best_fitness:
            result = result + " " + str(el)
        result += "\n"
        for el in all_best_com:
            result = result + " " + str(el)
        f.writelines(result)
        f.close()

    # from gml file
    def load_from_gml_file(self, file_name):
        g = nx.read_gml(file_name, label='id')
        n = len(g.node)
        self.num_nodes = n
        mat = []
        for i in range(n):
            a = [0 for j in range(n)]
            mat.append(a)
        for edge in g.edges:
            mat[edge[0] - 1][edge[1] - 1] = 1
            mat[edge[1] - 1][edge[0] - 1] = 1
        self.mat = mat
        degrees = []
        num_edges = 0
        for i in range(n):
            d = 0
            for j in range(n):
                if mat[i][j] == 1:
                    d += 1
                if j > i:
                    num_edges += mat[i][j]
            degrees.append(d)
        self.edges = num_edges
        self.degrees = degrees
