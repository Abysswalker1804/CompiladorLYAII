package org.example.lyaii.Tools;

public class Cola{
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
