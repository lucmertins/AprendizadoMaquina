package br.com.mertins.ufpel.am.id3;

import br.com.mertins.ufpel.am.preparacao.Attribute;
import br.com.mertins.ufpel.am.preparacao.Label;
import br.com.mertins.ufpel.am.preparacao.Register;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mertins
 */
public class Entropy {

    private final List<Register> registers;
    private final Set<Label> labels;

    private Map<Attribute, Double> result;

    public Entropy(List<Register> registers,Set<Label> labels) {
        this.registers = registers;
        this.labels=labels;
    }
    
    public void process(){
        
    }

}
