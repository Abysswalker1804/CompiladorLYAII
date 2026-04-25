package org.example.lyaii.AST;

import org.example.lyaii.Automatas.AutomataID;
import org.example.lyaii.Automatas.AutomataNumero;
import org.example.lyaii.Enums.Palabras;
import org.example.lyaii.Enums.Tipos;
import org.example.lyaii.TablaSimbolos.Simbolo;
import org.example.lyaii.TablaSimbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.Arrays;

public class NodoPrograma extends Nodo{
    private NodoInicio inicio;
    private  NodoInstrucciones instrucciones;
    private NodoFin fin;
    public NodoPrograma(){
        inicio=new NodoInicio();
        instrucciones=new NodoInstrucciones();
        fin=new NodoFin();
        hijos.add(inicio);
        hijos.add(instrucciones);
        hijos.add(fin);
    }
    @Override
    public boolean check() {
        return true;
    }
    protected void addChild(Palabras palabra, String[] tokens){
        switch (palabra){
            case PR01 -> inicio.setWord(palabra);
            case PR02 -> fin.setWord(palabra);
            case PR03 -> declaracion(palabra, tokens);
        }
    }
    private void declaracion(Palabras palabra, String[] tokens){
        //Estructura de tokens: declare, tipo, id, =, ..., ;
        //Primero hay que resolver el valor de lo que se está asignando
        String[]sub= Arrays.copyOfRange(tokens,4, tokens.length);
        Tipos valor=resolver(sub);
        //insertar en tabla de simbolos:
        TablaSimbolos.insertar(tokens[2], Tipos.valueOf(tokens[1].toUpperCase()));
        instrucciones.addDeclaracion(valor,TablaSimbolos.consultar(tokens[2]));
    }
    private Tipos resolver(String [] tokens){
        boolean cadena=false,INT=false,UINT=false,FIXED=false,UFIXED=false;
        for(int i=0; i< tokens.length; i++){
            if(tokens[i].charAt(0)=='"' || tokens[i].equals("string")){
                //Es un valor de cadena
                cadena=true;
            }else if(AutomataID.analizar(tokens[i])){
                Simbolo sym=TablaSimbolos.consultar(tokens[i]);
                if(sym==null){
                    //No existe el identificador
                    return Tipos.NULL;
                }else{
                    switch (sym.getTipo()){
                        case INT -> INT=true;
                        case UINT -> UINT=true;
                        case FIXED -> FIXED=true;
                        case UFIXED -> UFIXED=true;
                    }
                }
            }else if(AutomataNumero.analizar(tokens[i])){
                int valor_int;
                double valor_fix;
                try{
                    valor_int=Integer.parseInt(tokens[i]);
                    if(valor_int>=0 && valor_int <= 65535){
                        INT=true;
                    }else if(valor_int>=-32768 && valor_int <= 32767){
                        UINT=true;
                    }else{
                        System.out.println("Fuera de los límites int/uint");
                        return Tipos.ERROR;
                    }
                }
                catch(NumberFormatException nfe){
                    //Entonces es fixed
                    valor_fix=Double.parseDouble(tokens[i]);
                    if(valor_fix>=-128 && valor_fix <= 127.99609375){
                        FIXED=true;
                    }else if(valor_fix>=0 && valor_fix <= 255.99609375){
                        UFIXED=true;
                    }else{
                        System.out.println("Fuera de los límites fixed/ufixed");
                        return Tipos.ERROR;
                    }
                }catch (Exception e){
                    System.out.println("Error desconocido");
                    return Tipos.ERROR;
                }
            }
        }
        int count = 0;
        if (cadena) count++;
        if (INT) count++;
        if (UINT) count++;
        if (FIXED) count++;
        if (UFIXED) count++;

        if (count == 1) {
            // exactamente uno es true
            if (cadena) return Tipos.STRING;
            if (INT) return Tipos.INT;
            if (UINT) return Tipos.UINT;
            if (FIXED) return Tipos.FIXED;
            if (UFIXED) return Tipos.UFIXED;
        }
        // error: ninguno o más de uno
        System.out.println("Más de un tipo");
        return Tipos.ERROR;
    }
}
