package org.example.lyaii.Tools;

public class Pila{
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
    public int size(){
        int i=0;
        Item aux=tope;
        while(aux!=null){aux=aux.getSiguiente(); i++;}
        return i;
    }
}
