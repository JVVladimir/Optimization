import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by Владимир on 27.01.2018.
 * Класс с методами нулевого порядка
 */
public class MethodsOfOrderZero {


    /*double[] res3 = MethodsOfOrderZero.countMin(nodes,0,res,(ind-1),target,10000,0,50);
        System.out.println(Arrays.toString(res3));
        double[] res4 = MethodsOfOrderZero.countMin(nodes,0,res,(ind-1),target,0.01,0,50);
        System.out.println(Arrays.toString(res4));*/
        /*System.out.println(Arrays.toString(MethodsOfOrderZero.countMinGoldenSection(-10, 10,40)));
        System.out.println(Arrays.toString(MethodsOfOrderZero.polynom(-10,0.001)));*/



    /*
    * Метод равномерно поиска
    * принимает матрицу проводимостей
    * список проводимостей, номер интересующей проводимости, массив результата и интересующий номер потенциала узла
    * кол-во делений отрезка, левая и правая границы
    * Функция возвращает: найдённое значение проводимости и напряжение
    * */
   /* public static double[] countMin(double[] provod, int indP, double[] res, int indR, double target, int n, double left, double right) {
        System.out.println("****Метод равномерного поиска****");
        int len = provod.length / 2;
        int i = 1;
        // Текущее напряжение
        double U, X;
        double[] mas = new double[len];
        double[][] matr = null;
        matr = Main.createLinearSystem(provod);
        mas = Main.solveLinearSystem(len, matr, res);
        U = mas[indR];
        while (i <= n && Math.pow(U - target, 2) > 0.01) {
            X = left + i * (right - left) / (n + 1);
            provod[indP] = X;
            matr = Main.createLinearSystem(provod);
            mas = Main.solveLinearSystem(len, matr, res);
            U = mas[indR];
            i++;
        }
        return new double[]{provod[indP], U};
    }*/

    /*
    * Метод деления пополам поиска
    * принимает матрицу проводимостей
    * список проводимостей, номер интересующей проводимости, массив результата и интересующий номер потенциала узла
    * невязка, левая и правая границы
    * Функция возвращает: найдённое значение проводимости и напряжение
    * */

   /* public static double[] countMin(double[] provod, int indP, double[] res, int indR, double target, double eps, double left, double right) {
        System.out.println("****Метод деления пополам****");
        int len = provod.length / 2;
        // Текущее напряжение
        double[] mas = new double[len];
        double[][] matr = null;
        provod[indP] = right;
        matr = Main.createLinearSystem(provod);
        mas = Main.solveLinearSystem(len, matr, res);
        double U = mas[indR], Umid, X;
        double mid = 0;
        while (Math.pow(U - target, 2) > eps) {
            mid = (right - left) / 2;
            provod[indP] = mid;
            matr = Main.createLinearSystem(provod);
            mas = Main.solveLinearSystem(len, matr, res);
            Umid = mas[indR];
            if (Math.pow(Umid - target, 2) < Math.pow(U - target, 2))
                right = mid;
            else
                left = mid;
            U = Umid;
        }
        return new double[]{provod[indP], U};
    }*/

    /*
    * Метод золотого сечения
     */
    public static double[] countMinGoldenSection(double a, double b, int n) {
        System.out.println("****Метод золотого сечения****");
        double X1, X2, F1, F2;
        X1 = a + (b - a) * (3 - sqrt(5)) / 2; // рассчитывается из значения параметров
        X2 = a + (b - a) * (sqrt(5) - 1) / 2;
        F1 = F(X1);
        F2 = F(X2);
        for (int i = 0; i <= n; i++) {
            if (F1 <= F2) {
                b = X2;
                X2 = X1;
                F2 = F1;
                X1 = a + (b - a) * (3 - sqrt(5)) / 2;
                F1 = F(X1);
            } else {
                a = X1;
                X1 = X2;
                F1 = F2;
                X2 = a + (b - a) * (sqrt(5) - 1) / 2;
                F2 = F(X2);
            }
        }
        System.out.println("Кол-во шагов: " + n);
        return new double[]{X2, (F1 - F2) / 2 + F2};
    }


    /*
    * Метод парабол
    * */
    public static double[] polynom(double x, double eps) {
        System.out.println("****Метод парабол****");
        int steps = 0;
        double h = 0.001;
        if (x == 0)
            x += 0.1;
        while ((F(x + h) - 2 * F(x) + F(x - h)) / (h * h) <= 0)
            x += 0.1;
        double x1;
        x1 = x - 0.5 * h * (F(x + h) - F(x - h)) / (F(x + h) - 2 * F(x) + F(x - h));
        while (Math.abs(x1 - x) > eps) {
            steps++;
            x = x1;
            x1 = x - 0.5 * h * (F(x + h) - F(x - h)) / (F(x + h) - 2 * F(x) + F(x - h));
        }
        System.out.println("Кол-во шагов: " + steps);
        return new double[]{x1, F(x1)};
    }

    public static double[] interval() {
        System.out.println("Определяем априорный интервал");
        double a = 0, b = 0;
        double x0 = 0, delta = 0.0001;
        int N = 100;
        double x1 = x0-delta, x2 = x0+delta;
        if (F(x1) > F(x0) && F(x0) < F(x2)) {
            System.out.println("Интервал: " + x1 + " - " + x2);
            a = x1;
            b = x2;
            N = 0;
        } else if (F(x1) < F(x0) && F(x0) > F(x2)) {
            System.out.println("На заданном интервале функция не унимодальна!");
            return new double[] {0,0};
        } else if (F(x1) > F(x0) && F(x0) > F(x2)) {
            x1 = x2;
        } else if (F(x1) < F(x0) && F(x0) < F(x2)) {
            delta = -delta;
            x1 = x2;
        }

        x0 = x1;
        for (int i = 1; i<N; i++) {
            x2 = x1 + pow(2,i)*delta;
            if (F(x2)<F(x1)) {
                a = x0;
                b = x2;
            }
            else{
                a = x2;
                b = x0;
                break;
            }
            x0 = x1;
            x1 = x2;

        }
        System.out.println(a+ " " + b);
        return new double[] {a,b};
}

    public static double devide(double a, double b, double eps, double g, int N) {
        for(int i = 0; i < N && Math.abs(b-a)>eps; i++) {
            double x1 = (a+b-g)/2;
            double x2 = (a+b+g)/2;
            double F1 = F(x1);
            double F2 = F(x2);
            if(F1 < F2)
                b = x2;
            else if(F1>F2)
                a = x1;
            else {
                a = x1;
                b = x2;
            }
        }
        return (a+b)/2;
    }

    public static double F(double x) {
        return 3*pow(x,2)-20*(x-8)-10;
    }
}

