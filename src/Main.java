import java.util.ArrayList;
import java.util.Scanner;


// метод конфиг (с одном. минимизацией), покоординатный спуск, сопряжённый направления(гессиан)
public class Main {

    public static void main(String args[]) throws Exception {
        /*double[] in = MethodsOfOrderZero.interval();
        double res = MethodsOfOrderZero.devide(in[0], in[1],0.001,0.01,200);
        System.out.println(res+"     " + MethodsOfOrderZero.F(res));
        double[] res2= MethodsOfOrderZero.countMinGoldenSection(in[0], in[1], 200);
        System.out.println(res2[0] + "    "+ res2[1]);
        double[] res3 = MethodsOfOrderZero.polynom(in[1], 0.001);
        System.out.println(res3[0]+"   " + res3[1]);*/
        Scanner scan = new Scanner(System.in);
        System.out.println("****Начало работы програмы****");
        System.out.print("Введите кол-во узлов в схеме: ");
        // Кол-во узлов - число потенциалов в схеме
        int nNodes = scan.nextInt();
        Vector nodes = new Vector(2 * nNodes);
        int i = 0;
        while (i < 2 * nNodes) {
            System.out.print("Введите проводимость Y" + i + ": ");
            nodes.set(i, scan.nextDouble());
            i++;
        }
        // Создание вектор-столбца решений
        System.out.print("Введите напряжение источника питания: ");
        double E = scan.nextDouble();
        Vector res = new Vector(nNodes);
        res.fill(0);
        res.set(0, E * nodes.get(0));
        // Создание матрицы для решения системы
        Mat matr = createLinearSystem(nodes);
        // Вывод полученного матричного уравнения
        System.out.println("****Полученное матричное уравнение****");
        printMatrEquation(nNodes, matr, res);
        // Рассчитываем корни уравнений
        Vector mas = solveLinearSystem(nNodes, matr, res);
        // Ввод значения напряжения, которое хотим получить (с чем потом будем сравнивать)
        System.out.print("Введите число от 1 до " + nNodes
                + " соответсвующее варируемому напряжению и его значение: ");
        int ind = scan.nextInt();
        System.out.println("Полученное напряжение: " + mas.get(ind - 1));
        System.out.print("Введите необходимое напряжение: ");
        double target = scan.nextDouble();
        System.out.println("Напряжение U" + ind + " = " + target + "\n\n");
        // Выводим результат вычислений
        printSolution(mas, (ind - 1), target);
        System.out.println("****Работа первой части программы завершена****");
        Timer.start();
        Result res1 = useGradient(nodes, matr, (ind - 1), target, res, 0.002, 0.001);
        Timer.stop();
        String t1 = Timer.getString();
        System.out.println("\n*****Применение градиентного спуска*****");
        System.out.println(res1.getResults()+"Затраченное время: " + t1);
        Timer.start();
        Result res2 = useNewton(nodes, matr, (ind - 1), target, res, 0.05, 0.001);
        Timer.stop();
        String t2 = Timer.getString();
        /*Timer.start();
        Result res3 = useLM(nodes, matr, (ind - 1), target, res, 0.0001, 0.001);
        Timer.stop();
        String t3 = Timer.getString();
        Graph g = new Graph();
        //printMatrEquation(nNodes, matr2, res);
        /*System.out.println("\n*****Применение градиентного спуска*****");
        System.out.println(res1.getResults()+"Затраченное время: " + t1);
        Graph g = new Graph();
        g.setGraph(res1.getFunction(), res1.getErrors(), target);
        System.out.println("\n*****Применение метода Ньютона*****");
        System.out.println(res2.getResults() + "Затраченное время: " + t2);
        Graph g2 = new Graph();
        g2.setGraph(res2.getFunction(), res2.getErrors(), target);*/
       /* System.out.println("\n*****Применение метода LM*****");
        System.out.println(res3.getResults() + "Затраченное время: " + t3);
        Graph g3 = new Graph();
        g3.setGraph(res3.getFunction(), res3.getErrors(), target);
        g.show();*/
    }

