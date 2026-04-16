package org.example.lyaii.AST;

import java.util.List;

public abstract class Nodo {
    protected List<Nodo> hijos;
    public abstract boolean check();
}
