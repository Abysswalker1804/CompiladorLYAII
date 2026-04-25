package org.example.lyaii.AST;

import org.example.lyaii.Enums.Palabras;
import org.example.lyaii.TablaSimbolos.Simbolo;

import java.util.Arrays;

public class AST {
    private boolean flag_instrucciones=false;
    private NodoPrograma programa;
    public static void main(String []args){
        AST arbol=new AST();
        String [] tokens={"begin","declare","string","$id","=","string","(","9",")",";","end"};
        arbol.crearAST(tokens);
        boolean flag=arbol.validar(arbol.getPrograma());
        System.out.println(flag?"Correcto":"Incorrecto");
    }
    public AST(){
        programa=new NodoPrograma();
    }
    public void crearAST(String [] tokens){
        for(int i=0; i<tokens.length; i++){
            switch (tokens[i]){
                case "begin":
                    addChild(Palabras.PR01);
                    break;
                case "end":
                    addChild(Palabras.PR02);
                    break;
                case "declare":
                    //Recortar un pequeño arreglo y mandarlo para crear el nodo
                    String [] sub;
                    int j=i;
                    do{j++;}while(!tokens[j].equals(";") && j<tokens.length);
                    sub=Arrays.copyOfRange(tokens,i,j);
                    addChild(Palabras.PR03,sub);
                    break;
            }
        }
    }
    public boolean validar(Nodo nodo) {
        if (nodo == null) return true;

        // Si este nodo falla, ya no sigue
        if (!nodo.check()) return false;

        // Revisar todos los hijos
        for (Nodo hijo : nodo.hijos) {
            if (!validar(hijo)) {
                return false;
            }
        }
        return true;
    }
    public NodoPrograma getPrograma(){
        return programa;
    }
    private void addChild(Palabras palabra){
        programa.addChild(palabra,null);
    }
    private void addChild(Palabras palabra, String[] tokens){
        programa.addChild(palabra, tokens);
    }
}
