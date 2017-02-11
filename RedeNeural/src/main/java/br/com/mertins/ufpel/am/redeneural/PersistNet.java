package br.com.mertins.ufpel.am.redeneural;

import java.io.IOException;

/**
 *
 * @author mertins
 */
public interface PersistNet {
    void save(MLP net,String value) throws IOException;
}
