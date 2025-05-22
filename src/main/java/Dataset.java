import java.util.*;

public class Dataset {
    private double[][] features;
    private int[] labels;

    public Dataset(List<double[]> featuresList, List<Integer> labelsList) {
        this.features = featuresList.toArray(new double[0][]);
        this.labels = labelsList.stream().mapToInt(Integer::intValue).toArray();
    }

    public double[][] getFeatures() {
        return features;
    }

    public int[] getLabels() {
        return labels;
    }

    public int size() {
        return labels.length;
    }
}
