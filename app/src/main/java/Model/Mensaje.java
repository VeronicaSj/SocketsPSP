package Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class Mensaje {
    private String authorIp;
    private String msg;
    private String date;

    //constructor
    public Mensaje(String authorIp,String msg) {
        this.authorIp = authorIp ;
        this.msg = msg ;

        //la fecha que se utiliza es la fecha de cuando se crea el mensaje
        Date currentTime = Calendar.getInstance().getTime();
        date=getSysDate();
    }

    //toString
    @Override
    public String toString(){
        return (msg + "\n\t\t\t\tFecha:"+date);
    }

    public String getAuthorIp(){return authorIp;}

    public String getText(){return msg;}

    public String getDate(){return date;}

    private String getSysDate(){
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime.toString();
    }

}
