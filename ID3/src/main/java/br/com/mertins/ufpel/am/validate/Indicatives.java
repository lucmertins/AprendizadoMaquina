package br.com.mertins.ufpel.am.validate;

/**
 *
 * @author mertins
 */
public class Indicatives {

    private long verdadeirosPositivos;
    private long verdadeirosNegativos;
    private long falsosPositivos;
    private long falsosNegativos;

    public Indicatives() {
    }

    public long getVerdadeirosPositivos() {
        return verdadeirosPositivos;
    }

    public long getVerdadeirosNegativos() {
        return verdadeirosNegativos;
    }

    public long getFalsosPositivos() {
        return falsosPositivos;
    }

    public long getFalsosNegativos() {
        return falsosNegativos;
    }

    public void addVerdadeirosPositivos() {
        this.verdadeirosPositivos++;
    }

    public void addVerdadeirosNegativos() {
        this.verdadeirosNegativos++;
    }

    public void addFalsosPositivos() {
        this.falsosPositivos++;
    }

    public void addFalsosNegativos() {
        this.falsosNegativos++;
    }

    public void reset() {
        this.verdadeirosPositivos = 0;
        this.verdadeirosNegativos = 0;
        this.falsosPositivos = 0;
        this.falsosNegativos = 0;

    }
}
