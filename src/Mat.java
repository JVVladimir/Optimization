
public class Mat {

    private int n, m;
    private double mat[][];

    Mat() {}

    Mat(int n, int m) {
        this.n = n;
        this.m = m;
        mat = new double[n][m];
        fill(0);
    }

    public static Mat inv(Mat A) {
        int i, j, k;
        int size = A.getN();
        if (A.getM() != size)
            return null;
        Mat E = Mat.ones(size);
        for (k = 0; k < size; k++) {
            for (j = k + 1; j < size; j++)
                A.mat[k][j] = A.mat[k][j] / A.mat[k][k];
            for (j = 0; j < size; j++)
                E.mat[k][j] = E.mat[k][j] / A.mat[k][k];
            A.mat[k][k] = A.mat[k][k] / A.mat[k][k];
            if (k > 0) {
                for (i = 0; i < k; i++) {
                    for (j = 0; j < size; j++)
                        E.mat[i][j] = E.mat[i][j] - E.mat[k][j] * A.mat[i][k];
                    for (j = size - 1; j >= k; j--)
                        A.mat[i][j] = A.mat[i][j] - A.mat[k][j] * A.mat[i][k];
                }
            }
            for (i = k + 1; i < size; i++) {
                for (j = 0; j < size; j++)
                    E.mat[i][j] = E.mat[i][j] - E.mat[k][j] * A.mat[i][k];
                for (j = size - 1; j >= k; j--)
                    A.mat[i][j] = A.mat[i][j] - A.mat[k][j] * A.mat[i][k];
            }
        }
        return E;
    }

    public double det() {
        double B[][] = new double[n][n];
        int row[] = new int[n];
        int hold, I_pivot;
        double pivot, abs_pivot, D = 1.0;
        if (m != n) {
            System.exit(-1);
            return -1;
        }
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                B[i][j] = mat[i][j];
        for (int k = 0; k < n; k++) {
            row[k] = k;
        }
        for (int k = 0; k < n - 1; k++) {
            pivot = B[row[k]][k];
            abs_pivot = Math.abs(pivot);
            I_pivot = k;
            for (int i = k; i < n; i++) {
                if (Math.abs(B[row[i]][k]) > abs_pivot) {
                    I_pivot = i;
                    pivot = B[row[i]][k];
                    abs_pivot = Math.abs(pivot);
                }
            }
            if (I_pivot != k) {
                hold = row[k];
                row[k] = row[I_pivot];
                row[I_pivot] = hold;
                D = -D;
            }
            if (abs_pivot < 1.0E-10)
                return 0;
            else {
                D = D * pivot;
                for (int j = k + 1; j < n; j++)
                    B[row[k]][j] = B[row[k]][j] / B[row[k]][k];
                for (int i = 0; i < n; i++)
                    if (i != k)
                        for (int j = k + 1; j < n; j++)
                            B[row[i]][j] = B[row[i]][j] - B[row[i]][k] * B[row[k]][j];
            }
        }
        return Math.round(D * B[row[n - 1]][n - 1]);
    }

    public static double max(Mat mat) {
        int n = mat.getN();
        int m = mat.getM();
        double max = mat.mat[0][0];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (max < mat.mat[i][j])
                    max = mat.mat[i][j];
        return max;
    }

    public static double min(Mat mat) {
        int n = mat.getN();
        int m = mat.getM();
        double min = mat.mat[0][0];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (min > mat.mat[i][j])
                    min = mat.mat[i][j];
        return min;
    }

    public double average() {
        double sum = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                sum += mat[i][j];
        return sum / (n + m);
    }

    public boolean search(int val) {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (mat[i][j] == val)
                    return true;
        return false;
    }

    public static Mat sort(Mat mat, String s) {
        Mat nMat = null;
        double mas[] = Mat.toMas(mat);
        if (s.equals("ascend") || s == null) {
            insertSort(mas, 0);
            nMat = Mat.fromMas(mas, mat.getN(), mat.getM());
            return nMat;
        } else if (s.equals("descend")) {
            insertSort(mas, 1);
            nMat = Mat.fromMas(mas, mat.getN(), mat.getM());
            return nMat;
        } else {
            return null;
        }
    }

    public static void insertSort(double mas[], int mode) {
        int n = mas.length, j;
        double a;
        for (int i = 1; i < n; i++) {
            a = mas[i];
            j = i;
            if (mode == 0)
                while (j > 0 && a <= mas[j - 1]) {
                    mas[j] = mas[j - 1];
                    j--;
                }
            else
                while (j > 0 && a >= mas[j - 1]) {
                    mas[j] = mas[j - 1];
                    j--;
                }
            mas[j] = a;
        }
    }

    public static int sum(Mat mat) {
        int n = mat.getN();
        int m = mat.getM();
        int sum = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                sum += mat.mat[i][j];
        return sum;
    }

    public void set(double... args) {
        int len = args.length;
        int k = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (k != len) {
                    this.set(i, j, args[k]);
                    k++;
                } else
                    return;
    }

    public static Mat pow(Mat mat, int p) {
        Mat nMat = Mat.copyMat(mat);
        if (p == 0) {
            nMat.fill(0);
            return nMat;
        }
        for (int k = 0; k < p - 1; k++)
            nMat = multiple(nMat, mat);
        return nMat;
    }

