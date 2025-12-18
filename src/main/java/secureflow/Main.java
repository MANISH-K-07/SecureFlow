package secureflow;

import secureflow.parser.ASTParser;
import secureflow.analysis.TaintAnalyzer;
import secureflow.report.Report;
import secureflow.rules.RuleConfig;

import com.github.javaparser.ast.CompilationUnit;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {

        long start = System.currentTimeMillis();

        if (args.length != 1) {
            System.out.println("Usage: java secureflow.Main <JavaFile>");
            return;
        }

        // Load rules from JSON configuration
        RuleConfig rules = RuleConfig.load("config/rules.json");

        // Parse the Java source file
        File file = new File(args[0]);
        CompilationUnit cu = ASTParser.parse(file);

        // Run taint analysis with loaded rules
        TaintAnalyzer analyzer = new TaintAnalyzer(rules);
        analyzer.visit(cu, null);

        // Print results
	Report.print(analyzer.getIssues(), rules);

        long end = System.currentTimeMillis();
        System.out.println("Analysis time: " + (end - start) + " ms");
        System.out.println();

    }
}
