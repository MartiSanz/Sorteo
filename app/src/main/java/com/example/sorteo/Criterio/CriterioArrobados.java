package com.example.sorteo.Criterio;

public class CriterioArrobados implements Criterio{

    private int cantArrobados;

    public CriterioArrobados(int cantArrobados){
        this.cantArrobados = cantArrobados;
    }

    //método para calcular el número de veces que se repite un carácter en un String
    public static int contarCaracteres(String cadena, char caracter) {
        int posicion, contador = 0;
        //se busca la primera vez que aparece
        posicion = cadena.indexOf(caracter);
        while (posicion != -1) { //mientras se encuentre el caracter
            contador++;           //se cuenta
            //se sigue buscando a partir de la posición siguiente a la encontrada
            posicion = cadena.indexOf(caracter, posicion + 1);
        }
        return contador;
    }

    public boolean cumple (String e) {

        if((contarCaracteres(e, '@') >= this.cantArrobados)){
            return true;
        }
        return false;
    }
}
