package br.com.mertins.ufpel.am.validate;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author mertins
 */
public class Indicatives {

    private BigDecimal verdadeirosPositivos;
    private BigDecimal verdadeirosNegativos;
    private BigDecimal falsosPositivos;
    private BigDecimal falsosNegativos;

    public Indicatives() {
        reset();
    }

    public BigDecimal getVerdadeirosPositivos() {
        return verdadeirosPositivos;
    }

    public BigDecimal getVerdadeirosNegativos() {
        return verdadeirosNegativos;
    }

    public BigDecimal getFalsosPositivos() {
        return falsosPositivos;
    }

    public BigDecimal getFalsosNegativos() {
        return falsosNegativos;
    }

    public void addVerdadeirosPositivos() {
        this.verdadeirosPositivos = this.verdadeirosPositivos.add(BigDecimal.ONE);
    }

    public void addVerdadeirosNegativos() {
        this.verdadeirosNegativos = this.verdadeirosNegativos.add(BigDecimal.ONE);
    }

    public void addFalsosPositivos() {
        this.falsosPositivos = this.falsosPositivos.add(BigDecimal.ONE);
    }

    public void addFalsosNegativos() {
        this.falsosNegativos = this.falsosNegativos.add(BigDecimal.ONE);
    }

    public BigDecimal accuracy() {
        try {
            BigDecimal dividendo = this.verdadeirosPositivos.add(this.verdadeirosNegativos);
            BigDecimal divisor = this.verdadeirosPositivos.add(this.verdadeirosNegativos.add(this.falsosPositivos.add(this.falsosNegativos)));
            return dividendo.divide(divisor, 3, RoundingMode.HALF_UP);
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal precision() {
        try {
            BigDecimal divisor = this.verdadeirosPositivos.add(this.falsosPositivos);
            return this.verdadeirosPositivos.divide(divisor, 3, RoundingMode.HALF_UP);
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal recall() {
        try {
            BigDecimal divisor = this.verdadeirosPositivos.add(this.falsosNegativos);
            return this.verdadeirosPositivos.divide(divisor, 3, RoundingMode.HALF_UP);
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

    public void reset() {
        this.verdadeirosPositivos = BigDecimal.ZERO;
        this.verdadeirosNegativos = BigDecimal.ZERO;
        this.falsosPositivos = BigDecimal.ZERO;
        this.falsosNegativos = BigDecimal.ZERO;

    }
}
