package org.example.lyaii.TablaSimbolos;

import java.util.HashMap;
import java.util.Map;

import org.example.lyaii.Enums.Tipos;

public class TablaSimbolos {
    private Map<String, Simbolo> tabla;
    
    public TablaSimbolos(){
        tabla = new HashMap<>();
    }
    public TablaSimbolos(String nombre, Tipos tipo, String valor){
        tabla = new HashMap<>();
        tabla.put(nombre, new Simbolo(nombre, tipo, valor));
    }
    public Simbolo consultar(String nombre){
        if(tabla.containsKey(nombre))
            return tabla.get(nombre);
        else
            return null;
    }
    //Este método lanza excepciones
    public void insertar(String nombre, Tipos tipo, String valor){
        if(tabla.containsKey(nombre)){
            throw new IllegalArgumentException("Identificador ya existente");
        }else{
            tabla.put(nombre, new Simbolo(nombre,tipo,valor));
        }
    }
    public void actualizar(String nombre, Tipos tipo, String valor){
        if(tabla.containsKey(nombre)){
            tabla.put(nombre, new Simbolo(nombre,tipo,valor));
        }else{
            throw new IllegalArgumentException("Identificador no existente");
        }
    }
    public void printTabla(){
        tabla.forEach((nombre,simbolo) ->{
            String str;
            double num;
            switch (simbolo.getTipo()) {
                case STRING:
                    str=simbolo.getValorStr();
                    System.out.println("Identificador: "+simbolo.getNombre()+", Tipo: "+simbolo.getTipo()+", Valor: "+str);
                    break;
                case INT:
                case UINT:
                    num=simbolo.getValorInt();
                    System.out.println("Identificador: "+simbolo.getNombre()+", Tipo: "+simbolo.getTipo()+", Valor: "+(int)num);
                    break;
                case UFIXED:
                case FIXED:
                    num=simbolo.getValorFix();
                    System.out.println("Identificador: "+simbolo.getNombre()+", Tipo: "+simbolo.getTipo()+", Valor: "+num);
                    break;
                default:
                    break;
            }
        });
    }
}
