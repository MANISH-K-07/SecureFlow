import java.util.Scanner;

public class FP1 {

    static String sanitize(String x) {
        return "safe";
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String a = sc.nextLine();
        String b = sanitize(a);
        Runtime.getRuntime().exec(b);
    }
}
