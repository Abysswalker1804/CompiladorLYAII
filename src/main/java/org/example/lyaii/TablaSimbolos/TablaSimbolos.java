package org.example.lyaii.TablaSimbolos;

import java.util.HashMap;
import java.util.Map;

import org.example.lyaii.Enums.Tipos;

public class TablaSimbolos {
    private static Map<String, Simbolo> tabla = new HashMap<>();
    public static Simbolo consultar(String nombre){
        return tabla.get(nombre);
    }
    //Este método lanza excepciones
    public static void insertar(String nombre, Tipos tipo, String valor){
        if(tabla.containsKey(nombre))
            throw new IllegalArgumentException("Identificador ya existente");
        else
            tabla.put(nombre, new Simbolo(nombre,tipo,valor));
    }
    public static void insertar(String nombre, Tipos tipo){
        if(tabla.containsKey(nombre))
            throw new IllegalArgumentException("Identificador ya existente");
        else
            tabla.put(nombre, new Simbolo(nombre,tipo));
    }
    public static void actualizar(String nombre, Tipos tipo, String valor){
        if(tabla.containsKey(nombre)) {
            Tipos oldType=tabla.get(nombre).getTipo();
            if(oldType==tipo)
                tabla.put(nombre, new Simbolo(nombre, tipo, valor));
            else
                throw new IllegalArgumentException("Incompatibilidad de tipos en asignación");
        }else
            throw new IllegalArgumentException("Identificador no existente");
    }
    public static void actualizar(String nombre, Tipos tipo){
        if(tabla.containsKey(nombre)) {
            Tipos oldType=tabla.get(nombre).getTipo();
            if(oldType==tipo)
                tabla.put(nombre, new Simbolo(nombre, tipo));
            else
                throw new IllegalArgumentException("Incompatibilidad de tipos en asignación");
        }else
            throw new IllegalArgumentException("Identificador no existente");
    }
    public static void printTabla(){
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
