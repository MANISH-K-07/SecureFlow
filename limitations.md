# Limitations of SecureFlow

This document outlines the known limitations of **SecureFlow**, the design trade-offs made during its development, and areas for future improvement. These limitations are intentional in scope and reflect a focus on clarity, soundness, and educational value rather than full production completeness.

---

## 1. Intraprocedural Analysis Only

SecureFlow performs **intraprocedural taint analysis**, meaning taint propagation is analyzed only within the boundaries of a single method.

### Implication

* Taint flows across method boundaries (e.g., via return values or parameters across multiple calls) are not fully modeled.
* Recursive or deeply nested call chains are not precisely tracked.

### Rationale

* This design choice simplifies the analysis and keeps it lightweight and deterministic.
* Many industry tools begin with intraprocedural analysis before adding interprocedural extensions.

### Future Work

* Extend SecureFlow with interprocedural summaries to track taint across method calls.

---

## 2. Heuristic-Based Source Detection

Sources of tainted data are detected using **syntactic heuristics** (e.g., `Scanner.nextLine()`, command-line arguments, environment variables).

### Implication

* Custom input APIs or user-defined wrappers around input methods may not be detected as sources.
* False negatives are possible if the input pattern is non-standard.

### Rationale

* Heuristic detection avoids hardcoding specific APIs and keeps the analysis extensible.
* This approach mirrors early-stage static analysis prototypes and research tools.

### Future Work

* Introduce configurable source patterns via the rules configuration file.

---

## 3. Limited Expression Coverage

SecureFlow currently tracks taint through:

* Variable assignments
* Field reads and writes
* Method call arguments

It does **not** fully analyze:

* Arithmetic expressions
* Complex boolean expressions
* Collection indexing or map lookups

### Implication

* Taint propagation through complex expressions may be under-approximated.

### Rationale

* Focusing on common assignment and call patterns improves precision for real-world vulnerabilities while keeping the implementation simple.

### Future Work

* Extend expression handling to support compound expressions and collections.

---

## 4. No Control-Flow Sensitivity

SecureFlow does not perform control-flow-sensitive analysis.

### Implication

* Conditional sanitization (e.g., sanitizing input only on certain branches) is treated conservatively.
* Some false positives may occur when sanitization is path-dependent.

### Rationale

* Control-flow sensitivity significantly increases complexity and analysis cost.
* A conservative approach avoids unsound assumptions.

### Future Work

* Introduce basic control-flow analysis to distinguish between sanitized and unsanitized execution paths.

---

## 5. Single-File Analysis Scope

SecureFlow analyzes one Java source file per execution.

### Implication

* Cross-file data flows (e.g., taint propagating through class fields across files) are not detected.

### Rationale

* Single-file analysis simplifies parsing and keeps the tool easy to run and understand.

### Future Work

* Extend SecureFlow to analyze multiple compilation units in a project-level context.

---

## 6. Conservative Taint Propagation

SecureFlow favors **soundness over precision**.

### Implication

* In ambiguous cases, taint may be propagated even when it is safe, leading to false positives.

### Rationale

* Missing a real vulnerability is more harmful than reporting a potential one in security analysis.

### Future Work

* Introduce optional precision modes to balance false positives and false negatives.

---

## 7. Limited Sink Semantics

While SecureFlow supports severity-based sink classification, it does not model detailed sink semantics.

### Implication

* Context-specific safety checks (e.g., validated command execution) are not recognized.

### Rationale

* Sink-level severity abstraction keeps the reporting simple and consistent.

### Future Work

* Extend rule definitions with contextual constraints and argument validation logic.

---

## Summary

SecureFlow is intentionally designed as a **lightweight, extensible static analysis prototype**. Its limitations reflect conscious design trade-offs aimed at maximizing clarity, correctness, and educational value. These constraints also provide a clear roadmap for future enhancements, including interprocedural analysis, richer expression handling, and project-wide analysis.

Documenting these limitations demonstrates an understanding of both the capabilities and boundaries of static analysis tools, aligning SecureFlow with best practices in research and tool development.
