package com.company;

public class Peliculas {
    private final int id;
    private final String titulo;
    private final String descripcion;
    private final int release_year;

    public Peliculas(int id, String titulo, String descripcion, int release_year) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.release_year = release_year;
    }

    @Override
    public String toString(){
        String formatoPelicula = "\nIdentificador: "+ id  + "\nTitulo de la pelicula: "+ titulo + "\nResumen: "+ descripcion + "\nFilmada en: "+ release_year + "\n\n ";
        return  formatoPelicula;
    }

}
