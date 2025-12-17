import java.util.Scanner;

class User {
    String name;
    String command;
}

public class Test1 {

    static String sanitize(String x) {
        return "safe";
    }

    static String identity(String x) {
        return x;
    }

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        // SOURCE 1
        String input1 = sc.nextLine();

        // SOURCE 2
        String input2 = sc.nextLine();

        User user = new User();

        // FIELD TAINT
        user.command = input1;

        // FIELD -> VAR
        String cmdFromField = user.command;

        // METHOD PROPAGATION (unsanitized)
        String unsafeCmd = identity(cmdFromField);

        // SINK (should be reported)
        Runtime.getRuntime().exec(unsafeCmd);

        // SANITIZATION
        String safeCmd = sanitize(input2);

        // SINK (should NOT be reported)
        Runtime.getRuntime().exec(safeCmd);

        // MIXED ASSIGNMENTS
        String alias = unsafeCmd;
        String another = alias;

        // SECOND SINK (should be reported)
        Runtime.getRuntime().exec(another);
    }
}
