package org.example.lyaii.AST;

import org.example.lyaii.Automatas.AutomataID;
import org.example.lyaii.Automatas.AutomataNumero;
import org.example.lyaii.Enums.Palabras;
import org.example.lyaii.Enums.Tipos;
import org.example.lyaii.TablaSimbolos.Simbolo;
import org.example.lyaii.TablaSimbolos.TablaSimbolos;
import org.example.lyaii.Tools.PilaAST;
import org.example.lyaii.Tools.PilaErrores;
import org.example.lyaii.Tools.ShuntingYard;

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
            case PR09 -> ciclo(tokens);
            case PR10 -> condicion_if(tokens);
            case PR11 -> condicion_else(palabra);
        }
    }
    protected void addAsignacion(String [] tokens){
        //Estructura: $id, =, ..., ;
        String[]sub= Arrays.copyOfRange(tokens,2, tokens.length);
        Tipos valor=resolver(sub,tokens[0],null);
        try{
            TablaSimbolos.actualizar(tokens[0],valor);
            Nodo nodoAnidado=PilaAST.peek();
            if(nodoAnidado==null)
                instrucciones.addAsignacion(valor,TablaSimbolos.consultar(tokens[0]));
            else
                nodoAnidado.hijos.add(instrucciones.giveAsignacion(valor,TablaSimbolos.consultar(tokens[0])));
        }catch (IllegalArgumentException iae){
            System.out.println(iae.getMessage());
            PilaErrores.push(iae.getMessage());
            Nodo nodoAnidado=PilaAST.peek();
            if(nodoAnidado==null)
                instrucciones.addAsignacion(Tipos.ERROR,TablaSimbolos.consultar(tokens[0]));
            else
                nodoAnidado.hijos.add(instrucciones.giveAsignacion(Tipos.ERROR,TablaSimbolos.consultar(tokens[0])));
        }
    }
    private void declaracion(Palabras palabra, String[] tokens){
        //Estructura de tokens: declare, tipo, id, =, ..., ;
        //Primero hay que resolver el valor de lo que se está asignando
        String[]sub= Arrays.copyOfRange(tokens,4, tokens.length);
        Tipos valor=resolver(sub,null,tokens[1]);
        //insertar en tabla de simbolos:
        try{
            TablaSimbolos.insertar(tokens[2], Tipos.valueOf(tokens[1].toUpperCase()));
            Nodo nodoAnidado=PilaAST.peek();
            if(nodoAnidado==null)
                instrucciones.addDeclaracion(valor,TablaSimbolos.consultar(tokens[2]));
            else
                nodoAnidado.hijos.add(instrucciones.giveDeclaracion(valor,TablaSimbolos.consultar(tokens[2])));
        }catch (IllegalArgumentException iae){
            //Ya existe el identificador
            System.out.println(iae.getMessage());
            PilaErrores.push(iae.getMessage());
            Nodo nodoAnidado=PilaAST.peek();
            if(nodoAnidado==null)
                instrucciones.addDeclaracion(Tipos.ERROR,TablaSimbolos.consultar(tokens[2]));
            else
                nodoAnidado.hijos.add(instrucciones.giveDeclaracion(Tipos.ERROR,TablaSimbolos.consultar(tokens[2])));
        }
    }
    private void ciclo(String[] tokens){
        //Estructura: loop, (, condicion, ).
        int i=0;
        do{i++;}while(!tokens[i].equals(")"));
        String[]sub=Arrays.copyOfRange(tokens,1,tokens.length);
        NodoCiclo temp;
        try{
            boolean condicion=resolverCondiciones(sub);
            temp=new NodoCiclo(condicion);
            Nodo nodoAnidado=PilaAST.peek();
            if(nodoAnidado==null)
                instrucciones.addChild(temp);
            else
                nodoAnidado.hijos.add(temp);
        }catch(IllegalArgumentException iae){
            PilaErrores.push(iae.getMessage());
            temp=new NodoCiclo(false);
            Nodo nodoAnidado=PilaAST.peek();
            if(nodoAnidado==null)
                instrucciones.addChild(temp);
            else
                nodoAnidado.hijos.add(temp);
        }
        PilaAST.push(temp);
    }
    private void condicion_if(String [] tokens){
        //Estructura: if, (, condicion, ).
        int i=0;
        do{i++;}while(!tokens[i].equals(")"));
        String[]sub=Arrays.copyOfRange(tokens,1,tokens.length);
        NodoIF temp;
        try{
            boolean condicion=resolverCondiciones(sub);
            temp=new NodoIF(condicion);
            Nodo nodoAnidado=PilaAST.peek();
            if(nodoAnidado==null)
                instrucciones.addChild(temp);
            else
                nodoAnidado.hijos.add(temp);
        }catch(IllegalArgumentException iae){
            PilaErrores.push(iae.getMessage());
            temp=new NodoIF(false);
            Nodo nodoAnidado=PilaAST.peek();
            if(nodoAnidado==null)
                instrucciones.addChild(temp);
            else
                nodoAnidado.hijos.add(temp);
        }
        PilaAST.push(temp);
    }
    private void condicion_else(Palabras palabra){
        NodoElse temp=new NodoElse(palabra);
        Nodo nodoAnidado=PilaAST.peek();
        if(nodoAnidado==null)
            instrucciones.addChild(temp);
        else
            nodoAnidado.hijos.add(temp);
        PilaAST.push(temp);
    }
    private Tipos resolver(String [] tokens, String asg, String dec){
        boolean cadena=false,INT=false,UINT=false,FIXED=false,UFIXED=false;
        for(int i=0; i< tokens.length; i++){
            if(tokens[i].charAt(0)=='"' || tokens[i].equals("string")){
                //Es un valor de cadena
                cadena=true;
                break;
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
                    if(valor_int >= 0 && valor_int <= 32767){
                        //Compatible con ambos
                        if(dec!=null){
                            //Declaracion
                            if(dec.equals("int"))
                                INT=true;
                            else
                                UINT=true;
                        }else{
                            //Asignacion
                            Simbolo s = TablaSimbolos.consultar(asg);
                            if(s==null)
                                return Tipos.ERROR;
                            else{
                                switch (s.getTipo()){
                                    case INT -> INT=true;
                                    case UINT -> UINT=true;
                                }
                            }
                        }
                    }else if(valor_int < 0 && valor_int >= -32768){
                        INT=true;
                    }else if(valor_int > 32767 && valor_int <= 65535){
                        UINT=true;
                    }else{
                        System.out.println("Fuera de los límites int/uint");
                        PilaErrores.push("Fuera de los límites int/uint");
                        return Tipos.ERROR;
                    }
                    double res=ShuntingYard.evaluador(ShuntingYard.toPostfix(tokens));
                    if(res < -32768 || res > 65535){
                        System.out.println("Expresión sobrepasa el límite permitido para alojar un valor!");
                        PilaErrores.push("Expresión sobrepasa el límite permitido para alojar un valor!");
                        return Tipos.ERROR;
                    }

                }
                catch(NumberFormatException nfe){
                    //Entonces es fixed
                    valor_fix=Double.parseDouble(tokens[i]);
                    if(valor_fix >= 0 && valor_fix <= 127.99609375){
                        //Compatible con ambos
                        if(dec!=null){
                            //Declaracion
                            if(dec.equals("fixed"))
                                FIXED=true;
                            else
                                UFIXED=true;
                        }else{
                            //Asignacion
                            Simbolo s = TablaSimbolos.consultar(asg);
                            if(s==null)
                                return Tipos.ERROR;
                            else{
                                switch (s.getTipo()){
                                    case FIXED -> FIXED=true;
                                    case UFIXED -> UFIXED=true;
                                }
                            }
                        }
                    }else if(valor_fix < 0 && valor_fix >= -128){
                        FIXED=true;
                    }else if(valor_fix > 127.99609375 && valor_fix <= 255.99609375){
                        UFIXED=true;
                    }else{
                        System.out.println("Fuera de los límites fixed/ufixed");
                        PilaErrores.push("Fuera de los límites fixed/ufixed");
                        return Tipos.ERROR;
                    }
                    double res=ShuntingYard.evaluador(ShuntingYard.toPostfix(tokens));
                    if(res < -128 || res > 255.99609375){
                        System.out.println("Expresión sobrepasa el límite permitido para alojar un valor!");
                        PilaErrores.push("Expresión sobrepasa el límite permitido para alojar un valor!");
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
    private boolean resolverCondiciones(String [] condicion){
        //Resolver que todos los valores sean numéricos o booleanos y dentro de los límites
        //Estructura (, ... ,)
        for(int i=1; i<=condicion.length-2; i++){
            if(AutomataID.analizar(condicion[i])){
                //Revisarlo en la tabla
                Simbolo sym=TablaSimbolos.consultar(condicion[i]);
                if(sym==null)
                    throw new IllegalArgumentException(condicion[i]+" no ha sido declarado.");
                else{
                    try{
                        int valor_int = Integer.parseInt(condicion[i]);
                        if (valor_int < -32768 || valor_int > 65535)
                            return false;
                    }catch (Exception e){
                        //Entonces es fixed
                        double valor_fixed=Double.parseDouble(condicion[i]);
                        if(valor_fixed < -128 || valor_fixed > 255.99609375){
                            return false;
                        }
                    }
                }
            }else if(AutomataNumero.analizar(condicion[i])){
                //Asumir que es un número
                try{
                    int valor_int = Integer.parseInt(condicion[i]);
                    if (valor_int < -32768 || valor_int > 65535)
                        return false;
                }catch (Exception e){
                    //Entonces es fixed
                    double valor_fixed=Double.parseDouble(condicion[i]);
                    if(valor_fixed < -128 || valor_fixed > 255.99609375){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
