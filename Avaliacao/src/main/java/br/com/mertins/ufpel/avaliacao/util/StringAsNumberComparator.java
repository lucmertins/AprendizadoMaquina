package br.com.mertins.ufpel.avaliacao.util;

import java.io.File;
import java.util.Comparator;

/**
 *
 * @author mertins
 */
public class StringAsNumberComparator implements Comparator {
    
    private String namefileBegin;
    private boolean version;
    
    public StringAsNumberComparator(String namefileBegin, boolean version) {
        this.namefileBegin = namefileBegin.toUpperCase();
        this.version = version;
    }
    
    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof File && o2 instanceof File) {
            String so1 = ((File) o1).getName().toUpperCase().replace(this.namefileBegin, "");
            String so2 = ((File) o2).getName().toUpperCase().replace(this.namefileBegin, "");
            if (version) {
                return Integer.parseInt(so1.substring(0, so1.indexOf('_'))) - Integer.parseInt(so2.substring(0, so2.indexOf('_')));
            } else {
                return Integer.parseInt(so1) - Integer.parseInt(so2);
            }
        } else {
            throw new ClassCastException("Comparador para classes File apenas");
        }
    }
    
}
