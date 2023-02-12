package Connection;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.ParseException;

import Model.ListaMensajes;
import Model.Mensaje;

public class Client /*extends AsyncTask implements Runnable*/  {
    private String MyIp;
    private String ReceptorIp;

    public Client(String MyIp, String ReceptorIp){
        this.MyIp = MyIp;
        this.ReceptorIp = ReceptorIp;
    }

    public void enviarMensaje(String texto){
        Thread hiloCliente=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Mensaje mensage = new Mensaje(MyIp,texto);
                    ListaMensajes.addSynchronized(mensage);
                    Socket misocket = new Socket(ReceptorIp, 1234);
                    ObjectOutputStream paqueteDatos = new ObjectOutputStream(misocket.getOutputStream());
                    paqueteDatos.writeObject(mensage);
                    paqueteDatos.close();
                    misocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        hiloCliente.start();
    }
}
