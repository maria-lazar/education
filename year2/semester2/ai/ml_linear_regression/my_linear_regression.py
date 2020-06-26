class MyLinearRegression:
    def __init__(self):
        self.b = []
        self.intercept = 0.0

    def fit(self, x, y):
        copy_x = []
        for i in range(len(x)):
            row = x[i][:]
            row.append(1)
            copy_x.append(row)
        y_transpose = self.transpose([y])
        x_transpose = self.transpose(copy_x)
        inv = self.inverse(self.multiply(x_transpose, copy_x))
        val = self.multiply(x_transpose, y_transpose)
        b = self.multiply(inv, val)
        for i in range(len(b) - 1):
            self.b += b[i]
        self.intercept = b[-1][0]

    def transpose(self, x):
        mat = []
        for i in range(len(x[0])):
            col = [x[j][i] for j in range(len(x))]
            mat.append(col)
        return mat

    def multiply(self, x, y):
        mat = []
        for i in range(len(x)):
            row = []
            for j in range(len(y[0])):
                s = 0
                for k in range(len(x[0])):
                    s += x[i][k] * y[k][j]
                row.append(s)
            mat.append(row)
        return mat

    def inverse(self, x):
        identity = []
        for i in range(len(x)):
            row = [0 for j in range(len(x))]
            row[i] = 1
            identity.append(row)
        mat = x[:]
        for i in range(len(mat)):
            val1 = 1.0 / mat[i][i]
            for j in range(len(mat)):
                mat[i][j] *= val1
                identity[i][j] *= val1
            for k in range(len(mat)):
                if k != i:
                    val2 = mat[k][i]
                    for j in range(len(mat)):
                        mat[k][j] = mat[k][j] - val2 * mat[i][j]
                        identity[k][j] = identity[k][j] - val2 * identity[i][j]
        return identity

    def predict(self, x):
        y = []
        for i in range(len(x)):
            s = self.intercept
            for j in range(len(x[0])):
                s += x[i][j] * self.b[j]
            y.append(s)
        return y
