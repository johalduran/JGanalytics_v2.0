package com.johnduran.jganalytics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


public class MainActivity extends AppCompatActivity {

    private String correoR, contrasenaR,correoL,contrasenaL;
    private String NombrePreferencias="Mis_Preferencias";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private int optLog=0;
    GoogleApiClient mGoogleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs=getSharedPreferences(NombrePreferencias, Context.MODE_PRIVATE);
        optLog= prefs.getInt("optLog",0);
        System.out.println("VALOR DE OPTLOG:"+optLog);

        //___________________________google__________________________
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.default_web_client_id)) // Cuando se conecte a un servidor
                .requestProfile()
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(),"Error en login", Toast.LENGTH_LONG).show();
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //________________________google_____________________________________-

        Bundle extras = getIntent().getExtras();
        if (extras!=null){ //Si se inicia con Correo, se obtienen estos campos
            correoR = extras.getString("correo");
            contrasenaR = extras.getString("contrasena");
            correoL = extras.getString("correoL");
            contrasenaL = extras.getString("contrasenaL");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent, intent1, intent2;

        switch(id){
            case R.id.mPerfil:
                if(optLog==1){ //Para facebook
                    intent1 = new Intent(MainActivity.this, PerfilActivity.class);
                    intent1.putExtra("correo",correoR);
                    intent1.putExtra("contrasena",contrasenaR);
                    intent1.putExtra("correoL",correoL);
                    intent1.putExtra("contrasenal",contrasenaL);
                    startActivityForResult(intent1,9876);
                    finish();
                }else if(optLog==2 || optLog==3){
                    intent2 =  new Intent(MainActivity.this, PerfilActivity.class);
                    startActivity(intent2);
                    finish();
                }

                break;
            case R.id.mCerrar:
                if(optLog==1){ //Para facebook
                    LoginManager.getInstance().logOut();
                    intent =  new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra("correo",correoR);
                    intent.putExtra("contrasena",contrasenaR);
                    intent.putExtra("correoL",correoL);
                    intent.putExtra("contrasenal",contrasenaL);
                    setResult(RESULT_OK, intent);
                    finish();
                }else if(optLog==2){
                    LoginManager.getInstance().logOut(); // Logout con facebook
                    intent2 =  new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent2);
                    finish();
                }else if(optLog==3){
                    //__________________logout con google_______________

                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    // ...
                                }
                            });
                    //________________________________________________________
                    intent2 =  new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent2);
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
