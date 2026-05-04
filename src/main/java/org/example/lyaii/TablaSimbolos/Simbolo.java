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
        if(valor.equals("")) return;
        this.nombre=nombre;
        switch(tipo){
            case STRING:
                valor_str=valor;
                break;
            case INT:
            case UINT:
                int temp=0;
                try{
                    temp=Integer.parseInt(valor);
                    if(temp >= 0 && temp <= 32767){
                        if(tipo==Tipos.INT)
                            this.tipo=Tipos.INT;
                        else
                            this.tipo=Tipos.UINT;
                    }else if(temp < 0 && temp >= -32768)
                        this.tipo=Tipos.INT;
                    else if(temp > 32767 && temp <= 65535)
                        this.tipo=Tipos.UINT;
                    else
                        throw new IllegalArgumentException("Valor fuera de los límites de la capacidad!");
                    this.valor_int=temp;
                }
                catch(Exception e){
                    throw new IllegalArgumentException("Valor no compatible con INT ni con UINT");}
                break;
            case UFIXED:
            case FIXED:
                double aux=0;
                try{
                    aux=Double.parseDouble(valor);
                    if(aux >= 0 && aux <= 127.99609375){
                        if(tipo==Tipos.FIXED)
                            this.tipo=Tipos.FIXED;
                        else
                            this.tipo=Tipos.UFIXED;
                    }else if(aux < 0 && aux >= -128)
                        this.tipo=Tipos.FIXED;
                    else if(aux > 127.99609375 && aux <= 255.99609375)
                        this.tipo=Tipos.UFIXED;
                    else
                        throw new IllegalArgumentException("Valor fuera de los límites de la capacidad!");
                    this.valor_fix=aux;
                }
                catch(Exception e){
                    throw new IllegalArgumentException("Valor no compatible con FIXED ni con UFIXED");}
                break;
            case NULL:
                tipo=Tipos.NULL;
                break;
            default:
        }
        declarada=true;
    }
    public Simbolo(String nombre, Tipos tipo){
        this.nombre=nombre;
        this.tipo=tipo;
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

