package secureflow.report;

import java.util.Set;

public class Report {

    public static void print(Set<String> issues) {
        if (issues.isEmpty()) {
            System.out.println("No security issues found.");
            return;
        }

        System.out.println("Security Issues Detected:");
        issues.forEach(issue -> System.out.println(" - " + issue));
    }
}
