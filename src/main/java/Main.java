import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // User inputs
        System.out.print("Enter seed value: ");
        long seed = scanner.nextLong();
        scanner.nextLine(); // consume newline
        System.out.print("Enter base filepath to dataset (e.g., data/): ");
        String filepath = scanner.nextLine();
        System.out.print("Enter training filename (e.g., BTC_train.csv): ");
        String trainFile = scanner.nextLine();
        System.out.print("Enter test filename (e.g., BTC_test.csv): ");
        String testFile = scanner.nextLine();

        // Load data
        DataLoader loader = new DataLoader(filepath);
        Dataset trainingData = loader.loadData(trainFile);
        Dataset testData = loader.loadData(testFile);

        // Initialize and train Genetic Programming model
        GeneticProgramming gp = new GeneticProgramming(seed);
        gp.train(trainingData);

        // Evaluate and print results
        EvaluationResult result = gp.evaluate(testData);
        System.out.println("\nGenetic Programming Results:");
        System.out.printf("Accuracy: %.4f\n", result.accuracy);
        System.out.printf("F1-score: %.4f\n", result.f1Score);
    }
}
