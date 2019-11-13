package com.example.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Cadastro extends AppCompatActivity {

    private EditText edtEmail, edtSenha, edtConfirmarSenha;

    private Button btnCadastrar, btnVoltar;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    private FirebaseDatabase database;
    private DatabaseReference usuarioReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        database = FirebaseDatabase.getInstance();
        usuarioReference= database.getReference("usuarios");

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        edtEmail = findViewById(R.id.txtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtConfirmarSenha = findViewById(R.id.edtConfirmaSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnVoltar = findViewById(R.id.btnVoltar);

        final Spinner spinnerIdiomas = (Spinner) findViewById(R.id.spinnerIdioma);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languageArray, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerIdiomas.setAdapter(adapter);

        spinnerIdiomas.setOnItemSelectedListener(new  AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {
                String selectedItemText = (String) spinnerIdiomas.getSelectedItem();

                if (!selectedItemText.equals("Idioma")) {
                    if (!selectedItemText.equals("Português")) {
                        //salvar linguagem com cadastro do usuario

                    }
                    else if(!selectedItemText.equals("Criole")){
                        //salvar linguagem com cadastro do usuario

                    }

                }
            }
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarUsuario();
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);

            }
        });

    }

    private void cadastrarUsuario() {

        final String email = edtEmail.getText().toString().trim();
        String senha = edtSenha.getText().toString().trim();
        String confirmarSenha = edtConfirmarSenha.getText().toString().trim();

        if (email.equals("")) {
            Toast.makeText(getApplicationContext(), "Campo 'Email' é obrigatório", Toast.LENGTH_LONG).show();
            return;
        }

        if (senha.equals("")) {
            Toast.makeText(getApplicationContext(), "Campo 'senha' é obrigatório", Toast.LENGTH_LONG).show();
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            Toast.makeText(getApplicationContext(), "As senhas estão diferentes", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Cadastrando conta...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressDialog.dismiss();

                if (task.isSuccessful()) {

                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    Usuario usuario = new Usuario();
                    usuario.setAdmin(false);
                    usuario.setEmail(email);
                    usuario.setId(currentFirebaseUser.getUid());

                    usuarioReference.child(currentFirebaseUser.getUid()).setValue(usuario);

                    finish();
                    Toast.makeText(getApplicationContext(), "Cadastro efetuado com sucesso!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), Categorias.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), "Ocorreu um erro ao criar a conta, tente novamente", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
