package org.example.lyaii.Tools;

import java.util.HashMap;
import java.util.Map;

public class ShuntingYard {
    private static final Map<String,Integer> precedencia=new HashMap<>();
    static {
        precedencia.put("+",1);
        precedencia.put("-",1);
        precedencia.put("*",2);
        precedencia.put("/",2);
        precedencia.put("**",3);
        precedencia.put("(",4);
        precedencia.put(")",4);

        precedencia.put("||",1);
        precedencia.put("&&",2);
        precedencia.put("!",3);
    }
    public static void main(String [] args){
        //String [] tokens={"3", "+", "4", "*", "2" ,"/", "(", "1", "-", "5", ")","**" ,"2", "**", "3"};
        String [] tokens={"A", "&&", "B", "||","C"};
        tokens=toPostfix(tokens);
        print(tokens);
    }
    public static String [] toPostfix(String [] expresion){
        Pila pila=new Pila();
        Cola cola=new Cola();
        String peek;
        for(String token: expresion){
            if(precedencia.containsKey(token)){
                if(token.equals("(")){
                    pila.push(token);
                } else if(token.equals(")")){
                    while(pila.peek()!=null && !pila.peek().equals("(")){cola.enqueue(pila.pop());}
                    pila.pop();
                }else{
                    peek = pila.peek();
                    if(peek==null)
                        pila.push(token);
                    else{
                        while (pila.peek() != null && !pila.peek().equals("(")) {
                            String top = pila.peek();

                            int precTop = precedencia.get(top);
                            int precToken = precedencia.get(token);

                            boolean esIzq = !token.equals("**"); // asociatividad por la derecha

                            if (
                                    (esIzq && precToken <= precTop) ||
                                            (!esIzq && precToken < precTop)
                            ) {
                                cola.enqueue(pila.pop());
                            } else {
                                break;
                            }
                        }
                        pila.push(token);
                    }
                }
            }else{
                cola.enqueue(token);
            }
        }
        while(pila.peek()!=null){
            cola.enqueue(pila.pop());
        }
        String [] aux=new String[cola.length()];
        for (int i=0; i<aux.length; i++){
            aux[i]=cola.dequeue();
        }
        return aux;
    }
    public static void print(String [] tokens){
        String cad="";
        for(String token: tokens){
            cad=(cad.isEmpty()?token:cad+" | "+token);
        }
        System.out.println(cad);
    }
    public static double evaluador(String [] postfix){
        Pila pila=new Pila();
        double temp1,temp2,res=0;
        for(String token: postfix){
            if(precedencia.containsKey(token)){
                temp1=Double.parseDouble(pila.pop());
                temp2=Double.parseDouble(pila.pop());
                switch (token){
                    case "+":
                        res=temp2+temp1;
                        break;
                    case "-":
                        res=temp2-temp1;
                        break;
                    case "*":
                        res=temp2*temp1;
                        break;
                    case "/":
                        res=temp2/temp1;
                        break;
                    case "**":
                        res=Math.pow(temp2,temp1);
                        break;
                }
                pila.push(""+res);
            }else{
                pila.push(token);
            }
        }
        return Double.parseDouble(pila.pop());
    }
}

class Pila{
    private Item tope;
    public Pila(){
        tope=null;
    }
    public Pila(String dato){
        tope=new Item(dato);
        tope.setSiguiente(null);
    }
    public void push(String data){
        Item temp=new Item(data);
        if(tope!=null){
            temp.setSiguiente(tope);
            tope=temp;
        }else
            tope=temp;
    }
    public String pop(){
        if(tope!=null){
            String data=tope.getDato();
            Item temp = tope.getSiguiente();
            tope.setSiguiente(null);
            tope=temp;
            return data;
        }else{
            return null;
        }
    }
    public String peek(){
        if(tope==null)
            return null;
        else
            return tope.getDato();
    }
    public boolean isEmpty(){
        if(tope==null)
            return true;
        else
            return false;
    }
    public void print(){
        Item temp=tope;
        if(temp==null)
            System.out.println("...");
        else{
            while(temp!=null){
                System.out.println(temp.getDato());
                temp=temp.getSiguiente();
            }
        }
    }
}

class Cola{
    private Item inicio;
    public Cola(){
        inicio=null;
    }
    public Cola(String dato){
        inicio=new Item(dato);
        inicio.setSiguiente(null);
    }
    public void enqueue(String dato){
        Item temp= new Item(dato);
        if(inicio!=null){
            Item aux=inicio;
            while(aux.getSiguiente()!=null){aux=aux.getSiguiente();}
            aux.setSiguiente(temp);
        }else{
            inicio=temp;
        }
    }
    public String dequeue(){
        if(inicio!=null){
            Item temp=inicio;
            inicio=temp.getSiguiente();
            temp.setSiguiente(null);
            return temp.getDato();
        }else{
            return null;
        }
    }
    public void print(){
        Item temp=inicio;
        if(temp==null){
            System.out.println("...");
        }else{
            do{
                System.out.println(temp.getDato());
                temp=temp.getSiguiente();
            }while(temp!=null);
        }
    }
    public int length(){
        Item temp=inicio;
        int i=0;
        while(temp!=null){
            i++;
            temp=temp.getSiguiente();
        }
        return i;
    }
}

class Item{
    private String dato;
    private Item siguiente;
    public Item(String dato){
        this.dato=dato;
    }
    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public Item getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Item siguiente) {
        this.siguiente = siguiente;
    }
}
