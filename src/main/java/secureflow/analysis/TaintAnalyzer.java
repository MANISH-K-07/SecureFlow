package secureflow.analysis;

import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import secureflow.rules.Sinks;

import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class TaintAnalyzer extends VoidVisitorAdapter<Void> {

    private final Set<String> taintedVars = new HashSet<>();
    private final Set<String> issues = new HashSet<>();

@Override
public void visit(VariableDeclarationExpr expr, Void arg) {
    expr.getVariables().forEach(var -> {
        if (var.getInitializer().isPresent()) {
            Expression init = var.getInitializer().get();

            // SOURCE
            if (init.toString().contains("nextLine")) {
                taintedVars.add(var.getNameAsString());
            }

            // ASSIGNMENT PROPAGATION
            if (init.isNameExpr()) {
                String rhs = init.asNameExpr().getNameAsString();
                if (taintedVars.contains(rhs)) {
                    taintedVars.add(var.getNameAsString());
                }
            }

            // METHOD CALL PROPAGATION
if (init.isMethodCallExpr()) {
    MethodCallExpr call = init.asMethodCallExpr();
    String methodName = call.getNameAsString();

    // If method is a sanitizer, DO NOT propagate taint
    if (secureflow.rules.Sinks.SANITIZER_METHODS.contains(methodName)) {
        return;
    }

    // Otherwise, conservative propagation
    for (Expression argExpr : call.getArguments()) {
        if (argExpr.isNameExpr()) {
            String argName = argExpr.asNameExpr().getNameAsString();
            if (taintedVars.contains(argName)) {
                taintedVars.add(var.getNameAsString());
            }
        }
    }
}
        }
    });
    super.visit(expr, arg);
}

@Override
public void visit(MethodCallExpr call, Void arg) {
    String methodName = call.getNameAsString();

    if (Sinks.DANGEROUS_METHODS.contains(methodName)) {
        for (Expression e : call.getArguments()) {
            if (e.isNameExpr()) {
                String argName = e.asNameExpr().getNameAsString();
                if (taintedVars.contains(argName)) {
                    issues.add(
                        "Tainted data '" + argName + "' reaches dangerous method '" + methodName + "'"
                    );
                }
            }
        }
    }
    super.visit(call, arg);
}

    public Set<String> getIssues() {
        return issues;
    }
}
