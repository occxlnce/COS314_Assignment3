import java.io.*;
import java.util.*;

public class DataLoader {
    private String basepath;
    public DataLoader(String basepath) {
        this.basepath = basepath;
    }

    public Dataset loadData(String filename) {
        String fullpath = basepath + (basepath.endsWith("/") ? "" : "/") + filename;
        List<double[]> features = new ArrayList<>();
        List<Integer> labels = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fullpath))) {
            String line;
            boolean skipHeader = true;
            while ((line = br.readLine()) != null) {
                if (skipHeader) { skipHeader = false; continue; }
                String[] tokens = line.trim().split(",");
                if (tokens.length < 2) continue; // skip empty or invalid lines
                double[] feat = new double[tokens.length - 1];
                for (int i = 0; i < tokens.length - 1; i++) {
                    feat[i] = Double.parseDouble(tokens[i]);
                }
                labels.add(Integer.parseInt(tokens[tokens.length - 1]));
                features.add(feat);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + fullpath);
            e.printStackTrace();
        }
        return new Dataset(features, labels);
    }
}
