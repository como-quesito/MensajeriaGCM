package org.campitos.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.HashMap;
import java.util.Map;

/*
API KEY: AIzaSyCMVsw6hAsGYV0rlNgRWpjXat75qXX_4iA
 */


public class MainActivity extends ActionBarActivity {
    /*
    El siguiente es el numero de proyecto se requiere en el registro y se encuentra enla apli console
    cuando te metes a tu pagina inicial alli viene asi tal cual, como numero de proyecto
     */
    String SENDER_id="964281068865";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    static final String TAG = "GCMDemo";
    GoogleCloudMessaging gcm;
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    String registroId;
    Context ctx;
    TextView textoRegistro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean servicio =checarServiciosGoogle();
        if(servicio){
            //Toast.makeText(this, "Si tienes google play", Toast.LENGTH_LONG).show();
            gcm=GoogleCloudMessaging.getInstance(this);
            //Inicializamos el conetxto para que lo vean todos
            ctx=getApplicationContext();

            registroId=getRegistroId(ctx);
            if(registroId.isEmpty()){
              //  Toast.makeText(this,"No te has registrado todavia", Toast.LENGTH_LONG).show();
                registrarEnBackground();
            }
            else{
                Log.i(TAG, "APK de Google Play Services  no encontrada!!!");
            }

        }
    }
    /*******************************************************************
    Método para obtener el id de registro
     ******************************************************************/
    private String getRegistroId(Context ctx){
     final SharedPreferences sharedPreferences =getGcmPreferences(ctx);
        String registroId= sharedPreferences.getString(PROPERTY_REG_ID,"");
        if(registroId.isEmpty()){
            Log.i(TAG, "registro no encontardo");
            return "";
        }
        /*
        Checamos si la aplicacion ha sido actualizada, si es asi, se limpiara el
        registroid, por tantoi no se garantiza que la nueva app funcione con el anterior
         */
        int versionRegistrada= sharedPreferences.getInt(PROPERTY_APP_VERSION,Integer.MIN_VALUE);
        int versionActual=getAppVersion(ctx);
        if(versionRegistrada!=versionActual){
            Log.i(TAG, "La version registrada ha cambadio");
            return "";
        }
        return registroId;
    }

    //EL SIGUIENTE METODO NOS DA LA VERSION ACTUAL DEL REGISTRO DE GCM
    private static int getAppVersion(Context ctx){
        try{
            PackageInfo packageInfo=ctx.getPackageManager()
                    .getPackageInfo(ctx.getPackageName(),0);
            return packageInfo.versionCode;
        }catch(Exception e){
           //Nunca debe de ocurrir
            throw new RuntimeException("Could not get package name: " + e);
        }

    }
// EL REGISTRO SE GUARDA EN SharedPreferences
    private SharedPreferences getGcmPreferences(Context ctx){
        return getSharedPreferences(MainActivity.class.getSimpleName(),Context.MODE_PRIVATE);
    }
    /***********************************************************************
    EL METODO registrarEnBackground, es el corazón del registro
     **********************************************************************/

    private  void registrarEnBackground(){
        textoRegistro= (TextView) findViewById(R.id.textoRegistro);
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String msg="";
                try{
                    if(gcm==null){
                        gcm= GoogleCloudMessaging.getInstance(ctx);
                    }
                    //Este simple método realiza el registro!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    registroId=gcm.register(SENDER_id);
                    msg="Dispositivo registrado con id="+registroId;
                    /******************************************************************************************
                    Una vez obtenido el registro debemos transferirlo a través de http para que se guarde en el servidor
                     **************************************************************************************************/
                    String servidorulr="http://campitos-ley.whelastic.net/uv/servicios/celulares/registrar-mensajeria";
                    Map<String, String> paramos=new HashMap<String, String>();


                }catch(Exception e){

                }

                return msg;
            }
        }.execute(null,null,null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean checarServiciosGoogle() {
        int codigoResultado = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (codigoResultado != ConnectionResult.SUCCESS) {
            /*
            on el siguiente aparece un mensajito muy boito de que no tienes google play
             */
            if (GooglePlayServicesUtil.isUserRecoverableError(codigoResultado)) {
                GooglePlayServicesUtil.getErrorDialog(codigoResultado, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