    public static boolean compare(Mat m1, Mat m2) {
        if (m1.getN() != m2.getN() || m1.getM() !=
                m2.getM())
            return false;
        int n = m1.getN();
        int m = m1.getM();
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (m1.mat[i][j] != m2.mat[i][j])
                    return false;
        return true;
    }

    public static Mat copyMat(Mat mat) {
        int n = mat.getN();
        int m = mat.getM();
        Mat nMat = new Mat(n, m);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                nMat.mat[i][j] = mat.mat[i][j];
        return nMat;
    }

    public static Mat multiple(Mat m1, Mat m2) {
        if (m1.getM() != m2.getN())
            return null;
        int n = m1.getN();
        int m = m2.getM();
        int gen = m1.getM();
        Mat m3 = new Mat(n, m);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                for (int s = 0; s < gen; s++)
                    m3.mat[i][j] += m1.mat[i][s] * m2.mat[s][j];
        return m3;
    }

    public static Mat multiple(Mat m1, double k) {
        int n = m1.getN();
        int m = m1.getM();
        Mat m3 = new Mat(n, m);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                m3.mat[i][j] = m1.mat[i][j] * k;
        return m3;
    }

    public static Mat sum(Mat m1, Mat m2) {
        if (m1.getN() != m2.getN() || m1.getM() != m2.getM())
            return null;
        int n = m1.getN();
        int m = m1.getM();
        Mat m3 = new Mat(n, m);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                m3.mat[i][j] = m1.mat[i][j] + m2.mat[i][j];
        return m3;
    }

    public static Mat diff(Mat m1, Mat m2) {
        if (m1.getN() != m2.getN() || m1.getM() != m2.getM())
            return null;
        int n = m1.getN();
        int m = m1.getM();
        Mat m3 = new Mat(n, m);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                m3.mat[i][j] = m1.mat[i][j] - m2.mat[i][j];
        return m3;
    }

    public static double[][] toStandartMatr(Mat mat) {
        int n = mat.getN();
        int m = mat.getM();
        double[][] matr = new double[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                matr[i][j] = mat.mat[i][j];
        return matr;
    }

    public static Mat fromStandartMatr(double matr[][]) {
        int n = matr.length;
        int m = matr[0].length;
        Mat nMat = new Mat(n, m);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                nMat.mat[i][j] = matr[i][j];
        return nMat;
    }

    public static Mat tranc(Mat mat) {
        int n = mat.getN();
        int m = mat.getM();
        Mat nMat = new Mat(m, n);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                nMat.mat[j][i] = mat.mat[i][j];
        return nMat;
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public void clear() {
        mat = null;
        n = m = -1;
    }

    public void set(int i, int j, double val) {
        mat[i][j] = val;
    }

    public double get(int i, int j) {
        return mat[i][j];
    }

    public static double[] toMas(Mat mat) {
        int n = mat.getN();
        int m = mat.getM();
        double mas[] = new double[n * m];
        for (int i = 0, k = 0; i < n; i++)
            for (int j = 0; j < m; j++, k++)
                mas[k] = mat.mat[i][j];
        return mas;
    }

    public static Mat fromMas(double[] mas, int n, int m) {
        Mat mat = new Mat(n, m);
        for (int i = 0, k = 0; i < n; i++)
            for (int j = 0; j < m; j++, k++)
                mat.mat[i][j] = mas[k];
        return mat;
    }

    public static double[] toMas(double[][] mat) {
        int n = mat.length;
        int m = mat[0].length;
        double mas[] = new double[n * m];
        for (int i = 0, k = 0; i < n; i++)
            for (int j = 0; j < m; j++, k++)
                mas[k] = mat[i][j];
        return mas;
    }

    public void fill(int value) {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                mat[i][j] = value;
    }

    public static Mat ones(int n) {
        Mat mat = new Mat(n, n);
        for (int i = 0; i < n; i++)
            mat.mat[i][i] = 1;
        return mat;
    }

    public static Mat zeros(int n) {
        Mat mat = new Mat(n, n);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                mat.mat[i][j] = 0;
        return mat;
    }

    public void print() {
        for (int i = 0; i < n; i++) {
            System.out.println();
            for (int j = 0; j < m; j++)
                System.out.print(mat[i][j] + " ");
        }
        System.out.println();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++)
                str.append(mat[i][j] + " ");
            str.append('\n');
        }
        return str.toString();
    }

    @Override
    public Mat clone() {
        Mat mat = Mat.copyMat(this);
        return mat;
    }

    public static Mat solveEquation(Mat matr, Vector res) {
        int raws = matr.getN();
        int resRaws = res.getN();
        if (matr.getN() != matr.getM() || matr.getN() != res.getN())
            return null;
        double det = matr.det();
        Vector ans = new Vector(resRaws);
        if (det != 0) {
            Mat matStart =
                    Mat.copyMat(matr);
            Mat mat = Mat.copyMat(matStart);
            for (int k = 0; k < resRaws; k++) {
                for (int i = 0; i < raws; i++)
                    mat.set(i, k, res.get(i));
                double det2 = mat.det();
                if (det2 != 0)
                    ans.set(k,det2 / det);
                else
                    ans.set(k, 0);
                mat = Mat.copyMat(matStart);
            }
            return ans;
        } else
            return null;
    }

}
