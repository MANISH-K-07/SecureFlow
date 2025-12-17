package secureflow.rules;

import java.util.Set;

public class Sinks {

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
