package info.kgeorgiy.ja.asmirko.concurrent;

/**
 * @author Anton Asmirko
 */

class VarReference<T> {

    private T var;

    public VarReference(T var) {
        this.var = var;
    }

    public T getVar() {
        return var;
    }

    public void setVar(T var) {
        this.var = var;
    }
}
