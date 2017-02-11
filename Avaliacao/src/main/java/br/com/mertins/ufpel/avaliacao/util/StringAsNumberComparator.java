package br.com.mertins.ufpel.avaliacao.util;

import java.io.File;
import java.util.Comparator;

/**
 *
 * @author mertins
 */
public class StringAsNumberComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof File && o2 instanceof File) {
            String so1 = ((File) o1).getName().toUpperCase().replace("MLP_", "");
            String so2 = ((File) o2).getName().toUpperCase().replace("MLP_", "");
            return Integer.parseInt(so1) - Integer.parseInt(so2);
        } else {
            throw new ClassCastException("Comparador para classes File apenas");
        }
    }

}
