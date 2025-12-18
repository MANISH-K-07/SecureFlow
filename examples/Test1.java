import java.util.Scanner;

class User {
    String name;
    String command;
}

public class Test1 {

    // Sanitizer
    static String sanitize(String x) {
        return "safe";
    }

    // Identity (no sanitization)
    static String identity(String x) {
        return x;
    }

    // Wrapper method
    static String wrap(String x) {
        return identity(x);
    }

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        /* ===================== SOURCES ===================== */

        // SOURCE 1: Scanner input
        String input1 = sc.nextLine();

        // SOURCE 2: Scanner input
        String input2 = sc.nextLine();

        // SOURCE 3: Command-line argument
        String input3 = args[0];

        User user = new User();

        /* ===================== FIELD TAINT ===================== */

        // Variable -> Field
        user.command = input1;

        // Field -> Variable
        String cmdFromField = user.command;

        /* ===================== METHOD PROPAGATION ===================== */

        // Unsanitized propagation
        String unsafeCmd = identity(cmdFromField);

        // Method chain propagation
        String chained = wrap(unsafeCmd);

        /* ===================== SINK (SHOULD BE REPORTED) ===================== */

        Runtime.getRuntime().exec(chained);

        /* ===================== SANITIZATION ===================== */

        String safeCmd = sanitize(input2);

        // Sink should NOT be reported
        Runtime.getRuntime().exec(safeCmd);

        /* ===================== ALIASING ===================== */

        String alias = chained;
        String another = alias;

        // Sink should be reported
        Runtime.getRuntime().exec(another);

        /* ===================== FIELD SINK ===================== */

        // Propagate taint back into field
        user.command = input3;

        // Severity Testing
	int code = Integer.parseInt(input1);
	System.exit(code);

    }
}
