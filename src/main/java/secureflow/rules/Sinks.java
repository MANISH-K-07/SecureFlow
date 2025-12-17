package secureflow.rules;

import java.util.Set;

public class Sinks {

    // File that can be Discarded.

    public static final Set<String> DANGEROUS_METHODS = Set.of(
            "exec",
            "eval"
    );

    public static final Set<String> SANITIZER_METHODS = Set.of(
            "sanitize",
            "clean",
            "escape"
    );
}
