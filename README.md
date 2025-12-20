# ![SecureFlow](https://img.shields.io/badge/SecureFlow-Static%20Taint%20Analysis-blue)

# SecureFlow

[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
![Java](https://img.shields.io/badge/Language-Java-orange.svg)
![Status](https://img.shields.io/badge/Status-Research--Prototype-red.svg)
[![GitHub Pages](https://img.shields.io/badge/demo-GitHub%20Pages-brightgreen)](https://manish-k-07.github.io/SecureFlow/)


SecureFlow is a **static taint analysis tool for Java** that detects **insecure data flows** from untrusted sources to dangerous sinks.
It is designed as a **lightweight, extensible, and research-focused prototype**, demonstrating core program analysis concepts used in security research and industrial tools.

---

## SecureFlow in Action 🚀
Below is a live demonstration of SecureFlow running in the terminal and printing analysis results in real time.

Have a sneak-peek 😉

![SecureFlow Terminal Demo](docs/assets/secureflow_terminal.gif)

---

## Features

* **Intraprocedural Taint Analysis**: Tracks taint through variables, fields, and method calls.
* **Rule-Driven Configuration**: Define dangerous methods (sinks), sanitizer methods, and severity levels using a JSON configuration file.
* **Detailed Reporting**: Outputs human-readable security reports with severity, rule IDs, and line numbers.
* **Field Sensitivity**: Tracks taint through object fields within a single file.
* **Extensible Design**: Easily add new sources, sinks, and rules without modifying the core analyzer.
* **Performance-Friendly**: Lightweight analysis with minimal dependencies (`JavaParser`, `Gson`).

---

## Installation

1. Clone the repository:

```bash
git clone https://github.com/MANISH-K-07/SecureFlow.git
cd SecureFlow
```

2. Dependencies (in `lib/`):

* `javaparser-core-3.x.x.jar`
* `gson-2.x.x.jar`

3. Compile the code:

```powershell
javac -cp "lib/*" $(Get-ChildItem -Recurse src/main/java -Filter *.java | ForEach-Object { $_.FullName })
```
`compile.ps1` file is a plain text file containing the above PowerShell monster script, allowing simpler compilation via a single command:
```
.\compile.ps1
```

---

## Usage

Run SecureFlow on a Java file:

```powershell
java -cp "lib/*;src/main/java" secureflow.Main examples/Test1.java
```

### Sample Output

```
==================== SecureFlow Report ====================

Security Issues Detected:

[HIGH   ] [TAINT-001] Line 75 | Tainted variable 'another' reaches dangerous method 'exec'
[HIGH   ] [TAINT-001] Line 60 | Tainted variable 'chained' reaches dangerous method 'exec'
[MEDIUM ] [TAINT-001] Line 84 | Tainted variable 'code' reaches dangerous method 'exit'

-----------------------------------------------------------
Total Issues : 3
===========================================================

Analysis time: 396 ms
```

---

## Configuration

SecureFlow uses a JSON file to configure rules (`rules.json`):

```json
{
  "dangerous_methods": ["exec", "start", "exit"],
  "sanitizer_methods": ["sanitize", "cleanInput"],
  "severity": {
    "exec": "HIGH",
    "start": "HIGH",
    "exit": "MEDIUM"
  }
}
```

---

## Documentation

* **Design & Architecture**: See [`design.md`](https://github.com/MANISH-K-07/SecureFlow/blob/main/docs/design.md) for high-level architecture, data flow, and design decisions.
* **Known Limitations**: See [`limitations.md`](https://github.com/MANISH-K-07/SecureFlow/blob/main/docs/limitations.md) for analysis scope, limitations, and future work.

---

## Research & Educational Goals

SecureFlow demonstrates:

* **Taint tracking algorithms** in real Java programs
* **Separation of analysis and reporting**
* **Rule-driven static analysis design patterns**

It is intentionally **not a production security scanner**, but a research and educational tool for exploring program analysis and software security concepts.

## Evaluation

Evaluated SecureFlow on a small benchmark suite consisting of
manually crafted Java programs representing common security patterns.

### True Positives
- `TP1.java`: Command execution with untrusted input (Detected)

### False Positives
- `FP1.java`: Sanitized input passed to command execution (Not detected)

These results demonstrate that SecureFlow can detect real vulnerabilities
while avoiding false positives in common sanitization patterns.

---

## License

This project is released under the MIT License.
See [`LICENSE`](LICENSE) for details.
