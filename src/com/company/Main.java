package com.company;

public class Main {

    public static void main(String[] args) {
        //Creamos un objeto es instanciamos pasando como argumento el puerto al que se tien que conectar
        Servidor servidor = new Servidor(8000);
        servidor.iniciar();
    }
}
