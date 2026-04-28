package org.example.lyaii.AST;

import org.example.lyaii.Automatas.AutomataID;
import org.example.lyaii.Enums.Palabras;
import org.example.lyaii.TablaSimbolos.Simbolo;

import java.util.Arrays;

public class AST {
    private boolean flag_instrucciones=false;
    private NodoPrograma programa;
    public static void main(String []args){
        AST arbol=new AST();
        String [] tokens={"begin","declare","uint","$var","=","33000",";","end"};
        arbol.crearAST(tokens);
        boolean flag=arbol.validar(arbol.getPrograma());
        System.out.println(flag?"Correcto":"Incorrecto");
    }
    public AST(){
        programa=new NodoPrograma();
    }
    public void crearAST(String [] tokens){
        String [] sub;
        int j;
        for(int i=0; i<tokens.length; i++){
            if(tokens[i].equals("begin")){
                addChild(Palabras.PR01);
            }else if(tokens[i].equals("end")){
                addChild(Palabras.PR02);
            }else if(tokens[i].equals("loop")){
                //Recortar un pequeño arreglo hasta ")" y mandarlo para crear el nodo
                j=i;
                do{j++;}while(!tokens[j].equals(")"));
                sub=Arrays.copyOfRange(tokens,i,j+1);
                addChild(Palabras.PR09, sub);
            }else{
                //Recortar un pequeño arreglo y mandarlo para crear el nodo
                j=i;
                do{j++;}while(!tokens[j].equals(";"));
                sub=Arrays.copyOfRange(tokens,i,j);
                switch (tokens[i]){
                    case "declare":
                        addChild(Palabras.PR03, sub);
                        break;
                    case "/*":
                        //Omitir comentarios
                        break;
                    default:
                        if(AutomataID.analizar(tokens[i])){
                            //Es una asignación
                            //Estructura: $id, =, ..., ;
                            j=i;
                            do{j++;}while(!tokens[j].equals(";") && j<tokens.length);
                            sub=Arrays.copyOfRange(tokens,i,j);
                            addAsignacion(sub);
                        }
                }
                i=j;
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
    private void addAsignacion(String [] sub){
        programa.addAsignacion(sub);
    }
}
