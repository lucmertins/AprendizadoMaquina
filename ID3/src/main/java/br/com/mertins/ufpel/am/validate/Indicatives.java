package br.com.mertins.ufpel.am.validate;

import br.com.mertins.ufpel.am.preparacao.Label;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 *
 * @author mertins
 */
public class Indicatives {

    private final Label label;
    private BigDecimal truePositives;
    private BigDecimal trueNegatives;
    private BigDecimal falsePositives;
    private BigDecimal falseNegatives;

    Indicatives(Label label, long truePositives, long falsePositives, long trueNegatives, long falseNegatives) {
        this.label = label;
        this.truePositives = new BigDecimal(truePositives);
        this.falsePositives = new BigDecimal(falsePositives);
        this.trueNegatives = new BigDecimal(trueNegatives);
        this.falseNegatives = new BigDecimal(falseNegatives);
    }

    public void add(Label label, long truePositives, long falsePositives, long trueNegatives, long falseNegatives) {

    }

    public Label getLabel() {
        return label;
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

    public void addVerdadeirosPositivos() {
        this.truePositives = this.truePositives.add(BigDecimal.ONE);
    }

    public void addVerdadeirosNegativos() {
        this.trueNegatives = this.trueNegatives.add(BigDecimal.ONE);
    }

    public void addFalsosPositivos() {
        this.falsePositives = this.falsePositives.add(BigDecimal.ONE);
    }

    public void addFalsosNegativos() {
        this.falseNegatives = this.falseNegatives.add(BigDecimal.ONE);
    }

    public BigDecimal accuracy() {
        try {
            BigDecimal dividendo = this.truePositives.add(this.trueNegatives);
            BigDecimal divisor = this.truePositives.add(this.trueNegatives.add(this.falsePositives.add(this.falseNegatives)));
            return dividendo.divide(divisor, 3, RoundingMode.HALF_UP);
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal precision() {
        try {
            BigDecimal divisor = this.truePositives.add(this.falsePositives);
            return this.truePositives.divide(divisor, 3, RoundingMode.HALF_UP);
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal recall() {
        try {
            BigDecimal divisor = this.truePositives.add(this.falseNegatives);
            return this.truePositives.divide(divisor, 3, RoundingMode.HALF_UP);
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal f1() {
        try {
            BigDecimal precision = this.precision();
            BigDecimal recall = this.recall();
            BigDecimal dividendo = precision.multiply(recall);
            BigDecimal divisor = precision.add(recall);
            return dividendo.divide(divisor, 3, RoundingMode.HALF_UP);
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

//    public void reset() {
//        this.truePositives = BigDecimal.ZERO;
//        this.trueNegatives = BigDecimal.ZERO;
//        this.falsePositives = BigDecimal.ZERO;
//        this.falseNegatives = BigDecimal.ZERO;
//
//    }
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.label);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Indicatives other = (Indicatives) obj;
        if (!Objects.equals(this.label, other.label)) {
            return false;
        }
        return true;
    }

}
