package com.company;

import static com.sun.corba.se.impl.util.Utility.printStackTrace;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class ConectionDb {
    //Datos de para conectar a la Db
    private final String DRIVER = "org.postgresql.Driver";
    private String URL = "jdbc:postgresql://";
    private final String HOST = "localhost";
    private final String PORT = "5432";
    private final String DB_NAME = "dvdrental";
    private final String USER = "postgres";
    private final String PASS = "ConectarMYSQL1";
    //Mensajes
    private final String CONEXION_EXITOSA = "La coneción a las DB ha sido exitosa....";
    //Consultas
    private final String QUERY_PELICULAS = "SELECT film_id, title, description, release_year FROM film ORDER  BY film_id ASC;";
    private final String QUERY_ID = "SELECT film_id, title, description, release_year FROM film WHERE film_id =";
    //Consultar por nombre
    private final String QUERY_NAME = "SELECT film_id, title, description, release_year FROM film WHERE title like '%";
    private final String QUERY_ANIO = "SELECT film_id, title, description, release_year FROM film WHERE release_year =";
    private final String QUERY_DELETE = "DELETE FROM film WHERE title = '";
    private final String QUERY_UPDATE = "UPDATE film SET title ='";
    private final String QUERY_FKDELETE = "ALTER TABLE film DROP CONSTRAINT film_actor_film_id_fkey;";
    private final String QUERY_INSERT = "INSERT INTO film (title,description,release_year, language_id) ";
    //Creamos un objeto de la clase Connection y la inicializamos con null
    Connection connection = null;
    Statement declaracion = null;
    
    Scanner leer = new Scanner(System.in);
    
    // id
    private int id;

    //Nombre
    private String nombre;

    //Anio
    private int anio;

    //Almacenar elementos de la db en un ArrayList
    private ArrayList<Peliculas> peliculas = new ArrayList<Peliculas>();

    //Constructor de la clase que recibe como parametro el id
    public ConectionDb(int id ){
        this.id = id;
    }

    //Constructor vacio
    public  ConectionDb(){
    }

    //metodo que realiza la conexión de manera generica
    public void conectarDb(){
        try{
            //Al método forName le pasamos el DRIVER para conectar a las DB
            Class.forName(DRIVER);
        }catch (ClassNotFoundException ex){
            System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
        }
        //Conexión a la db Postgresql
        try{
            //Pasamos datos para la conexión url, host, port, dbnombre, usuario y contraseña para conectar a la DB
            URL = URL + HOST + ":" + PORT + "/" + DB_NAME;
            connection = DriverManager.getConnection(URL , USER , PASS);
            // Método para comprobación de conexión valida
            boolean valid = connection.isValid(5000);
            if(valid){
                System.out.println(valid? "TEST OK" : "TEST FAIL");
            }
        }catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ":"+ e.getMessage());
            System.exit(0);
        }
    }

    //Consulta de todas las peliculas
    public ArrayList<Peliculas> consultarPeliculas(){
        try {
            //Realizar la consulta a la db
            declaracion = connection.createStatement();
            ResultSet rs = declaracion.executeQuery(QUERY_PELICULAS);
            while(rs.next()){
                int id = rs.getInt("film_id");
                String titulo = rs.getString("title");
                String descripcion = rs.getString("description");
                int release_year = rs.getInt("release_year");
                //Llenamos la lista con los elementos del array list
                peliculas.add(new Peliculas(id,titulo,descripcion,release_year));
            }

            //Cerramos conexión a la db
            rs.close();
            declaracion.close();
            connection.close();

        }catch (Exception e){
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(0);
        }

        return peliculas;
    }

    //Consultar peliculas por nombre
    public ArrayList<Peliculas> consultarPeliculasNombre(String nombre){
        try {
            //Realizar la consulta a la db
            declaracion = connection.createStatement();
            //pasar el nombre a buscar y realizar consulta
            ResultSet rs = declaracion.executeQuery(QUERY_NAME + nombre + "%';");
            while(rs.next()){
                int id = rs.getInt("film_id");
                String titulo = rs.getString("title");
                String descripcion = rs.getString("description");
                int release_year = rs.getInt("release_year");
                peliculas.add(new Peliculas(id,titulo,descripcion,release_year));
            }

            //Cerramos conexión a la db
            rs.close();
            declaracion.close();
            connection.close();

        }catch (Exception e){
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(0);
        }
        return peliculas;
    }

    //Consultar peliculas por nombre
    public ArrayList<Peliculas> consultarPeliculasAnio(int anio){
        try {
            //Realizar la consulta a la db
            declaracion = connection.createStatement();
            //pasar el anio a buscar y realizar consulta
            ResultSet rs = declaracion.executeQuery(QUERY_ANIO + anio + ";");
            while(rs.next()){
                int id = rs.getInt("film_id");
                String titulo = rs.getString("title");
                String descripcion = rs.getString("description");
                int release_year = rs.getInt("release_year");
                peliculas.add(new Peliculas(id,titulo,descripcion,release_year));
            }

            //Cerramos conexión a la db
            rs.close();
            declaracion.close();
            connection.close();

        }catch (Exception e){
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(0);
        }
        return peliculas;
    }    

    //Consultar por identificador
    public ArrayList<Peliculas> consultarPeliculasId(){
        try {
            //Realizar la consulta a la db
            declaracion = connection.createStatement();
            ResultSet rs = declaracion.executeQuery(QUERY_ID + id + ";");
            while(rs.next()){
                int id = rs.getInt("film_id");
                String titulo = rs.getString("title");
                String descripcion = rs.getString("description");
                int release_year = rs.getInt("release_year");
                peliculas.add(new Peliculas(id,titulo,descripcion,release_year));
            }

            //Cerramos conexión a la db
            rs.close();
            declaracion.close();
            connection.close();

        }catch (Exception e){
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(0);
        }

        return peliculas;
    }

    //Eliminar peliculas por nombre
    public String eliminarRegistro(String nameDelete ){
        String resultadoOperacion = "Al parecer no existen coincidencias.\nIntente nuevamente.."; 
        try {
            //Realizar la consulta a la db
            declaracion = connection.createStatement();
            //pasar el nombre a buscar y realizar consulta
      
            declaracion.executeUpdate(QUERY_DELETE + nameDelete + "';" );
            resultadoOperacion = "La pelicula " + nameDelete+ " fue eliminda exitosamente";
      //Cerramos conexión a la db
            declaracion.close();
            connection.close();

        }catch (Exception e){
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(0);
        }
        return resultadoOperacion;
    }

    //Eliminar peliculas por nombre
    public String EditarPelicula(int idEd, String titulo, String descripcion, int year){
        String resultadoOperacion = "Al parecer no existen coincidencias.Intente nuevamente.."; 
        try {
            //Realizar la consulta a la db
            declaracion = connection.createStatement();
            //pasar el nombre a buscar y realizar consulta
            declaracion.executeUpdate(QUERY_UPDATE + titulo + "', description = '" + descripcion + "', release_year = " + year + " WHERE film_id  = " + idEd + ";");
 
            declaracion.close();
            connection.close();
            resultadoOperacion = "El registro fue actualizado correctamente";

        }catch (Exception e){
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(0);
        }
        return resultadoOperacion;
    }

    //Eliminar peliculas por nombre
    public String InsertarPelicula(String tit, String descrip){
        String resultadoOp = "Error al tratar de Insertar"; 
        try {
            System.out.println(tit+ " - " + descrip);
            declaracion.executeUpdate("insert into film (title,description,release_year,language_id) values ('"+tit+"', '"+descrip+"', 2006, 1);");
            resultadoOp = "La pelicula " + tit + " fue agregada exitosamente";
            declaracion.close();
            connection.close();

        }catch (SQLException e){
            System.err.println(e);
            System.exit(0);
        }
        return resultadoOp;
    }
}
