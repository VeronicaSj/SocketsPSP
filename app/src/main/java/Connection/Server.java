package Connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;

import Model.ListaMensajes;
import Model.Mensaje;

public class Server{
    private Thread serverThread;

    public Server(){
        serverThread= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket servidor = new ServerSocket(1234);
                    while(true){
                        Socket misocket = servidor.accept();
                        //este try es para que si ocurre alguna excepcion, no se cierre el bucle
                        try {
                            ObjectInputStream paqueteDatos = new ObjectInputStream(misocket.getInputStream());
                            //Mi servidor llega hasta aquí
                            //en la siguiente linea hay un error que no soy capaz de solucionar
                            Mensaje mensajeRecibido = (Mensaje) paqueteDatos.readObject();
                            ListaMensajes.addSynchronized(mensajeRecibido);
                            paqueteDatos.close();
                        }catch(IOException ex){
                            ListaMensajes.addSynchronized(new Mensaje("","Te han intentado mandar un mensaje"));
                        }
                        //cerramos el socket
                        misocket.close();
                    }
                } catch (IOException ex) {
                    ListaMensajes.addSynchronized(new Mensaje("","Te han intentado mandar un mensaje"));
                } catch ( ClassNotFoundException ex) {
                    ListaMensajes.addSynchronized(new Mensaje("","Te han intentado mandar un mensaje"));
                }
            }
        });
        serverThread.start();
    }

    public void killServer(){
        serverThread.interrupt();
    }
}
