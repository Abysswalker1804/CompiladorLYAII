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
    //Este método vacía la pila entera
    public static String[] dump(){
        int size=pila.size();
        String[] temp=new String[size];
        for(int i=0; i<size;i++){temp[i]=pila.pop();}
        return temp;
    }
}
