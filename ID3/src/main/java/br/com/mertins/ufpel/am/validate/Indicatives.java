package br.com.mertins.ufpel.am.validate;

import br.com.mertins.ufpel.am.preparacao.Label;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mertins
 */
public class Indicatives {

    private final Map<Label, Estrutura> indicadores;
    private BigDecimal sizeSamples;

    Indicatives(long sizeSamples) {
        this.indicadores = new HashMap();
        this.sizeSamples = new BigDecimal(sizeSamples);
    }

    public void add(Label label, long truePositives, Map<Label, Long> falsePositives, long trueNegatives, Map<Label, Long> falseNegatives) {
        indicadores.put(label, new Estrutura(truePositives, falsePositives, trueNegatives, falseNegatives));
    }

    public BigDecimal getTruePositives(Label label) {
        return this.indicadores.get(label).getTruePositives();
    }

    public BigDecimal ammountTruePositives() {
        BigDecimal ret = BigDecimal.ZERO;
        for (Label key : this.indicadores.keySet()) {
            ret = ret.add(this.indicadores.get(key).truePositives);
        }
        return ret;
    }

    public BigDecimal getTrueNegatives(Label label) {
        return this.indicadores.get(label).getTrueNegatives();
    }

    public BigDecimal ammountTrueNegatives() {
        BigDecimal ret = BigDecimal.ZERO;
        for (Label key : this.indicadores.keySet()) {
            ret = ret.add(this.indicadores.get(key).trueNegatives);
        }
        return ret;
    }

    public Map<Label, BigDecimal> getFalsePositives(Label label) {
        return this.indicadores.get(label).getFalsePositives();
    }

    public Map<Label, BigDecimal> getFalseNegatives(Label label) {
        return this.indicadores.get(label).getFalseNegatives();
    }

    public BigDecimal ammountFalsePositives(Label label) {
        Map<Label, BigDecimal> falsePositives = this.getFalsePositives(label);
        BigDecimal ret = BigDecimal.ZERO;
        if (falsePositives != null) {
            for (Label key : falsePositives.keySet()) {
                ret = ret.add(falsePositives.get(key));
            }
        }
        return ret;
    }

    public BigDecimal ammountFalseNegatives(Label label) {
        Map<Label, BigDecimal> falseNegatives = this.getFalseNegatives(label);
        BigDecimal ret = BigDecimal.ZERO;
        if (falseNegatives != null) {
            for (Label key : falseNegatives.keySet()) {
                ret = ret.add(falseNegatives.get(key));
            }
        }
        return ret;
    }

    public BigDecimal accuracy() {
        if (!this.indicadores.isEmpty()) {
            try {
                BigDecimal dividendo = this.ammountTruePositives();
                BigDecimal divisor = sizeSamples;
                return dividendo.divide(divisor, 3, RoundingMode.HALF_UP);
            } catch (Exception ex) {
                return BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal precision(Label label) {
        try {
            Estrutura estrutura = this.indicadores.get(label);
            Map<Label, BigDecimal> falsePositives = estrutura.getFalsePositives();
            BigDecimal falsePositive = falsePositives.get(label) == null ? BigDecimal.ZERO : falsePositives.get(label);
            BigDecimal divisor = estrutura.getTruePositives().add(falsePositive);
            return estrutura.getTruePositives().divide(divisor, 3, RoundingMode.HALF_UP);
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal recall(Label label) {
        try {
            Estrutura estrutura = this.indicadores.get(label);
            Map<Label, BigDecimal> falseNegatives = estrutura.getFalseNegatives();
            BigDecimal falseNegative = falseNegatives.get(label) == null ? BigDecimal.ZERO : falseNegatives.get(label);
            BigDecimal divisor = estrutura.getTruePositives().add(falseNegative);
            return estrutura.getTruePositives().divide(divisor, 3, RoundingMode.HALF_UP);
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal f1(Label label) {
        try {
            BigDecimal precision = this.precision(label);
            BigDecimal recall = this.recall(label);
            BigDecimal dividendo = precision.multiply(recall);
            BigDecimal divisor = precision.add(recall);
            return dividendo.divide(divisor, 3, RoundingMode.HALF_UP);
        } catch (Exception ex) {
            return BigDecimal.ZERO;

        }
    }

    private class Estrutura {

        private final BigDecimal truePositives;
        private final BigDecimal trueNegatives;
        private final Map<Label, BigDecimal> falsePositives = new HashMap<>();
        private final Map<Label, BigDecimal> falseNegatives = new HashMap<>();

        Estrutura(long truePositives, Map<Label, Long> falsePositives, long trueNegatives, Map<Label, Long> falseNegatives) {
            this.truePositives = new BigDecimal(truePositives);
            falsePositives.keySet().forEach(key -> {
                this.falsePositives.put(key, new BigDecimal(falsePositives.get(key)));
            });
            this.trueNegatives = new BigDecimal(trueNegatives);
            falseNegatives.keySet().forEach(key -> {
                this.falseNegatives.put(key, new BigDecimal(falseNegatives.get(key)));
            });
        }

        public BigDecimal getTruePositives() {
            return truePositives;
        }

        public BigDecimal getTrueNegatives() {
            return trueNegatives;
        }

        public Map<Label, BigDecimal> getFalsePositives() {
            return falsePositives;
        }

        public Map<Label, BigDecimal> getFalseNegatives() {
            return falseNegatives;
        }

    }

}
