package org.campitos.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by campitos on 5/28/15.
 */
public class GCMServicioIntent extends IntentService {
    static final String TAG="MensageriaGGCM";
    String mensaje;
    String titulo;
    private Handler handler;
    Bundle extras;
    public static final int NOTTIFICACION_ID=1;
    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    public GCMServicioIntent() {
      super("MensajeriaGCM");
    }
    @Override
    public void onCreate(){
        super.onCreate();
        handler=new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        extras=intent.getExtras();
        GoogleCloudMessaging gcm=GoogleCloudMessaging.getInstance(this);
        //El parametro intent del  getMessageType() debe ser el intent que recibiste en el GCMReceptorBroadcast
        String tipoMensaje=gcm.getMessageType(intent);
        titulo=extras.getString("title");
        mensaje=" " +extras.getString("message");
        titulo=titulo+mensaje;
        mostrarToast();
        Log.i("GCM", "Received : (" + tipoMensaje + ")  " + extras.getString("title"));
        GCMReceptorBroadcast.completeWakefulIntent(intent);
    }
    public void mostrarToast(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), titulo, Toast.LENGTH_LONG).show();
                Vibrator vibrador = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                mandarNotificacion(extras.getString("message"));
                vibrador.vibrate(1000);
            }
        });
    }
    private void mandarNotificacion(String msg){
        notificationManager=(NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.mensito)
                .setContentTitle("Nuevo Mensajito")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);
        builder.setContentIntent(pendingIntent);
        notificationManager.notify(NOTTIFICACION_ID,builder.build());
    }

}
