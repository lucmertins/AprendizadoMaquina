package br.com.mertins.ufpel.knn;

/**
 *
 * @author mertins
 */
public class KnnInformation implements Comparable<KnnInformation> {

    private final int posicao;
    private final double distancia;
    private final String label;

    public KnnInformation(int posicao, double distancia, String label) {
        this.posicao = posicao;
        this.distancia = distancia;
        this.label = label;
    }

    public int getPosicao() {
        return posicao;
    }

    public double getDistancia() {
        return distancia;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public int compareTo(KnnInformation o) {
        double value = this.distancia - o.distancia;
        return value > 0 ? 1 : value < 0 ? -1 : 0;
    }

}
