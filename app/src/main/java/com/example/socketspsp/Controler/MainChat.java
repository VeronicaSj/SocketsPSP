package com.example.socketspsp.Controler;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socketspsp.R;

import java.util.ArrayList;

import Connection.Client;
import Connection.Server;
import Model.ListaMensajes;
import Model.Mensaje;

public class MainChat extends AppCompatActivity {
    private TextView tvRecepNick;
    private TextView tvMsgList;
    private Button bGoBack;
    private EditText etTextBox;
    private Button bSend;
    private static AsyncTask hilo;

    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        tvRecepNick = (TextView) findViewById(R.id.tvReceptingNick);
        tvMsgList = (TextView) findViewById(R.id.tvMsgList);
        bGoBack = (Button) findViewById(R.id.bGoBack);
        etTextBox = (EditText) findViewById(R.id.etTextBox);
        bSend = (Button) findViewById(R.id.bSend);

        Intent i = getIntent();
        String receptorNick = i.getStringArrayExtra("CHAT")[0];

        tvMsgList.setText(getAllMsgs());

        //hilo que actualiza el text view de los mensajes
        seeActualicedMensages(tvMsgList);

        //inicializamos el cliente el servidor
        client=new Client(i.getStringArrayExtra("CHAT")[1],i.getStringArrayExtra("CHAT")[2]);

        tvRecepNick.setText(receptorNick);

        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.enviarMensaje(etTextBox.getText().toString());
                etTextBox.setText("");
            }
        });

        bGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = createAlertDialog("INFO!",
                        "Si sale se borrará la conversacion");
                alertDialog.show();
            }
        });

        //en mi movil, la aplicacion no encuentra el toolBar, pero lo dejo hecho por si en otros movile funciona
        ActionBar actionBar = getActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }else{
            Toast.makeText(this,"Si sale se borrará la conversacion", Toast.LENGTH_SHORT).show();
        }
    }

    private String getAllMsgs(){
        String msgs = "";
        ArrayList<Mensaje> listaMensajes = ListaMensajes.getLista();
        for (Mensaje msg : listaMensajes) {
            msgs = msgs + msg.toString() + "\n";
        }
        return msgs;
    }


    private void seeActualicedMensages(TextView tvMsgList){
        hilo= new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {
                while(true) {
                    //luego los ponemos en el textView
                        tvMsgList.setText(getAllMsgs());
                }
            }
        };
        hilo.execute();
    }
    public static void stopUpdatingMensageView(){
        hilo.cancel(true);
    }

    //funcion para construir un AlertDialog
    private AlertDialog createAlertDialog(String titulo, String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainChat.this);
        builder.setMessage(mensaje)
                .setTitle(titulo);

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        return builder.create();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                AlertDialog alertDialog = createAlertDialog("INFO!",
                        "Si sale se borrará la conversacion");
                alertDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
