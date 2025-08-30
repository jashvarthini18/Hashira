# 🔐 Shamir Secret Sharing Recovery (Java)

This project is a **Java implementation** of Shamir's Secret Sharing recovery algorithm. It takes shares from a JSON file, validates them, and reconstructs the original secret using **Lagrange interpolation**.

## 📋 Overview

Shamir's Secret Sharing is a cryptographic algorithm that divides a secret into multiple parts (shares) and requires a minimum number of shares to reconstruct the original secret. This implementation:

- Parses share data from JSON input
- Validates shares and identifies corrupted ones
- Reconstructs the secret using Lagrange interpolation
- Supports different number bases (binary, decimal, quaternary, etc.)

## 📂 Files in this Repository

- **`Test.java`** → Main program that parses input and reconstructs the secret
- **`test1.json`** → Example input file with shares
- **`test2.json`** → Example input file with shares
- **`README.md`** → Project documentation (this file)

## 🚀 How to Compile and Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Basic understanding of command line operations

### Compilation
Compile the Java program using:
```bash
javac Test.java
```

### Execution
Run the program with JSON input using:

```bash
java Test < test1.json
```
