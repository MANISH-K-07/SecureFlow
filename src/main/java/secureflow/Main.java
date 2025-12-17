package secureflow;

import secureflow.parser.ASTParser;
import secureflow.analysis.TaintAnalyzer;
import secureflow.report.Report;

import com.github.javaparser.ast.CompilationUnit;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {

	long start = System.currentTimeMillis();

        if (args.length != 1) {
            System.out.println("Usage: java Main <JavaFile>");
            return;
        }

        File file = new File(args[0]);
        CompilationUnit cu = ASTParser.parse(file);

        TaintAnalyzer analyzer = new TaintAnalyzer();
        analyzer.visit(cu, null);

        Report.print(analyzer.getIssues());

	long end = System.currentTimeMillis();
	System.out.println("Analysis time: " + (end - start) + " ms");

    }
}
