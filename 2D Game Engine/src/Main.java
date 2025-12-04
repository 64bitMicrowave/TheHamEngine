import Ham.App;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        Thread t1 = new Thread(app);
        t1.start();
    }
}