    /*
    * Градиентный спуск
    */
    public static Result useGradient(Vector nodes2, Mat matr2, int ind, double target, Vector res2, double momentum, double eps) {
        // Списки для хранения всех значений функции и ошибок
        ArrayList<Double> function = new ArrayList<>();
        ArrayList<Double> errors = new ArrayList<>();
        // Копирование всех данных для изменения
        Vector nodes = nodes2.clone();
        Mat matr = matr2.clone();
        Vector res = res2.clone();
        int nNodes = nodes.getN() / 2;
        Vector mas = solveLinearSystem(nNodes, matr, res);
        // Автоматическое определение направления градиента (max or min)
        if (target < mas.get(ind))
            momentum *= -1;
        // Вектор градиента
        Vector grad = new Vector(nodes.getN());
        int steps = 0;
        double Y, Y2, U = mas.get(ind), U2, Proiz, Error = Math.pow(target - mas.get(ind), 2);
        while (Error > eps) {
            // Находим градиент по всем проводимостям на данной итерации
            for (int i = 0; i < nodes.getN(); i++) {
                // Изменение аргумента
                Y = nodes.get(i);
                Y2 = Y * 1.03;
                nodes.set(i, Y2);
                // Изменение функции
                matr = createLinearSystem(nodes);
                mas = solveLinearSystem(nNodes, matr, res);
                U2 = mas.get(ind);
                // Подсчёт градиента для данной проводимости
                Proiz = (U2 - U) / (Y2 - Y);
                // Записываем производную в градиент
                grad.set(i, Proiz);
                nodes.set(i, Y);
            }
            // Изменение всех проводимостей
            for (int i = 0; i < nodes.getN(); i++)
                nodes.set(i, nodes.get(i) + momentum * grad.get(i));
            // Пересчитываем уравнение, применяя посчитанный градиент
            matr = createLinearSystem(nodes);
            mas = solveLinearSystem(nNodes, matr, res);
            U = mas.get(ind);
            Error = Math.pow(target - U, 2);
            // System.out.println();
            // Сохраняем значение ошибки и функции
            function.add(U);
            errors.add(Error);
            /* for(int k = 0; k<nodes.length; k++)
                nodes[k]*=proizError(target,U);*/
            steps++;
        }
        return new Result(nodes, matr, U, Error, function, errors, steps);
    }

    /*
    * Метод Ньютона
    * */
    public static Result useNewton(Vector nodes2, Mat matr2, int ind, double target, Vector res2, double momentum, double eps) {
        // Списки для хранения всех значений функции и ошибок
        ArrayList<Double> function = new ArrayList<>();
        ArrayList<Double> errors = new ArrayList<>();
        int len = nodes2.getN();
        Vector nodes = nodes2.clone();
        Mat matr = matr2.clone();
        Vector res = res2.clone();
        int nNodes = len / 2;
        Vector mas = solveLinearSystem(nNodes, matr, res);
        // Автоматическое определение направления градиента (max or min)
        if (target < mas.get(ind))
            momentum *= -1;
        // Вектор градиента и матрица Гёссе
        Vector grad = new Vector(len);
        Mat gessian = new Mat(len, len);
        int steps = 0;
        double Y, Y2, Y3, U = mas.get(ind), U2, U3, Error = Math.pow(target - mas.get(ind), 2);
        while (Error > eps) {
            // Находим градиент по всем проводимостям на данной итерации
            for (int i = 0; i < len; i++) {
                // Изменение аргумента
                Y = nodes.get(i);
                Y2 = Y * 1.03;
                double hx = Y2 - Y;
                nodes.set(i, Y + hx);
                // Изменение функции
                matr = createLinearSystem(nodes);
                mas = solveLinearSystem(nNodes, matr, res);
                U2 = mas.get(ind);
                grad.set(i, (U2 - U) / hx);
                nodes.set(i, Y - hx);
                matr = createLinearSystem(nodes);
                mas = solveLinearSystem(nNodes, matr, res);
                U3 = mas.get(ind);
                // Вычисляем вторую производную
                gessian.set(i, i, (U2 - 2 * U + U3) / (hx * hx));
                nodes.set(i, Y);
                for (int j = 0; j < len; j++) {
                    if (j != i) {
                        // f(x+hx,y+hy)
                        nodes.set(i, Y + hx);
                        double Y5 = nodes.get(j);
                        Y3 = Y5 * 1.03;
                        double hy = Y3 - Y5;
                        nodes.set(j, Y5 + hy);
                        matr = createLinearSystem(nodes);
                        mas = solveLinearSystem(nNodes, matr, res);
                        double F1 = mas.get(ind);
                        // f(x-hx, y-hy)
                        nodes.set(i, Y - hx);
                        nodes.set(j, Y5 - hy);
                        matr = createLinearSystem(nodes);
                        mas = solveLinearSystem(nNodes, matr, res);
                        double F4 = mas.get(ind);
                        // f(x-hx, y + hy)
                        nodes.set(j, Y5 + hy);
                        matr = createLinearSystem(nodes);
                        mas = solveLinearSystem(nNodes, matr, res);
                        double F3 = mas.get(ind);
                        // f(x+hx, y - hy)
                        nodes.set(i, Y + hx);
                        nodes.set(j, Y5 - hy);
                        matr = createLinearSystem(nodes);
                        mas = solveLinearSystem(nNodes, matr, res);
                        double F2 = mas.get(ind);
                        // Вычисляем смешанную производную
                        gessian.set(i, j, (F1 - F2 - F3 + F4) / (4 * hx * hy));
                        nodes.set(j, Y5);
                    }
                }
                nodes.set(i, Y);
            }
            // Обращаем гессиан и умножаем его на градиент, затем вычитаем из проводимостей
            nodes = nodes.getVector(Mat.sum(nodes, Mat.multiple(Mat.multiple(Mat.inv(gessian), grad), momentum)));
            // Пересчитываем систему
            matr = createLinearSystem(nodes);
            mas = solveLinearSystem(nNodes, matr, res);
            Error = Math.pow(target - mas.get(ind), 2);
            U = mas.get(ind);
            // Сохранение значений функции и ошибки
            function.add(U);
            errors.add(Error);
            /* for(int k = 0; k<nodes.length; k++)
                nodes[k]*=proizError(target,U);*/
            steps++;
        }
        return new Result(nodes, matr, U, Error, function, errors, steps);
    }

