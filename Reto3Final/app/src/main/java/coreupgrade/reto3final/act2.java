package coreupgrade.reto3final;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import coreupgrade.reto3final.entity.PokemonEntity;
import coreupgrade.reto3final.service.ApiService;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class act2 extends AppCompatActivity {
    String avatar;
    String usuario;
    TextView usuari;
    ImageView img;
    private ListView list;

    private Bitmap DownloadImage (String imageHttpAddress){
        URL imageUrl = null;
        Bitmap imagen = null;
        try{
            imageUrl = new URL(imageHttpAddress);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            imagen = BitmapFactory.decodeStream(conn.getInputStream());
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return imagen;
    }

    class DownloadFileFromURL extends AsyncTask<String, Void, Bitmap> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(act2.this);
            pDialog.setMessage("Cargando Imágenes...");
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground ", "Entra en doInBackground");
            String url = params[0];
            Bitmap imagen = DownloadImage(url);
            return imagen;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            img.setImageBitmap(result);
            pDialog.dismiss();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        usuario = getIntent().getStringExtra("nombre");
        avatar = getIntent().getStringExtra("image");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act2);
        usuari = (TextView) findViewById(R.id.txtBienv);
        usuari.setText("Bienvenido "+usuario);
        img = (ImageView) findViewById(R.id.img);
        new DownloadFileFromURL().execute(avatar);
        list = (ListView)findViewById(R.id.lista_poke);

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://victorcasass.com/api/").build();

        ApiService servide = restAdapter.create(ApiService.class);
        servide.getPokemons(new Callback<ArrayList<PokemonEntity>>() {
            @Override
            public void success(ArrayList<PokemonEntity> pokemonEntities, Response response) {
                AdaptadorPoke adapter = new AdaptadorPoke(act2.this, pokemonEntities);
                list.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

    private class AdaptadorPoke extends ArrayAdapter<PokemonEntity>{
        private ArrayList<PokemonEntity> listapokemons;

        public AdaptadorPoke(Context context, ArrayList<PokemonEntity> pokemons){
            super(context, R.layout.list_item,pokemons);
            listapokemons = pokemons;
        }
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = LayoutInflater.from(getContext());

            View item = inflater.inflate(R.layout.list_item, null);

            TextView txtTitulo = (TextView)item.findViewById(R.id.txtTitulo);
            txtTitulo.setText(listapokemons.get(position).getNombre());

            TextView txtTipo = (TextView)item.findViewById(R.id.txtTipo);
            txtTipo.setText(listapokemons.get(position).getTipo());

            ImageView imageen = (ImageView) item.findViewById(R.id.imgD);
            String param = listapokemons.get(position).getImagen();
            new DownloadImageTask(imageen).execute(param);

            return item;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String links = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(links).openStream();
                Log.i("url", links);
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            bmImage.setImageBitmap(result);
        }
    }
}

