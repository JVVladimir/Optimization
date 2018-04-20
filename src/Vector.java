
public class Vector extends Mat {

    private static final int M = 0;

    Vector(int n) {
        super(n, 1);
    }

    public void set(int i, double val) {
        super.set(i, 0, val);
    }

    public double get(int i) {
        return super.get(i, M);
    }

    public static Vector copyMat(Vector mat) {
        int n = mat.getN();
        Vector nMat = new Vector(n);
        for (int i = 0; i < n; i++)
            nMat.set(i, mat.get(i));
        return nMat;
    }

    public Vector clone() {
        Vector vector = Vector.copyMat(this);
        return vector;
    }

    public Vector getVector(Mat mat) {
        Vector v = new Vector(mat.getN());
        for (int i = 0; i < mat.getN(); i++)
            v.set(i, mat.get(i, M));
        return v;
    }

}