    /*
    * Метод Левенберга — Марквардта
    */
    public static Result useLM(Vector nodes2, Mat matr2, int ind, double target, Vector res2, double momentum, double eps) {
        // Списки для хранения всех значений функции и ошибок
        ArrayList<Double> function = new ArrayList<>();
        ArrayList<Double> errors = new ArrayList<>();
        int len = nodes2.getN();
        Vector nodes = nodes2.clone();
        Mat matr = matr2.clone();
        Vector res = res2.clone();
        int nNodes = len / 2;
        Vector mas = solveLinearSystem(nNodes, matr, res);
        // Автоматическое определение направления градиента (max or min)
        if (target < mas.get(ind))
            momentum *= -1;
        // Вектор градиента и матрица Гёссе
        Vector grad = new Vector(len);
        int steps = 0;
        double Y, Y2, Y3, U = mas.get(ind), U2, U3, Error = Math.pow(target - mas.get(ind), 2);
        while (Error > eps) {
            // Находим градиент по всем проводимостям на данной итерации
            for (int i = 0; i < len; i++) {
                // Изменение аргумента
                Y = nodes.get(i);
                Y2 = Y * 1.03;
                double hx = Y2 - Y;
                nodes.set(i, Y + hx);
                // Изменение функции
                matr = createLinearSystem(nodes);
                mas = solveLinearSystem(nNodes, matr, res);
                U2 = mas.get(ind);
                grad.set(i, (U2 - U) / hx);
                nodes.set(i, Y);
            }
            // Обращаем гессиан и умножаем его на градиент, затем вычитаем из проводимостей
            nodes = nodes.getVector(Mat.sum(nodes, Mat.multiple(Mat.multiple(grad,Mat.multiple(Mat.tranc(grad), grad)), momentum)));
            // Пересчитываем систему
            matr = createLinearSystem(nodes);
            mas = solveLinearSystem(nNodes, matr, res);
            Error = Math.pow(target - mas.get(ind), 2);
            U = mas.get(ind);
            // Сохранение значений функции и ошибки
            function.add(U);
            errors.add(Error);
            /* for(int k = 0; k<nodes.length; k++)
                nodes[k]*=proizError(target,U);*/
            steps++;
        }
        return new Result(nodes, matr, U, Error, function, errors, steps);
    }

    public static double proizError(double target, double current) {
        return 2 * (target - current);
    }

