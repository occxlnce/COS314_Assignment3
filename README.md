# COS314 Assignment 3

## Overview
This project investigates the use of three machine learning models—**Genetic Programming (GP)**, **Multi-Layer Perceptron (MLP)**, and **Decision Tree (J48)**—to classify stock purchase decisions based on historical financial data. The aim is to compare symbolic, neural, and rule-based approaches for interpretable and effective stock trading strategies.

## Problem Statement & Motivation
Automated stock purchase classification is critical for building algorithmic trading systems and decision support tools in finance. Accurate and interpretable models can help traders and analysts identify profitable opportunities and reduce risk.

## Data Description & Insights
The datasets (`BTC_train.csv` and `BTC_test.csv`) contain rows of historical Bitcoin trading data. Each row represents a time step with columns for technical indicators (e.g., open, high, low, close, volume) and a binary target label (0 = No Buy, 1 = Buy).

- **Feature Ranges:** Features are normalized or on a similar scale, suitable for ML algorithms.
- **Label Distribution:** [Analyze with a script or visually; if imbalanced, use F1-score for fair assessment.]
- **Insights:** Feature correlation analysis or tree-based feature importances can reveal which indicators influence buy decisions. Outliers or missing values should be checked.

## Model Overview
This project compares three models:
- **Genetic Programming (GP):** Evolves symbolic arithmetic expressions for classification.
- **Multi-Layer Perceptron (MLP):** Neural network for learning non-linear patterns. [Stub/not implemented]
- **Decision Tree (J48):** Rule-based model for interpretable decision-making. [Stub/not implemented]

## Implementation Details
### Genetic Programming (GP)
- **Representation:** Individuals are arithmetic expression trees (+, -, *, /) with feature variables and random constants.
- **Population:** Randomly initialized trees, evolved over generations.
- **Selection:** Tournament selection (size 3).
- **Crossover & Mutation:** Subtree operations.
- **Fitness:** Accuracy (default) or F1-score.
- **Termination:** Fixed number of generations (e.g., 30).
- **Prediction:** Best individual used for classification.
- **Robustness:** Handles division by zero and tree bloat.
- **Reproducibility:** User-defined seed.

### Multi-Layer Perceptron (MLP)
- **Architecture:** [Stub; to be implemented]
- **Training:** [Stub; to be implemented]
- **Prediction:** [Stub; to be implemented]

### Decision Tree (J48)
- **Algorithm:** J48 (C4.5) [Stub; to be implemented]
- **Splitting:** Information gain.
- **Prediction:** [Stub; to be implemented]

## Setup
1. Clone or download this repository.
2. Place your training and test datasets in the `data/` folder.
3. (If using Maven) Build the project:
   ```sh
   mvn clean package
   ```
   This will generate a standalone JAR in the `target/` directory.

## Running the Program

### Using the Generated JAR File
After building the project with Maven, the runnable JAR file is located in the `target/` directory. The filename will look like:

    target/COS314_Assignment3-1.0-SNAPSHOT.jar

To run the program using the JAR file:
```sh
java -jar target/COS314_Assignment3-1.0-SNAPSHOT.jar
```
You will be prompted to enter the seed value, dataset path, and filenames.

### Manual Compilation (No Maven)
If you are not using Maven, you can compile and run the program manually:

1. Compile all Java files:
   ```sh
   javac -d bin src/main/java/*.java
   ```
2. Run the program:
   ```sh
   java -cp bin Main
   ```
3. Enter the requested values when prompted (seed, data path, filenames).
4. Results—including the best evolved expression, accuracy, and F1-score—will be printed to the console.

## Results Table

| Model                | Seed value | Training Acc | Training F1 | Testing Acc | Testing F1 |
|----------------------|------------|--------------|-------------|-------------|------------|
| Genetic Programming  | 42         | 0.2776       | 0.0777      | 0.2776      | 0.0777     |
| MLP                  | 42         | [TBD]        | [TBD]       | [TBD]       | [TBD]      |
| Decision Tree        | 42         | [TBD]        | [TBD]       | [TBD]       | [TBD]      |

*Figure 1: Results Table*

