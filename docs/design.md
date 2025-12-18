# SecureFlow – Design & Architecture

## 1. Overview

SecureFlow is a lightweight **static taint analysis tool for Java** designed to detect insecure data flows from **untrusted sources** to **dangerous sinks**.

The primary goal of SecureFlow is **clarity and extensibility**, not completeness.  
It intentionally focuses on a **small, well-defined analysis scope** to demonstrate core static analysis concepts used in security research and industrial tools.

---

## 2. High-Level Architecture

The analysis pipeline is organized as follows:

```
Java Source File
↓
JavaParser (AST Generation)
↓
TaintAnalyzer (AST Visitor)
↓
Issue Collection
↓
Report Generator
```


Each stage is implemented as a separate module to ensure separation of concerns.

---

## 3. Core Components

### 3.1 AST Parsing

SecureFlow uses **JavaParser** to parse Java source files into an **Abstract Syntax Tree (AST)**.

**Rationale:**
- JavaParser provides a stable, well-documented AST
- Enables precise syntactic analysis without bytecode execution
- Widely used in academic and industrial static analysis tools

---

### 3.2 TaintAnalyzer

The `TaintAnalyzer` is implemented using the **Visitor pattern** (`VoidVisitorAdapter`).

It performs:
- **Taint source detection**
- **Taint propagation**
- **Sink detection**

#### Tracked entities
- Local variables
- Object fields (limited field sensitivity)

#### Taint sources
- User-controlled inputs (e.g., `Scanner.nextLine()`)

#### Taint propagation rules
- Variable assignments (`b = a`)
- Field reads and writes (`obj.x`)
- Method calls (conservative propagation)

Sanitizer methods are explicitly excluded from propagation.

---

### 3.3 Rule Configuration

SecureFlow loads sinks, sanitizers, and severity mappings from an external **JSON-based rule configuration**.

This allows:
- Rule changes without recompilation
- Easy extensibility
- Clear separation between analysis logic and security policy

Example rule categories:
- Dangerous methods (sinks)
- Sanitizer methods
- Severity levels per sink

---

### 3.4 Issue Representation

Detected issues are recorded with:
- Rule identifier
- Severity
- Line number
- Human-readable explanation

Issues are stored independently of reporting logic to allow future output formats (e.g., JSON, SARIF).

---

### 3.5 Reporting Engine

The reporting module formats findings into a **human-readable security report**, inspired by industry tools.

Design goals:
- Clear severity indication
- Stable rule identifiers
- Minimal noise
- Deterministic output

---

## 4. Analysis Characteristics

| Property              | Supported |
|----------------------|-----------|
| Static analysis       | Yes       |
| Flow-sensitive        | Yes       |
| Field-sensitive       | Limited   |
| Interprocedural       | No        |
| Context-sensitive     | No        |
| Path-sensitive        | No        |

These limitations are **intentional** and documented explicitly.

---

## 5. Design Decisions

### Why intra-procedural analysis?
- Keeps analysis predictable and easy to reason about
- Avoids CFG and call graph complexity
- Appropriate for an educational and research-focused tool

### Why conservative propagation?
- Avoids false negatives
- Demonstrates real-world static analysis trade-offs
- Matches behavior of many security linters

### Why rule-driven configuration?
- Decouples policy from implementation
- Enables experimentation with different security models
- Aligns with modern static analysis design patterns

---

## 6. Extensibility Roadmap

SecureFlow is designed to be extended incrementally:

Possible future enhancements:
- Interprocedural taint propagation
- Control-flow graph (CFG) integration
- SSA-based variable tracking
- Support for additional sources and sinks
- Machine-readable output formats (JSON, SARIF)

---

## 7. Scope Statement

SecureFlow is **not intended to be a production-ready security scanner**.

It is a:
- Research-oriented prototype
- Learning-focused static analysis tool
- Demonstration of core program analysis concepts

Known limitations are documented separately in `limitations.md`.

---

## 8. Summary

SecureFlow demonstrates how classical static analysis techniques can be applied to real-world Java code in a clean, modular, and extensible manner.

The design emphasizes:
- Simplicity over completeness
- Correctness over cleverness
- Transparency over black-box heuristics
