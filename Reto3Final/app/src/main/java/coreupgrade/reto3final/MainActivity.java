package coreupgrade.reto3final;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import coreupgrade.reto3final.entity.PokemonEntity;
import coreupgrade.reto3final.service.ApiImplementation;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    String usuario;
    String pass;
    boolean encontrado;
    Intent in;
    ArrayList<PokemonEntity> listP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        in = new Intent(MainActivity.this, act2.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button boton = (Button) findViewById(R.id.Ingresar);

        //CONSUMO DE DATOS
        ApiImplementation.getService().getPokemons(new Callback<ArrayList<PokemonEntity>>() {
            @Override
            public void success(ArrayList<PokemonEntity> lista, Response response) {
                for (PokemonEntity p : lista) {
                    Log.i("Respuesta: ", p.getNombre());
                    Log.i("Respuesta: ", p.getTipo());
                    Log.i("Respuesta: ", p.getImagen());
                }
                listP = lista;
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.i("Message", "Error");
            }
        });


        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = ((EditText) findViewById(R.id.correo)).getText().toString();
                pass = ((EditText) findViewById(R.id.pass)).getText().toString();
                TextView text = (TextView) findViewById(R.id.aviso);
                if (usuario.equals("")) {
                    text.setText("INGRESE UN USUARIO");
                } else {
                    if (pass.equals("")) {
                        text.setText("INGRESE EL TIPO");
                    }
                    if (!pass.equals("")) {
                        for (int i = 0; i < listP.size(); i++) {
                            if(usuario.equals(listP.get(i).getNombre()) && pass.equals(listP.get(i).getTipo())){
                                in.putExtra("nombre", listP.get(i).getNombre());
                                String image = listP.get(i).getImagen();
                                in.putExtra("image",image);
                                encontrado = true;
                            }
                        }
                        if(encontrado){
                            startActivity(in);
                        }
                        else {
                            errorM(view);
                        }
                    }
                }
            }
        });
    }
    public void errorM(View v) {
        Toast.makeText(this,"Pokémon no registrado o error de contraseña",Toast.LENGTH_SHORT).show();
    }

}
