package br.com.mertins.ufpel.avaliacao.redeneural;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mertins
 */
public class Accumulator {
      private final int label;
        private double truePositive;
        private double trueNegative;
        private final Map<Integer, Double> falsePositive = new HashMap<>();
        private final Map<Integer, Double> falseNegative = new HashMap<>();

        Accumulator(int label) {
            this.label = label;
        }

        public int getLabel() {
            return label;
        }

        public double getTruePositive() {
            return truePositive;
        }

        public Map<Integer, Double> getFalsePositive() {
            return falsePositive;
        }

        public double getTrueNegative() {
            return trueNegative;
        }

        public Map<Integer, Double> getFalseNegative() {
            return falseNegative;
        }

        public void addTruePositive() {
            this.truePositive++;
        }

        public void addFalsePositive(int label) {
            if (falsePositive.containsKey(label)) {
                falsePositive.put(label, falsePositive.get(label) + 1);
            } else {
                falsePositive.put(label, 1.0);
            }
        }

        public void addTrueNegative() {
            this.trueNegative++;
        }

        public void addFalseNegative(int label) {
            if (falseNegative.containsKey(label)) {
                falseNegative.put(label, falseNegative.get(label) + 1);
            } else {
                falseNegative.put(label, 1.0);
            }
        }

        public double totalFalsePositive() {
            double ret = 0;
            ret = this.falsePositive.keySet().stream().map((key) -> this.falsePositive.get(key)).reduce(ret, (accumulator, _item) -> accumulator + _item);
            return ret;
        }

        public double totalFalseNegative() {
            double ret = 0;
            ret = this.falseNegative.keySet().stream().map((key) -> this.falseNegative.get(key)).reduce(ret, (accumulator, _item) -> accumulator + _item);
            return ret;
        }

        public double totalAcumulado() {
            return truePositive + trueNegative + totalFalseNegative() + totalFalsePositive();
        }

        public double accuracy() {
            return (this.getTruePositive() + this.getTrueNegative()) / this.totalAcumulado();
        }

        public double precision() {
            return this.truePositive / (this.truePositive + this.totalFalsePositive());
        }

        public double recall() {
            return this.truePositive / (this.truePositive + this.totalFalseNegative());
        }

        public double f1() {
            double prec = this.precision();
            double rec = this.recall();
            return 2 * (prec * rec / (prec + rec));
        }
}
