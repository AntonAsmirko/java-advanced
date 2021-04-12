package info.kgeorgiy.ja.asmirko.concurrent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton Asmirko
 */

class VarReference<T> {

    private List<T> var = new ArrayList<>();

    public VarReference(T var) {
        this.var.add(var);
    }

    public List<T> getVar() {
        return var;
    }

    public void addVar(T var) {
        this.var.add(var);
    }
}
