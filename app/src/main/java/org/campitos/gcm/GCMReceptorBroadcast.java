package org.campitos.gcm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by campitos on 5/28/15.
 */
public class GCMReceptorBroadcast extends WakefulBroadcastReceiver {
    @Override

    public void onReceive(Context context, Intent intent) {
  //  Explicitamente especifica que el GCMServicioIntent manegara el Intent
        ComponentName componentName=new ComponentName(context.getPackageName(),GCMServicioIntent.class.getName());
        //Inicializa el servicio, manteniendo el dispositivo despierto mientras esta lanzandose
        startWakefulService(context,(intent.setComponent(componentName)));
    }
}
