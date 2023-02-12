package Model;

import java.util.ArrayList;

public class ListaMensajes extends ArrayList<Mensaje>{
    //esta clase nos hace la funcion de guardar los datos mientras se ejecuta la aplicacion.
    //esta clase simularia el acceso a algun fichero o a alguna base de datos en un chat en el
    //que hubiera persistencia

    private static ArrayList<Mensaje> lista=new ArrayList<Mensaje>();

    //esta funcion nos permite asegurarnos que no va a haber ningun conflicto entre los hilos
    synchronized public static void addSynchronized(Mensaje msg){
        lista.add(msg);
    }

    public static ArrayList getLista(){
        return lista;
    }

    public static void restartList(){
        lista.clear();
    }
}
