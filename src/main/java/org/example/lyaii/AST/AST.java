package org.example.lyaii.AST;

public class AST {
    private boolean flag_instrucciones=false;
    private NodoPrograma programa;
    public AST(){
        programa=new NodoPrograma();
    }
    public void addInicio(String inicio){
        programa.addInicio(inicio);
    }
    public void addFin(String fin){
        programa.addFin(fin);
    }
    public void createInstrucciones(){
        programa.createInstrucciones();
    }
    public void addInstrucciones(String instruccion){
        programa.addInstruccion(instruccion);;
    }
}
