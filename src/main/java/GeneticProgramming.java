import java.util.*;

/**
 * Genetic Programming classifier for binary classification.
 * Implements population, fitness, tournament selection, subtree crossover/mutation, and evolution.
 * Expression trees use arithmetic operators and variables/constants.
 *
 * Assignment-ready: well-commented, robust, and supports accuracy/F1 fitness.
 */
public class GeneticProgramming {
    private long seed;
    private int[] lastPredictions;
    private Random rand;
    private Individual bestIndividual;

    // GP hyperparameters (tune as needed)
    private static final int POP_SIZE = 30;
    private static final int GENERATIONS = 30;
    private static final int TOURNAMENT_SIZE = 3;
    private static final double CROSSOVER_RATE = 0.7;
    private static final double MUTATION_RATE = 0.3;
    private static final int MAX_DEPTH = 4;
    private static final boolean USE_F1_FITNESS = false; // Set true to use F1-score as fitness
    private static final boolean VERBOSE = true; // Print progress

    // Operators for arithmetic expressions
    private static final String[] OPERATORS = {"+", "-", "*", "/"};

    public GeneticProgramming(long seed) {
        this.seed = seed;
        this.rand = new Random(seed);
    }

    // Abstract node for expression tree
    /**
     * Base class for all nodes in the expression tree (variable, constant, or operator).
     */
    private abstract static class Node {
        abstract double eval(double[] x); // Evaluate the subtree for input x
        abstract Node cloneTree();        // Deep copy of the subtree
        abstract int countNodes();        // Count all nodes in the subtree
        abstract void replaceNode(int idx, Node newNode, int[] currIdx); // Replace node at idx
        abstract int depth();             // Depth of the subtree
        abstract String print();          // String representation
    }
    /**
     * Variable node (represents a feature x_i).
     */
    private static class VarNode extends Node {
        int idx;
        VarNode(int idx) { this.idx = idx; }
        double eval(double[] x) { return x[idx]; }
        Node cloneTree() { return new VarNode(idx); }
        int countNodes() { return 1; }
        void replaceNode(int idx, Node newNode, int[] currIdx) {
            if (currIdx[0] == idx) currIdx[0] = -1;
        }
        int depth() { return 1; }
        String print() { return "x" + idx; }
    }
    /**
     * Constant node (represents a numeric constant).
     */
    private static class ConstNode extends Node {
        double value;
        ConstNode(double value) { this.value = value; }
        double eval(double[] x) { return value; }
        Node cloneTree() { return new ConstNode(value); }
        int countNodes() { return 1; }
        void replaceNode(int idx, Node newNode, int[] currIdx) {
            if (currIdx[0] == idx) currIdx[0] = -1;
        }
        int depth() { return 1; }
        String print() { return String.format("%.2f", value); }
    }
    /**
     * Operator node (applies an arithmetic operation to its children).
     */
    private static class OpNode extends Node {
        String op;
        Node left, right;
        OpNode(String op, Node left, Node right) { this.op = op; this.left = left; this.right = right; }
        double eval(double[] x) {
            double l = left.eval(x), r = right.eval(x);
            switch (op) {
                case "+": return l + r;
                case "-": return l - r;
                case "*": return l * r;
                case "/": return Math.abs(r) < 1e-6 ? l : l / r; // robust division
                default: return 0;
            }
        }
        Node cloneTree() { return new OpNode(op, left.cloneTree(), right.cloneTree()); }
        int countNodes() { return 1 + left.countNodes() + right.countNodes(); }
        void replaceNode(int idx, Node newNode, int[] currIdx) {
            currIdx[0]++;
            if (currIdx[0] == idx) { left = newNode; currIdx[0] = -1; return; }
            left.replaceNode(idx, newNode, currIdx);
            if (currIdx[0] == -1) return;
            currIdx[0]++;
            if (currIdx[0] == idx) { right = newNode; currIdx[0] = -1; return; }
            right.replaceNode(idx, newNode, currIdx);
        }
        int depth() { return 1 + Math.max(left.depth(), right.depth()); }
        String print() { return "(" + left.print() + op + right.print() + ")"; }
    }

    /**
     * Individual: encapsulates an expression tree and its fitness.
     */
    private static class Individual {
        Node root;
        double fitness;
        Individual(Node root) { this.root = root; }
        Individual cloneInd() { return new Individual(root.cloneTree()); }
        int predict(double[] x) { return root.eval(x) > 0 ? 1 : 0; }
        int depth() { return root.depth(); }
        String print() { return root.print(); }
    }

    /**
     * Generate a random expression tree up to a given depth.
     */
    private Node randomTree(int maxDepth, int numFeatures) {
        if (maxDepth == 0 || rand.nextDouble() < 0.3) {
            if (rand.nextBoolean()) return new VarNode(rand.nextInt(numFeatures));
            else return new ConstNode(rand.nextDouble() * 10 - 5);
        } else {
            String op = OPERATORS[rand.nextInt(OPERATORS.length)];
            Node left = randomTree(maxDepth - 1, numFeatures);
            Node right = randomTree(maxDepth - 1, numFeatures);
            return new OpNode(op, left, right);
        }
    }

