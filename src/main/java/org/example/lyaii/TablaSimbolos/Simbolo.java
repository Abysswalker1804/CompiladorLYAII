package org.example.lyaii.TablaSimbolos;

import org.example.lyaii.Enums.Tipos;

public class Simbolo{
    private String nombre;
    private Tipos tipo;
    private String valor_str;
    private int valor_int;
    private double valor_fix;
    private boolean declarada;

    public Simbolo(String nombre){
        this.nombre=nombre;
        tipo=Tipos.NULL;
        valor_str="";
        valor_int=0;
        valor_fix=0.0;
        declarada=false;
    }

    //Este método lanza excepciones personalizadas
    public Simbolo(String nombre, Tipos tipo, String valor){
        this.nombre=nombre;
        int temp;
        switch(tipo){
            case STRING:
                valor_str=valor;
                break;
            case INT:
            case UINT:
                temp=0;
                try{temp=Integer.parseInt(valor);}
                catch(Exception e){
                    throw new IllegalArgumentException("Valor no compatible con INT ni con UINT");}
                if(tipo==Tipos.UINT && valor_int>=0 && valor_int <= 65535){
                    this.valor_int=temp;
                    this.tipo=Tipos.UINT;
                }else if(tipo==Tipos.INT && valor_int>=-32768 && valor_int <= 32767){
                    this.valor_int=temp;
                    this.tipo=Tipos.INT;
                }else{
                    throw new IndexOutOfBoundsException("Valor fuera de límites para tipo de dato INT o UINT");
                }
                break;
            case UFIXED:
            case FIXED:
                temp=0;
                try{temp=Integer.parseInt(valor);}
                catch(Exception e){
                    throw new IllegalArgumentException("Valor no compatible con FIXED ni con UFIXED");}
                if(tipo==Tipos.FIXED && valor_fix>=-128 && valor_fix <= 127.99609375){
                    this.valor_fix=temp;
                    this.tipo=Tipos.FIXED;
                }else if(tipo==Tipos.UFIXED && valor_fix>=0 && valor_fix <= 255.99609375){
                    this.valor_fix=temp;
                    this.tipo=Tipos.UFIXED;
                }else{
                    throw new IndexOutOfBoundsException("Valor fuera de límites para tipo de dato FIXED o UFIXED");
                }
                break;
            case NULL:
                tipo=Tipos.NULL;
                break;
            default:
        }
        declarada=true;
    }

    public void setNombre(String nombre){
        this.nombre=nombre;
    }
    public String getNombre(){
        return this.nombre;
    }
    public void setTipo(Tipos tipo){
        this.tipo=tipo;
    }
    public Tipos getTipo(){
        return this.tipo;
    }
    public void setValorStr(String valor){
        valor_str=valor;
    }
    public String getValorStr(){
        return valor_str;
    }
    public void setValorInt(int valor){
        valor_int=valor;
    }
    public int getValorInt(){
        return valor_int;
    }
    public void setValorFix(double valor){
        valor_fix=valor;
    }
    public double getValorFix(){
        return valor_fix;
    }
    public void setDec(boolean bol){
        this.declarada=bol;
    }
    public boolean getDec(){
        return declarada;
    }
}

