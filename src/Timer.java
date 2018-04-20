public class Timer {

    private static double tStart = -1;
    private static double tStop = -1;

    private Timer() {}

    public static void start() {
        tStart = (System.nanoTime() / Math.pow(10, 6));
        tStop = -1l;
    }

    public static void stop() {
        tStop = (System.nanoTime() / Math.pow(10, 6));
    }

    public static double getTime() {
        return (tStop - tStart);
    }

    public static String getString() {
        return (double)Math.round(getTime()*100)/100 + " ms";
    }
}