package org.example.lyaii.Tools;

public class PilaErrores {
    private static Pila pila=new Pila();
    public static void push(String error){
        pila.push(error);
    }
    public static String pop(){
        return pila.pop();
    }
    public static String peek(){
        return pila.peek();
    }
}