## Analysis & Interpretation
- **GP Performance:** The GP model achieved an accuracy of 0.2776 and F1-score of 0.0777 on both training and test data. This indicates that, for this dataset and parameter setting, the evolved expressions have limited predictive power. This could be due to data complexity, class imbalance, or insufficient generations/population size.
- **Patterns:** The best evolved expression can be analyzed for interpretability. However, low F1-score suggests the model struggles with minority class detection.
- **Recommendations:** Consider tuning GP hyperparameters, engineering new features, or balancing the dataset for improved performance. Compare with MLP and Decision Tree once implemented.

## Statistical Test: Wilcoxon Signed-Rank Test
To evaluate the significance of performance differences between GP and MLP, a Wilcoxon signed-rank test will be performed on their predictions for the test set.
- **Null hypothesis:** No significant difference in performance between GP and MLP.
- **Test statistic:** [To be filled after running the test]
- **p-value:** [To be filled after running the test]
- **Conclusion:** [Reject/Do not reject null hypothesis at α = 0.05]

## Conclusions
- **Interpretation:** The GP model’s results suggest challenges in modeling this financial data with symbolic regression alone. Further work is needed to improve performance.
- **Business Value:** Symbolic models like GP offer transparency, but may require more expressive features or integration with other approaches for better results in real-world trading.
- **Next Steps:** Complete MLP and Decision Tree implementations, perform the Wilcoxon test, and refine models based on comparative analysis.

## Requirements
- Java 8+
- Maven

## Setup
1. Clone or download this repository.
2. Place your training and test datasets in the `data/` folder.
3. Build the project using Maven:
   ```sh
   mvn clean package
   ```
   This will generate a standalone JAR in the `target/` directory.

## Running the Program

### Genetic Programming Classifier

This project includes a custom implementation of a Genetic Programming (GP) classifier for binary classification of stock purchase decisions. The GP algorithm evolves arithmetic expressions (using +, -, *, /) to find patterns in your data.

**How it works:**
- A population of random expression trees is generated.
- The population is evolved over several generations using tournament selection, subtree crossover, and mutation.
- Fitness is measured by classification accuracy (or F1-score if enabled).
- The best evolved expression is used to classify the test data.
- The program prints the best expression, accuracy, and F1-score.

**To run the GP classifier:**
1. Compile the code:
   ```sh
   javac -d bin src/main/java/Main.java src/main/java/GeneticProgramming.java src/main/java/DataLoader.java src/main/java/Dataset.java src/main/java/EvaluationResult.java
   ```
2. Run the program:
   ```sh
   java -cp bin Main
   ```
3. Enter the requested values when prompted (seed, data path, filenames).

**Sample output:**
```
Gen  0 | Best fitness: ... | Depth: ... | Expr: ...
...
Genetic Programming Results:
Accuracy: ...
F1-score: ...

## Results Table

| Model                | Seed value | Training Acc | Training F1 | Testing Acc | Testing F1 |
|----------------------|------------|--------------|-------------|-------------|------------|
| Genetic Programming  | 42         | 0.2776       | 0.0777      | 0.2776      | 0.0777     |
| MLP                  | 42         | [TBD]        | [TBD]       | [TBD]       | [TBD]      |
| Decision Tree        | 42         | [TBD]        | [TBD]       | [TBD]       | [TBD]      |

*Figure 1: Results Table*

You can tune the GP parameters or switch the fitness function in `GeneticProgramming.java` as needed.

Run the JAR file from the command line:
```sh
java -jar target/COS314_Assignment3-1.0-SNAPSHOT-jar-with-dependencies.jar
```
You will be prompted to enter:
- Seed value
- Filepath to dataset (e.g., `C:/Users/YourName/Documents/UP/COS314/Assignment3/data/`)
- Model parameters (if applicable)

## Folder Structure
```
COS314_Assignment3/
├── data/
│   ├── training.csv
│   └── test.csv
├── src/
│   └── main/
│       └── java/
│           └── ... (Java source files)
├── README.md
├── pom.xml
└── report.pdf
```

## Notes
- Ensure Java and Maven are installed and configured in your PATH.
- The JAR is self-contained and does not require an IDE.
- For any issues, please refer to the report or contact the team.
