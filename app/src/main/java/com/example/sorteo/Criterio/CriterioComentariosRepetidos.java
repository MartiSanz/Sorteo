package com.example.sorteo.Criterio;

public class CriterioComentariosRepetidos implements Criterio {

    private final String Si = "Si";
    private final String No = "No";

    public CriterioComentariosRepetidos(){

    }

    public boolean cumple (String e) {

        if (e.contains(this.Si)){
            return true;
        }
        return false;
    }
}