    /**
     * Main GP training loop: evolves a population over generations.
     */
    public void train(Dataset data) {
        int numFeatures = data.getFeatures()[0].length;
        List<Individual> pop = new ArrayList<>();
        // Init population
        for (int i = 0; i < POP_SIZE; i++) {
            pop.add(new Individual(randomTree(MAX_DEPTH, numFeatures)));
        }
        // Evolve
        for (int gen = 0; gen < GENERATIONS; gen++) {
            // Evaluate fitness (accuracy or F1)
            for (Individual ind : pop) {
                ind.fitness = fitness(ind, data);
            }
            // Find best
            pop.sort((a, b) -> Double.compare(b.fitness, a.fitness));
            bestIndividual = pop.get(0).cloneInd();
            if (VERBOSE) {
                System.out.printf("Gen %2d | Best fitness: %.4f | Depth: %d | Expr: %s\n", gen, bestIndividual.fitness, bestIndividual.depth(), bestIndividual.print());
            }
            // Next generation
            List<Individual> nextPop = new ArrayList<>();
            nextPop.add(bestIndividual.cloneInd()); // elitism
            while (nextPop.size() < POP_SIZE) {
                Individual p1 = tournament(pop);
                Individual p2 = tournament(pop);
                Individual child;
                if (rand.nextDouble() < CROSSOVER_RATE) {
                    child = crossover(p1, p2, numFeatures);
                } else {
                    child = p1.cloneInd();
                }
                if (rand.nextDouble() < MUTATION_RATE) {
                    child = mutate(child, numFeatures);
                }
                // Control tree depth
                if (child.depth() > MAX_DEPTH) {
                    child = new Individual(randomTree(MAX_DEPTH, numFeatures));
                }
                nextPop.add(child);
            }
            pop = nextPop;
        }
        if (VERBOSE) {
            System.out.println("Best evolved expression: " + bestIndividual.print());
        }
    }

    /**
     * Compute fitness for an individual (accuracy or F1-score).
     */
    private double fitness(Individual ind, Dataset data) {
        double[][] X = data.getFeatures();
        int[] y = data.getLabels();
        int[] preds = new int[X.length];
        for (int i = 0; i < X.length; i++) preds[i] = ind.predict(X[i]);
        if (USE_F1_FITNESS) {
            return EvaluationResult.f1Score(y, preds);
        } else {
            int correct = 0;
            for (int i = 0; i < X.length; i++) {
                if (preds[i] == y[i]) correct++;
            }
            return (double) correct / X.length;
        }
    }

    // Tournament selection
    private Individual tournament(List<Individual> pop) {
        Individual best = null;
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            Individual ind = pop.get(rand.nextInt(pop.size()));
            if (best == null || ind.fitness > best.fitness) best = ind;
        }
        return best;
    }

    // Crossover: swap random subtree
    private Individual crossover(Individual p1, Individual p2, int numFeatures) {
        Individual c = p1.cloneInd();
        int totalNodes = c.root.countNodes();
        int idx = rand.nextInt(totalNodes);
        Node donor = p2.root.cloneTree();
        c.root.replaceNode(idx, donor, new int[]{0});
        // Ensure depth after crossover
        if (c.depth() > MAX_DEPTH) {
            c = new Individual(randomTree(MAX_DEPTH, numFeatures));
        }
        return c;
    }

    // Mutation: replace random subtree
    private Individual mutate(Individual ind, int numFeatures) {
        Individual m = ind.cloneInd();
        int totalNodes = m.root.countNodes();
        int idx = rand.nextInt(totalNodes);
        Node newSubtree = randomTree(MAX_DEPTH / 2, numFeatures);
        m.root.replaceNode(idx, newSubtree, new int[]{0});
        // Ensure depth after mutation
        if (m.depth() > MAX_DEPTH) {
            m = new Individual(randomTree(MAX_DEPTH, numFeatures));
        }
        return m;
    }

    /**
     * Evaluate the best individual on test data
     */
    public EvaluationResult evaluate(Dataset testData) {
        int[] preds = new int[testData.size()];
        for (int i = 0; i < preds.length; i++) {
            preds[i] = bestIndividual == null ? 0 : bestIndividual.predict(testData.getFeatures()[i]);
        }
        lastPredictions = preds;
        double acc = EvaluationResult.accuracy(testData.getLabels(), preds);
        double f1 = EvaluationResult.f1Score(testData.getLabels(), preds);
        return new EvaluationResult(acc, f1);
    }

    public int[] getPredictions() {
        return lastPredictions != null ? lastPredictions : new int[0];
    }
}