    // Создание матриц для решения системы уравнений
    public static Mat createLinearSystem(Vector nodes) {
        int n = nodes.getN(), k = 0;
        Mat res = new Mat(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j && k + 2 != n) {
                    res.set(i, i, nodes.get(k) + nodes.get(k + 1) + nodes.get(k + 2));
                    k += 2;
                } else if (i == j)
                    res.set(i, i, nodes.get(k) + nodes.get(k + 1));
                else if (i != j && k % 2 == 0 && k != 0) {
                    res.set(i, j, -nodes.get(k));
                } else {
                    res.set(i, j, 0);
                }
            }
        }
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (j > 1 + i)
                    res.set(i, j, 0);
                if (i > 1 + j)
                    res.set(i, j, 0);
            }
        }
        Mat mat = new Mat(n / 2, n / 2);
        for (int i = 0; i < n / 2; i++)
            for (int j = 0; j < n / 2; j++)
                mat.set(i, j, res.get(i, j));
        return mat;
    }

    // Вывод полученного матричного уравнения
    public static void printMatrEquation(int n, Mat A, Vector b) {
        int i, j, k;
        for (i = 0; i < n; i++)
            for (j = 0; j < n; j++) {
                if (n % 2 == 0) k = n / 2;
                else k = (int) n / 2;
                if (j == 0)
                    System.out.print("|");
                if (i == k && j == (n - 1)) {
                    k = n / 2;
                    System.out.printf("%6.2f", (float) A.get(i, j));
                    System.out.print("|" + " X ");
                    System.out.printf("%4s", "u" + (i + 1));
                    System.out.print(" = ");
                    System.out.printf("%4s%4.2f", "|", (float) b.get(i));
                    System.out.print("|\n");
                } else if (j == (n - 1)) {
                    System.out.printf("%6.2f", (float) A.get(i, j));
                    System.out.print("| ");
                    System.out.printf("%6s", "u" + (i + 1));
                    System.out.printf("%7s%4.2f", "|", (float) b.get(i));
                    System.out.print("|\n");
                } else
                    System.out.printf("%6.2f ", (float) A.get(i, j));
            }
    }

    // Решение системы уравнений
    public static Vector solveLinearSystem(int n, Mat A, Vector b) {
        Mat U = new Mat(n, n);
        Vector x = new Vector(n);
        Vector y = new Vector(n);
        Vector mas = new Vector(n);
        int k, i, j;
        double temp;
        for (i = 0; i < n; i++)
            for (j = 0; j < n; j++)
                U.set(i, j, 0);
        for (i = 0; i < n; i++) {
            temp = 0;
            for (k = 0; k < i; k++)
                temp = temp + U.get(k, i) * U.get(k, i);
            U.set(i, i, (float) (Math.sqrt(A.get(i, i) - temp)));
            for (j = i; j < n; j++) {
                temp = 0;
                for (k = 0; k < i; k++)
                    temp = temp + U.get(k, i) * U.get(k, j);
                U.set(i, j, (A.get(i, j) - temp) / U.get(i, i));
            }
        }
        for (i = 0; i < n; i++) {
            temp = 0;
            for (k = 0; k < i; k++)
                temp = temp + U.get(k, i) * y.get(k);
            y.set(i, (b.get(i) - temp) / U.get(i, i));
        }
        for (i = n - 1; i >= 0; i--) {
            temp = 0;
            for (k = i + 1; k < n; k++)
                temp = temp + U.get(i, k) * x.get(k);
            x.set(i, (y.get(i) - temp) / U.get(i, i));
        }
        for (i = 0; i < n; i++)
            mas.set(i, x.get(i));
        return mas;
    }

    // Вывод списка проводимостей
    public static void printArray(Vector mas) {
        for (int i = 0; i < mas.getN(); i++)
            System.out.println("Проводимость Y" + i + " = " + mas.get(i));
    }

    // Вывод решения в консоль
    public static void printSolution(Vector mas, int ind, double target) {
        System.out.print("\n\n");
        for (int i = 0; i < mas.getN(); i++)
            System.out.println("Напряжение узла U" + (i + 1) + " = " + mas.get(i));
        System.out.println("****Расчёт целевой функции****");
        System.out.println("Полученное значение для заданного узла: " + mas.get(ind)
                + ". Значение, заданное пользователем: " + target);
        System.out.println("Невязка: " + Math.pow(mas.get(ind) - target, 2));
    }

}
