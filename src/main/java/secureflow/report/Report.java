package secureflow.report;

import secureflow.rules.RuleConfig;

import java.util.Set;

public class Report {

    public static void print(Set<String> issues, RuleConfig rules) {

        System.out.println();
        System.out.println("==================== SecureFlow Report ====================");
        System.out.println();

        if (issues.isEmpty()) {
            System.out.println("[OK] No security issues detected.");
        } else {

            System.out.println("Security Issues Detected:\n");

            for (String issue : issues) {

                /*
                 * Internal issue format:
                 *   sinkName::Line X | message
                 * Example:
                 *   exec::Line 42 | Tainted variable 'x' reaches dangerous method
                 */

                String severity = "MEDIUM"; // default severity
                String message = issue;
                String sink = "unknown";

                if (issue.contains("::")) {
                    String[] parts = issue.split("::", 2);
                    sink = parts[0];
                    message = parts[1];

                    if (rules.severity != null && rules.severity.containsKey(sink)) {
                        severity = rules.severity.get(sink);
                    }
                }

		String paddedSeverity = String.format("%-7s", severity);

		System.out.println(
    		    "[" + paddedSeverity + "] [TAINT-001] " +
    		    message + " '" + sink + "'"
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
