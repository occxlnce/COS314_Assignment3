public class EvaluationResult {
    public double accuracy;
    public double f1Score;

    public EvaluationResult(double accuracy, double f1Score) {
        this.accuracy = accuracy;
        this.f1Score = f1Score;
    }

    public static double accuracy(int[] trueLabels, int[] predictedLabels) {
        int correct = 0;
        for (int i = 0; i < trueLabels.length; i++) {
            if (trueLabels[i] == predictedLabels[i]) correct++;
        }
        return (double) correct / trueLabels.length;
    }

    public static double f1Score(int[] trueLabels, int[] predictedLabels) {
        int tp = 0, fp = 0, fn = 0;
        for (int i = 0; i < trueLabels.length; i++) {
            if (predictedLabels[i] == 1 && trueLabels[i] == 1) tp++;
            if (predictedLabels[i] == 1 && trueLabels[i] == 0) fp++;
            if (predictedLabels[i] == 0 && trueLabels[i] == 1) fn++;
        }
        double precision = tp + fp == 0 ? 0 : (double) tp / (tp + fp);
        double recall = tp + fn == 0 ? 0 : (double) tp / (tp + fn);
        return precision + recall == 0 ? 0 : 2 * precision * recall / (precision + recall);
    }
}
