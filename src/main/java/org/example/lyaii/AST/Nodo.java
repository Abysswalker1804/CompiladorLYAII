package org.example.lyaii.AST;

import java.util.ArrayList;
import java.util.List;

public abstract class Nodo {
    protected List<Nodo> hijos= new ArrayList<>();;
    public abstract boolean check();
}
