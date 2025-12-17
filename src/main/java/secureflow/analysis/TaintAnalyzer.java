package secureflow.analysis;

import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import secureflow.rules.RuleConfig;

import java.util.HashSet;
import java.util.Set;

public class TaintAnalyzer extends VoidVisitorAdapter<Void> {

    private final Set<String> taintedVars = new HashSet<>();
    private final Set<String> taintedFields = new HashSet<>();
    private final Set<String> issues = new HashSet<>();
    private final RuleConfig rules;

    // Constructor receives rules loaded from JSON
    public TaintAnalyzer(RuleConfig rules) {
        this.rules = rules;
    }

    @Override
    public void visit(VariableDeclarationExpr expr, Void arg) {
        expr.getVariables().forEach(var -> {
            if (var.getInitializer().isPresent()) {
                Expression init = var.getInitializer().get();

                // SOURCE: Scanner input
                if (init.toString().contains("nextLine")) {
                    taintedVars.add(var.getNameAsString());
                }

                // ASSIGNMENT PROPAGATION: b = a
                if (init.isNameExpr()) {
                    String rhs = init.asNameExpr().getNameAsString();
                    if (taintedVars.contains(rhs)) {
                        taintedVars.add(var.getNameAsString());
                    }
                }

                // FIELD READ: b = obj.x
                if (init.isFieldAccessExpr()) {
                    FieldAccessExpr field = init.asFieldAccessExpr();
                    String fieldKey =
                            field.getScope().toString() + "." + field.getNameAsString();

                    if (taintedFields.contains(fieldKey)) {
                        taintedVars.add(var.getNameAsString());
                    }
                }

                // METHOD CALL PROPAGATION: b = foo(a)
                if (init.isMethodCallExpr()) {
                    MethodCallExpr call = init.asMethodCallExpr();
                    String methodName = call.getNameAsString();

                    // Sanitizer: do NOT propagate taint
                    if (rules.sanitizer_methods.contains(methodName)) {
                        return;
                    }

                    // Conservative propagation
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
    public void visit(AssignExpr expr, Void arg) {

        Expression target = expr.getTarget();
        Expression value = expr.getValue();

        // FIELD WRITE: obj.x = ...
        if (target.isFieldAccessExpr()) {
            FieldAccessExpr field = target.asFieldAccessExpr();
            String fieldKey =
                    field.getScope().toString() + "." + field.getNameAsString();

            // Source: obj.x = input()
            if (value.toString().contains("nextLine")) {
                taintedFields.add(fieldKey);
            }

            // Propagation: obj.x = y
            if (value.isNameExpr()) {
                String rhs = value.asNameExpr().getNameAsString();
                if (taintedVars.contains(rhs)) {
                    taintedFields.add(fieldKey);
                }
            }
        }

        super.visit(expr, arg);
    }

    @Override
    public void visit(MethodCallExpr call, Void arg) {
        String methodName = call.getNameAsString();

        // Sink detection
        if (rules.dangerous_methods.contains(methodName)) {
            for (Expression e : call.getArguments()) {

                // Variable argument
                if (e.isNameExpr()) {
                    String argName = e.asNameExpr().getNameAsString();
                    if (taintedVars.contains(argName)) {
                        issues.add(
                            "Tainted data '" + argName +
                            "' reaches dangerous method '" + methodName + "'"
                        );
                    }
                }

                // Field argument: exec(obj.x)
                if (e.isFieldAccessExpr()) {
                    FieldAccessExpr field = e.asFieldAccessExpr();
                    String fieldKey =
                            field.getScope().toString() + "." + field.getNameAsString();

                    if (taintedFields.contains(fieldKey)) {
                        issues.add(
                            "Tainted field '" + fieldKey +
                            "' reaches dangerous method '" + methodName + "'"
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
