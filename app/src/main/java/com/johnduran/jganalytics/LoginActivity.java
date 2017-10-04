package com.johnduran.jganalytics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//import com.facebook.FacebookSdk;
//import com.facebook.ap|pevents.AppEventsLogger;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

import static android.R.attr.id;
import static android.R.attr.privateImeOptions;

public class LoginActivity extends AppCompatActivity {

    private String correoR, contrasenaR, correoL, contrasenaL,uriFoto,nombreGoogle,correoGoogle,nombreFacebook,nameFacebook,emailFacebook;
    private EditText eCorreoL, eContrasenaL;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN=2222;
    private String NombrePreferencias="Mis_Preferencias";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private int optLog=0;
    private String urlDefault="http://www.freeiconspng.com/uploads/profile-icon-9.png", UserID="",URLfotoFacebook;

    //___________________________________
    String facebook_id;
    String f_name;
    String m_name;
    String l_name;
    String full_name;
    String profile_image;
    String email_id;
    String gender;
    String id_facebook;
    //______________________



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //-------------------------Login con Google---------------------------------------
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
        mGoogleApiClient.connect();
        super.onStart();
                    // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        //__________________________________________________________________________________
        //______________________________login con Facebook__________________________________
        loginButton= (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        callbackManager= CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                optLog=2;
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object,GraphResponse response) {

                        if (response.getError() != null) {
                            // handle error
                        } else {

                            nameFacebook = object.optString("name");
                            emailFacebook =object.optString("email");
                            prefs=getSharedPreferences(NombrePreferencias, Context.MODE_PRIVATE);
                            editor = prefs.edit();
                            editor.putString("nameFacebook", nameFacebook);
                            editor.putString("emailFacebook", emailFacebook);
                            editor.commit(); //Si no se h
                            //Toast.makeText(getApplicationContext(), "cAPMOSD: "+nombreFacebook, Toast.LENGTH_LONG).show();


                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,email,picture");
                request.setParameters(parameters);
                request.executeAsync();

                UserID=loginResult.getAccessToken().getUserId();
                URLfotoFacebook= "https://graph.facebook.com/" + UserID+ "/picture?type=large";
                goMainActivity();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login Cancelado", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Error en Login", Toast.LENGTH_LONG).show();
            }
        });
        //___________________________________________________________________________________

        //__________________Codigo para obtener el hash___________________________
        /*
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.johnduran.jganalytics",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        */
        //______________________________________________________________________________

        eCorreoL=(EditText) findViewById(R.id.eCorreoL);
        eContrasenaL=(EditText) findViewById(R.id.eContrasenaL);
    }

    private void handleSignInResult(GoogleSignInResult result){
        Log.d("google","handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(getApplicationContext(),"Bienvenid@ "+acct.getDisplayName(), Toast.LENGTH_SHORT).show();
            optLog=3;
            Uri personPhoto = acct.getPhotoUrl();
            nombreGoogle= acct.getDisplayName();
            correoGoogle= acct.getEmail();
            uriFoto=personPhoto.toString();
            goMainActivity();
        } else {
            // Signed out, show unauthenticated UI.

        }
    }
    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void goMainActivity(){

        switch (optLog){
            case 1: // Caso Login con Correo
                prefs=getSharedPreferences(NombrePreferencias, Context.MODE_PRIVATE);
                editor = prefs.edit();
                //almacenamos el valor de optlog
                editor.putInt("optLog", optLog); //Almacena una variable con identificador optlog y valor por defecto optlog
                editor.putString("foto",urlDefault);
                editor.commit(); //Si no se hace commit, los cambios no son salvados
                correoL=eCorreoL.getText().toString();
                contrasenaL=eContrasenaL.getText().toString();
                if(contrasenaL.isEmpty() || correoL.isEmpty()){
                    Toast.makeText(this, "Faltan campos por llenar", Toast.LENGTH_SHORT).show();
                }else{
                    if(contrasenaL.equals(contrasenaR) && correoL.equals(correoR)){
                        Intent intent =new Intent (LoginActivity.this, MainActivity.class);
                        intent.putExtra("correo",correoR);
                        intent.putExtra("contrasena",contrasenaR);
                        intent.putExtra("correoL",correoL);
                        intent.putExtra("contrasenaL",contrasenaL);
                        startActivityForResult(intent, 4567);
                    }else{
                        Toast.makeText(this, "Usuario o contraseña inválido", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 2: //Caso Login con Facebook
                prefs=getSharedPreferences(NombrePreferencias, Context.MODE_PRIVATE);
                editor = prefs.edit();
                editor.putString("foto",URLfotoFacebook);
                //Toast.makeText(this, "Usuario:"+nombreFacebook, Toast.LENGTH_SHORT).show();
                //editor.putString("nombreFacebook", nombreFacebook);
                editor.putInt("optLog", optLog);
                editor.commit(); //Si no se hace commit, los cambios no son salvados
                Intent intent2 =new Intent (LoginActivity.this, MainActivity.class);
                startActivity(intent2);
                break;
            case 3: //Caso Loginc on Google
                prefs=getSharedPreferences(NombrePreferencias, Context.MODE_PRIVATE);
                editor = prefs.edit();
                editor.putString("nombreGoogle", nombreGoogle);
                editor.putString("correoGoogle", correoGoogle);
                editor.putString("foto",uriFoto);
                editor.putInt("optLog", optLog);
                editor.commit(); //Si no se hace commit, los cambios no son salvados
                Intent intent3 =new Intent (LoginActivity.this, MainActivity.class);
                startActivity(intent3);
                break;
        }

    }


    public void iniciar(View view) {
        optLog=1;
        goMainActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1234 && resultCode == RESULT_OK){ //Espera resultado del registro
            correoR = data.getExtras().getString("correo");
            contrasenaR = data.getExtras().getString("contrasena");
            Toast.makeText(this, correoR+" ha sido registrado", Toast.LENGTH_SHORT).show();
            Log.d("correo", correoR); //visualizar en el monitor las variables
            Log.d("contrasena",contrasenaR);
        }else if(requestCode == RC_SIGN_IN){ // Respuesta de GOOGLE
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else{
            callbackManager.onActivityResult(requestCode,resultCode,data); //Respuesta de Facebook
        }
        if (requestCode == 4567 && resultCode == RESULT_OK){ //Espera resultado de mainactivity
            correoL = data.getExtras().getString("correoL");
            contrasenaL = data.getExtras().getString("contrasenaL");
            Log.d("correo", correoR); //visualizar en el monitor las variables
            Log.d("contrasena",contrasenaR);
        }
        if (requestCode == 9876 && resultCode == RESULT_OK){//espera resultado de MiPerfil
            correoL = data.getExtras().getString("correoL");
            contrasenaL = data.getExtras().getString("contrasenaL");
            Log.d("correo", correoR); //visualizar en el monitor las variables
            Log.d("contrasena",contrasenaR);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void Registrese(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
        startActivityForResult(intent, 1234);
    }

}
