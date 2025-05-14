# TextSimplifier
A Java-based **Text Simplifier** that processes a user-provided text file and replaces its words with synonyms from the **Google list of 1,000 most common English words**.
This is a text simplification application developed for a project focused on natural language processing and user-friendly content delivery. The application takes in a text file, replaces words with synonyms, and outputs a simplified version of the text.

## 📚 Project Overview

The goal of this project is to simplify text with embeddings and swap words with synonyms drawn from the [Google-1000 most common words](https://github.com/first20hours/google-10000-english). Word similarity is computed using **GloVe word embeddings**.

---

## 🚀 Features/How It Works

- The application reads and processes the text content.
- It attempts to **replace each word** in the text with a **simpler synonym** based on the 1,000 most common English words.
- Outputs the simplified text to a new file

  This application combines **file I/O**, **string manipulation**, and **word similarity matching** to produce a version of the original text that is (potentially) easier to understand.

---

## 🛠️ How to Run

### 1. Clone the repository
```bash
git clone https://github.com/yourusername/TextSimplifier.git
cd TextSimplifier
```
## 2. Compile the code
Make sure you have Java installed (JDK 8 or above):
```bash
javac -d out src/*.java
```
## 3. Run the application
```bash
java -cp out Runner
```

---

## ⚙️ Requirements
- Java 8+
- GloVe word embeddings file
- Google-1000 word list

  ---

  ## 💻 Technologies Used
- **Java** – Core language used for building the application logic
- **GloVe Word Embeddings** – Used for finding semantically similar words
- **Google-1000 Word List** – Reference for identifying common words
- **File I/O (Java)** – Reading and writing plain text files
- **Collections Framework** – For managing and processing word data
- **String Manipulation** – Parsing and simplifying text content
- **Command Line Interface (CLI)** – Running and testing the application

  ---

  ## 🎯 Learning Outcomes
- Gained experience in Natural Language Processing (NLP) concepts
- Developed skills in writing clean, modular, and testable code
- Improved proficiency with Java data structures, file handling, and conditionals

---

## 📄 License
This project was completed as part of academic coursework and is provided for educational purposes only.

---

## 📫 Contact
For questions or feedback, feel free to reach out via GitHub.
