package com.example.socketspsp.Controler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socketspsp.R;

import Connection.Client;
import Connection.Server;
import Model.ListaMensajes;

public class MainActivity extends AppCompatActivity {
    TextView tvMyIp;
    Button bStartTalking;
    EditText etMyNick;
    EditText etReceptorNick;
    EditText etReceptorIp;

    private Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //conectamos la vista y el controlador
        tvMyIp = (TextView) findViewById(R.id.tvIpActual);
        bStartTalking = (Button) findViewById(R.id.buttonStartTalking);
        etMyNick = (EditText) findViewById(R.id.etMyNick);
        etReceptorNick = (EditText) findViewById(R.id.etNickReceptor);
        etReceptorIp = (EditText) findViewById(R.id.etIpReceptor);

        //obtenemos la ip del cliente y la mostramos
        String myIp = getMyIp() ;
        tvMyIp.setText("Mi ip actual es: " + myIp);

        bStartTalking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obtenemos los datos introduccidos por el usuario
                //el nick propio por ahora no tiene uso
                String myNick = etMyNick.getText().toString() ;
                String receptorNick = etReceptorNick.getText().toString() ;
                String receptorIp = etReceptorIp.getText().toString() ;

                //si los datos introducidos son validos,
                // iniciamos el chat y le pasamos los datos
                if(validateData(myNick,receptorNick,myIp,receptorIp)){
                    String[] argsArray = {receptorNick,myIp,receptorIp};
                    Intent i = new Intent(MainActivity.this,MainChat.class);
                    i.putExtra("CHAT", argsArray);
                    startActivity(i);

                    server=new Server();

                }else{
                    //si los datos introucidos no fueran validos, informamos al usuario
                    AlertDialog alertDialog = createAlertDialog("ERROR",
                            "Los datos introducidos son incorrectos");
                    alertDialog.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //cuando salgamos del chat, tendremos que reiniciar el chat
        super.onActivityResult(requestCode, resultCode, data);
        ListaMensajes.restartList();
        server.killServer();
        MainChat.stopUpdatingMensageView();
    }

    //funcion para obtener la ip del propio dipositivo
    private String getMyIp(){
        String ip_texto="";
        try{
            Context context = this.getApplicationContext();
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            ip_texto = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        }catch(Exception ex){
            ip_texto= "error";
        }
        return ip_texto;
    }

    //funcion para validar los datos de conexion dados por el usuario
    private boolean validateData(String myNick,String otherNick,String myIp,String otherIp){
        boolean res = false;
        //Ningun dato puede ser null
        if(myNick != null && otherNick != null && myIp != null && otherIp != null){
            if(myNick.length()>0 && otherNick.length()>0) {
                //las ip validas tienen que tener una longitud de entre 7 y 15 caracteres
                if (myIp.length() >= 7 && otherIp.length() >= 7 && myIp.length() <= 15 && otherIp.length() <= 15) {
                    /* las ip validas tienen que tener 4 grupos de entre 1 y 3 caracteres numericos.
                     los grupos tienen que estar separados por "." */

                    //el siguiente codigo es el mas apropiado, pero no me funciona

                    /*String[] myIpSplited = myIp.split(".");
                    String[] otherIpSplited = otherIp.split(".");
                    Toast.makeText(this,myIpSplited.length+"llegamos"+otherIpSplited.length,Toast.LENGTH_LONG).show();
                    if(myIpSplited.length==4 && otherIpSplited.length==4) {
                        res = true;
                    }*/

                    res = true;
                }
            }
        }
        return res;
    }

    //funcion para construir un AlertDialog
    private AlertDialog createAlertDialog(String titulo, String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(mensaje)
                .setTitle(titulo);

        //no hay boton, de confirmacion, solo se pulsa fuera de la notificacion para cerrar

        return builder.create();
    }

}