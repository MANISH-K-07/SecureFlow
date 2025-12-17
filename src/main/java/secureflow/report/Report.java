package secureflow.report;

import java.util.Set;

public class Report {

    public static void print(Set<String> issues) {

        System.out.println();
        System.out.println("==================== SecureFlow Report ====================");
        System.out.println();

        if (issues.isEmpty()) {
            System.out.println("[OK] No security issues detected.");
        } else {

            System.out.println("Security Issues Detected:\n");

            for (String issue : issues) {
                System.out.println(
                    "[HIGH] [TAINT-001] " + issue
                );
            }
        }

        System.out.println();
        System.out.println("-----------------------------------------------------------");
        System.out.println("Total Issues : " + issues.size());
        System.out.println("===========================================================");
        System.out.println();
    }
}
