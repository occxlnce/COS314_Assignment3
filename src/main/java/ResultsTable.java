import java.util.*;

public class ResultsTable {
    private static class ModelResult {
        String name;
        double acc;
        double f1;
        ModelResult(String name, double acc, double f1) {
            this.name = name; this.acc = acc; this.f1 = f1;
        }
    }
    private List<ModelResult> results = new ArrayList<>();

    public void addModelResult(String modelName, EvaluationResult result) {
        results.add(new ModelResult(modelName, result.accuracy, result.f1Score));
    }
    public void printTable() {
        System.out.printf("%-20s | %-10s | %-10s\n", "Model", "Accuracy", "F1-Score");
        System.out.println("---------------------+------------+-----------");
        for (ModelResult r : results) {
            System.out.printf("%-20s | %-10.4f | %-10.4f\n", r.name, r.acc, r.f1);
        }
    }
}
