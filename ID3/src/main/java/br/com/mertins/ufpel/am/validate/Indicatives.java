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

    Indicatives() {
        this.indicadores = new HashMap();
    }

    public void add(Label label, long truePositives, long falsePositives, long trueNegatives, long falseNegatives) {
        indicadores.put(label, new Estrutura(truePositives, falsePositives, trueNegatives, falseNegatives));
    }

    public BigDecimal getTruePositives(Label label) {
        return this.indicadores.get(label).getTruePositives();
    }

    public BigDecimal getTrueNegatives(Label label) {
        return this.indicadores.get(label).getTrueNegatives();
    }

    public BigDecimal getFalsePositives(Label label) {
        return this.indicadores.get(label).getFalsePositives();
    }

    public BigDecimal getFalseNegatives(Label label) {
        return this.indicadores.get(label).getFalseNegatives();
    }

    public BigDecimal accuracy() {
        if (!this.indicadores.isEmpty()) {
            for (Label label : this.indicadores.keySet()) {
                Estrutura estrutura = this.indicadores.get(label);
                try {
                    BigDecimal dividendo = estrutura.getTruePositives().add(estrutura.getTrueNegatives());
                    BigDecimal divisor = estrutura.getTruePositives().add(estrutura.getTrueNegatives().add(
                            estrutura.getFalsePositives().add(estrutura.getFalseNegatives())));
                    return dividendo.divide(divisor, 3, RoundingMode.HALF_UP);
                } catch (Exception ex) {
                    return BigDecimal.ZERO;
                }
            }
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal precision(Label label) {
        try {
            Estrutura estrutura = this.indicadores.get(label);
            BigDecimal divisor = estrutura.getTruePositives().add(estrutura.getFalsePositives());
            return estrutura.getTruePositives().divide(divisor, 3, RoundingMode.HALF_UP);
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal recall(Label label) {
        try {
            Estrutura estrutura = this.indicadores.get(label);
            BigDecimal divisor = estrutura.getTruePositives().add(estrutura.getFalseNegatives());
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
        private final BigDecimal falsePositives;
        private final BigDecimal falseNegatives;

        Estrutura(long truePositives, long falsePositives, long trueNegatives, long falseNegatives) {
            this.truePositives = new BigDecimal(truePositives);
            this.falsePositives = new BigDecimal(falsePositives);
            this.trueNegatives = new BigDecimal(trueNegatives);
            this.falseNegatives = new BigDecimal(falseNegatives);
        }

        public BigDecimal getTruePositives() {
            return truePositives;
        }

        public BigDecimal getTrueNegatives() {
            return trueNegatives;
        }

        public BigDecimal getFalsePositives() {
            return falsePositives;
        }

        public BigDecimal getFalseNegatives() {
            return falseNegatives;
        }

    }

}
