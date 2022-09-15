package bmarpc.acpsiam.offlineloginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ActivitySignUp extends AppCompatActivity {

    MyAnim myAnim;
    DatabaseHelper databaseHelper;

    TextInputLayout firstNameLayout, lastNameLayout, usernameLayout,
            emailLayout, passwordLayout, confirmPasswordLayout;
    TextInputEditText firstNameEditText, lastNameEditText, usernameEditText,
            emailEditText, passwordEditText, confirmPasswordEditText;
    ExtendedFloatingActionButton signUpButton;
    MaterialButton loginButton;
    MaterialCardView cardView;


    String FIRST_NAME, LAST_NAME, FULL_NAME,
            USERNAME, EMAIL, PASSWORD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        //Initializing Variables
        databaseHelper = new DatabaseHelper(ActivitySignUp.this);
        myAnim = new MyAnim();


        //*Finding IDs
        firstNameLayout = findViewById(R.id.sign_up_first_name_text_layout_id);
        lastNameLayout = findViewById(R.id.sign_up_last_name_text_layout_id);
        emailLayout = findViewById(R.id.sign_up_email_text_layout_id);
        passwordLayout = findViewById(R.id.sign_up_password_text_layout_id);
        confirmPasswordLayout = findViewById(R.id.sign_up_confirm_password_text_layout_id);
        usernameLayout = findViewById(R.id.login_user_name_text_layout_id);

        firstNameEditText = findViewById(R.id.sign_up_first_name_et_id);
        lastNameEditText = findViewById(R.id.sign_up_last_name_et_id);
        emailEditText = findViewById(R.id.sign_up_email_et_id);
        passwordEditText = findViewById(R.id.sign_up_password_et_id);
        confirmPasswordEditText = findViewById(R.id.sign_up_confirm_password_et_id);
        usernameEditText = findViewById(R.id.sign_up_username_et_id);

        signUpButton = findViewById(R.id.sign_up__button_id);
        loginButton = findViewById(R.id.login_old_account_button_id);

        cardView = findViewById(R.id.sign_up_cardview_id);



        //Animating CardView
        cardView.setVisibility(View.GONE);
        myAnim.popOpenBothSide(cardView, 400L);



        //This whole code from line 74 to line 109 is responsible for the confirm and re-type password functionality
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 0){                                         //When the field is not empty or user has entered some text
                    myAnim.popOpenBothSide(confirmPasswordLayout, 400L);        //Showing the confirm and re-type password field with an animation
                    confirmPasswordLayout.setError(" ");                               //Setting an error till the user is re-typing and confirming the password
                }

                else {                                                                //When the field is empty or user has not entered anything or removed the texts
                    myAnim.popCloseBothSide(confirmPasswordLayout, 400L);     //Showing the confirm and re-type password field with an animation
                    confirmPasswordLayout.setErrorEnabled(false);                    //Removing an error
                    confirmPasswordEditText.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        confirmPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    if (!passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())){
                        confirmPasswordLayout.setError(getString(R.string.confirm_password_error));
                    } else {
                        confirmPasswordLayout.setErrorEnabled(false);
                    }
                } else {
                    confirmPasswordLayout.setErrorEnabled(false);
                }
            }
        });



        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getCurrentFocus().clearFocus();
                if (confirmPasswordLayout.getError() != null){
                    confirmPasswordEditText.requestFocus();
                    confirmPasswordLayout.setError(getString(R.string.confirm_password_error));
                }

                else {
                    FIRST_NAME = firstNameEditText.getText().toString().trim();
                    LAST_NAME = lastNameEditText.getText().toString().trim();
                    FULL_NAME = FIRST_NAME + " " + LAST_NAME;
                    USERNAME = usernameEditText.getText().toString().trim();
                    EMAIL = emailEditText.getText().toString().trim();
                    PASSWORD = passwordEditText.getText().toString();

                    databaseHelper.addUser(FIRST_NAME, LAST_NAME, USERNAME, EMAIL, PASSWORD);

                    startActivity(new Intent(ActivitySignUp.this, ActivityLogin.class));
                    finish();
                }
            }
        });



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivitySignUp.this, ActivityLogin.class));
            }
        });



    }
}