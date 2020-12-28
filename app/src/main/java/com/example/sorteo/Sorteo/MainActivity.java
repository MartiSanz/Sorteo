package com.example.sorteo.Sorteo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sorteo.Criterio.CriterioArrobados;
import com.example.sorteo.Criterio.CriterioComentariosRepetidos;
import com.example.sorteo.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //creo los objetos
    private TextView Sorteo;
    private TextView CantGanadores, CantPersonas, comentariosRepetidos, ganador, textGanador;
    private Spinner opcionesCantGanadores, opcionesCantPersonasArrobadas, opcionesRepetirComentarios;
    private ListView lista;
    private final int posicion = 0;
    private final String Si= "Si";
    private final String No= "No";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INSTANCIO LOS OBJETOS

        //Instancio los textView
        Sorteo = (TextView) findViewById(R.id.nombrePrincipal);
        CantGanadores = (TextView) findViewById(R.id.textViewCantGanadores);
        CantPersonas = (TextView) findViewById(R.id.textViewCantPersonas);
        comentariosRepetidos = (TextView) findViewById(R.id.textViewComentariosRepetidos);
        ganador = (TextView) findViewById(R.id.textViewGanador);
        textGanador = (TextView) findViewById(R.id.textViewGanador2);

        //Instancio spinner de cantidad de ganadores. R es un fichero donde se guarda el id
        opcionesCantGanadores = (Spinner) findViewById(R.id.spcantGanadores);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.opcionesGanadores, android.R.layout.simple_spinner_item);
        opcionesCantGanadores.setAdapter(adapter1);

        //Instancio spinner de cantidad de personas arrobadas
        opcionesCantPersonasArrobadas = (Spinner) findViewById(R.id.spCantPersonas);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.opcionesPersonas, android.R.layout.simple_spinner_item);
        opcionesCantPersonasArrobadas.setAdapter(adapter2);

        //Instancio spinner booleano
        opcionesRepetirComentarios = (Spinner) findViewById(R.id.spBooleano);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.opcionesBooleanas, android.R.layout.simple_spinner_item);
        opcionesRepetirComentarios.setAdapter(adapter3);

        //Instancio lista
        lista = (ListView) findViewById(R.id.listViewParticipantes);
    }

    // ------------------------------------------------------------------------------------------------------------------------------




    // este metodo es el que se ejecuta cuando apretamos Sortear, debe estar vinculado al boton sino no funciona
    //metodo que carga en una lista, a partir de otra lista con los comentarios validos para sortear, los usuarios que hicieron estos comentarios validos
    // este metodo llama a cargarComentarios
    public void CargaUsuarios(View view) throws IOException {
        ArrayList<String> listaDeNombres = new ArrayList<String>();
        ArrayList<String> listaDeComentarios = new ArrayList<String>();
        listaDeComentarios = this.cargarComentarios();

        String comentario= " ";

        for(int i= 0; i <listaDeComentarios.size(); i++){
            comentario = listaDeComentarios.get(i);
            // cargo en la listaDeNombres las palabras posicionadas en 0, es decir, el nombre de la persona que comento
            listaDeNombres.add(this.getString(comentario, posicion, "@"));
        }

//        Toast.makeText(this, "Carga: " + listaDeNombres.size() , Toast.LENGTH_LONG).show(); // muestro cuantos nombres carga

        //paso del arreglo de arraylist a un arreglo de string
        String datos[] = listaDeNombres.toArray(new String[listaDeNombres.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datos);
        lista.setAdapter(adapter); // seteo en la lista global del main

        sortear(listaDeNombres, listaDeNombres.size());
    }

    //este metodo realiza el sorteo entre los participantes
    public void sortear (ArrayList<String> listaDeNombres, int cantParticipantes){
        int numero = (int)(Math.random()*(cantParticipantes));
        String gana = listaDeNombres.get(numero);
        ganador.setText(gana);
    }

    // este metodo carga en una lista todos los comentarios que reunan los requisitos
    // para cargar la lista lee el archivo data.txt
    public ArrayList<String> cargarComentarios()throws IOException{

        ArrayList<String> listaDeComentarios = new ArrayList<String>();
        String comentario;

        String arrobados = opcionesCantPersonasArrobadas.getSelectedItem().toString(); // obtengo la seleccion del usuario
        int cantArrobados = this.getCantidadDePersonasArrobadas(arrobados); // traduzco esa opcion en un numero
        CriterioArrobados criterioArrobados = new CriterioArrobados(cantArrobados);

        String comentariosRepetidos = opcionesRepetirComentarios.getSelectedItem().toString(); // obtengo la seleccion del usuario
        CriterioComentariosRepetidos criterioComentariosRepetidos= new CriterioComentariosRepetidos();

        InputStream entradaDeData = this.getResources().openRawResource(R.raw.data); // obtengo la base de datos
        BufferedReader reader = new BufferedReader(new InputStreamReader(entradaDeData)); // lo creo para leer

        while((comentario = reader.readLine()) != null) { //mientras la linea leida sea distinto de null
            if (criterioArrobados.cumple(comentario)) { // si el comentario cumple con la cantidad de personas arrobadas que debe tener
                if (criterioComentariosRepetidos.cumple(comentariosRepetidos) == false) { // si no se permiten comentarios repetidos
                    if (!listaDeComentarios.contains(comentario)) { // chequeo que no esté en la lista
                        listaDeComentarios.add(comentario); // y cargo el comentario
                    }
                }
                else {
                    listaDeComentarios.add(comentario); // y cargo el comentario
                }
            }
        }

        entradaDeData.close();

        return listaDeComentarios;
    }

    //este metodo devuelve una palabra especifica de un String
    public String getString(String texto, int posicion, Object division) {
        String div = (java.lang.String) division;
        String retorno = texto.split(div)[posicion];
        return retorno;
    }

    // metodo que traduce la opcion elegida por el usario a numero, es decir, devuelve la cantidad de personas que al menos deben estar arrobados
    public int getCantidadDePersonasArrobadas(String arrobados) {
        String palabra = this.getString(arrobados, 2, " "); // obtengo el numero de la seleccion del usuario
        int cantArrobados = Integer.parseInt(palabra); // paso ese numero que es un string a un entero
        return cantArrobados;
    }

}
