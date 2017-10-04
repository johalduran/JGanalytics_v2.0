package com.johnduran.jganalytics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;


public class PerfilActivity extends AppCompatActivity {
    private String correoR, contrasenaR, correoL, contrasenaL;
    private TextView tUsuario;
    private String NombrePreferencias="Mis_Preferencias";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private int optLog=0;
    private String RegPhoto, noAvailable = "No Available",nombreGoogle,correoGoogle,nombreFacebook,correoFacebook;
    private ImageView fotoPerfil;
    GoogleApiClient mGoogleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        tUsuario= (TextView) findViewById(R.id.tUsuario);
        prefs=getSharedPreferences(NombrePreferencias, Context.MODE_PRIVATE);
        optLog= prefs.getInt("optLog",0);
        RegPhoto=prefs.getString("foto",noAvailable);
        nombreGoogle= prefs.getString("nombreCorreo","");
        correoGoogle= prefs.getString("correoGoogle","");
        nombreFacebook= prefs.getString("nameFacebook","");
        correoFacebook= prefs.getString("emailFacebook","");



        Bundle extras = getIntent().getExtras();
        fotoPerfil = (ImageView)findViewById(R.id.fotoPerfil);

        cargarImagendeURL(RegPhoto,fotoPerfil); //Carga la url de la foto segun el tipo de login
        if(optLog==3){ //  Para login con google
            tUsuario.setText("\n"+nombreGoogle+"\n"+correoGoogle+"\n");
        }else if(optLog==2){
            tUsuario.setText("\n"+nombreFacebook+"\n"+correoFacebook);
        }
        //____________________________________google___________________________
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
        //____________________________________________________________

        if (extras!=null){ //Si se inicia con Correo, se obtienen estos campos
            correoR = extras.getString("correo");
            contrasenaR = extras.getString("contrasena");
            correoL = extras.getString("correoL");
            contrasenaL = extras.getString("contrasenaL");
            tUsuario.setText(correoR);
        }
        /*
        ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.userProfilePicture);
        profilePictureView.setCropped(true);
        profilePictureView.setProfileId(user.getId());
        */
    }

    private void cargarImagendeURL(String url, ImageView imageView) {
        Picasso.with(this).load(url).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView,new com.squareup.picasso.Callback(){
                    @Override
                    public void onSuccess(){}
                    @Override
                    public void onError(){}
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu2) {
        getMenuInflater().inflate(R.menu.menu2, menu2);
        return super.onCreateOptionsMenu(menu2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent, intent1,intent2;

        switch(id){
            case R.id.mPrincipal:
                if(optLog==1){
                    intent1 = new Intent(PerfilActivity.this, MainActivity.class);
                    intent1.putExtra("correo",correoR);
                    intent1.putExtra("contrasena",contrasenaR);
                    intent1.putExtra("correoL",correoL);
                    intent1.putExtra("contrasenaL",contrasenaL);
                    startActivity(intent1);
                    finish();
                }else if(optLog==2 || optLog==3){
                    intent2 =  new Intent(PerfilActivity.this, MainActivity.class);
                    startActivity(intent2);
                    finish();
                }

                break;
            case R.id.mCerrar:
                if (optLog==1){
                    intent =  new Intent(PerfilActivity.this, LoginActivity.class);
                    intent.putExtra("correo",correoR);
                    intent.putExtra("contrasena",contrasenaR);
                    intent.putExtra("correoL",correoL);
                    intent.putExtra("contrasenaL",contrasenaL);
                    setResult(RESULT_OK, intent);
                    finish();
                }else if(optLog==2){
                    LoginManager.getInstance().logOut(); //Cierra sesión de facebook
                    intent2 =  new Intent(PerfilActivity.this, LoginActivity.class);
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
                    intent2 =  new Intent(PerfilActivity.this, LoginActivity.class);
                    startActivity(intent2);
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
