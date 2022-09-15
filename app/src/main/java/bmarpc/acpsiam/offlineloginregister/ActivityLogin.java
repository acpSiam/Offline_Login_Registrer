package bmarpc.acpsiam.offlineloginregister;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ActivityLogin extends AppCompatActivity {

    MyAnim myAnim;
    DatabaseHelper databaseHelper;


    MaterialCardView materialCardView;
    TextInputLayout usernameLayout, passwordLayout;
    TextInputEditText usernameEditText, passwordEditText;
    ExtendedFloatingActionButton loginButton;
    MaterialButton signupButton;


    String USERNAME, PASSWORD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Initializing Variables
        databaseHelper = new DatabaseHelper(ActivityLogin.this);
        myAnim = new MyAnim();


        //*Finding IDs
        materialCardView = findViewById(R.id.login_cardview_id);
        usernameLayout = findViewById(R.id.login_user_name_text_layout_id);
        passwordLayout = findViewById(R.id.login_password_text_layout_id);
        usernameEditText = findViewById(R.id.login_user_name_et_id);
        passwordEditText = findViewById(R.id.login_password_et_id);
        loginButton = findViewById(R.id.login_button_id);
        signupButton = findViewById(R.id.sign_up_new_account_button_id);


        if (loginSaved()) {
            startActivity(new Intent(ActivityLogin.this, ActivityHome.class));
        }


        //Animating CardView
        myAnim.popOpenBothSide(materialCardView, 400L);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Getting data from user and initializing them to variable
                USERNAME = usernameEditText.getText().toString().trim();
                PASSWORD = passwordEditText.getText().toString().trim();


                //Getting response from database helper to check the login auth
                //This returns TRUE if username and password matches
                //Or returns FALSE if username or password is incorrect
                boolean success = databaseHelper.loginAuth(USERNAME, PASSWORD);


                if (success) {
                    new MaterialAlertDialogBuilder(ActivityLogin.this)
                            .setTitle("Keep you logged in?")
                            .setIcon(R.drawable.ic_round_remember_me_24)
                            .setMessage("Save your login informations saved so that you don't need to login again. You'll always have an option to log out")
                            .setCancelable(true)
                            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    saveLogin(USERNAME, PASSWORD);
                                    startActivity(new Intent(ActivityLogin.this, ActivityHome.class));
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(ActivityLogin.this, ActivityHome.class));
                                }
                            })
                            .show();
                }

                else {
                    Toast.makeText(ActivityLogin.this, "Fail", Toast.LENGTH_SHORT).show();
                }
            }
        });


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityLogin.this, ActivitySignUp.class));
            }
        });


    }


    public void saveLogin(String uname, String pass) {
        SharedPreferences sharedPreferences = ActivityLogin.this.getSharedPreferences(getString(R.string.LOGIN_SP_STR), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(getString(R.string.USER_NAME_STR), uname);
        editor.putString(getString(R.string.PASSWORD_STR), pass);

        editor.apply();
    }


    public boolean loginSaved() {
        boolean saved = false;

        SharedPreferences sharedPreferences = ActivityLogin.this.getSharedPreferences(getString(R.string.LOGIN_SP_STR), MODE_PRIVATE);
        USERNAME = sharedPreferences.getString(getString(R.string.USER_NAME_STR), "");
        PASSWORD = sharedPreferences.getString(getString(R.string.PASSWORD_STR), "");
        saved = databaseHelper.loginAuth(USERNAME, PASSWORD);

        Toast.makeText(this, saved + "", Toast.LENGTH_SHORT).show();

        return saved;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //So that it ends the app
        this.finishAffinity();
    }
}