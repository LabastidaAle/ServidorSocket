package com.company;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;



public class Servidor {
    private ServerSocket sServidor; // Socket del servidor
    private Socket sCliente; //Socket para el cliente
    private Scanner entrada; //Flujo de entrada para el envío de datos
    private PrintStream salida; //Flujo de salida para recepción de datos

    private final int puerto; //Puerto por el cual escuchará el servidor
    private javax.swing.Timer t; //Declaramos un Timer

    //Constructor de la clase Servidor
    public Servidor(int puerto){
        this.puerto = puerto;
    }

    public void iniciar(){
        final String SERVIDOR_INICIADO = " - SERVIDOR INICIADO -";
        final String EN_ESPERA_DE_CLIENTE = " - ESPERANDO CLIENTE -";

        try{
            // Se crea el socket del servidor a traves de una instancia de la clase ServerSocket
            sServidor = new ServerSocket(puerto);
            System.out.println(SERVIDOR_INICIADO);
            System.out.println(EN_ESPERA_DE_CLIENTE);

            //El metodo accept(), espera hasta que un cliente realice una conexión
            //Una vez que se ha establecido una conexión por el cliente, este
            // método devolverá un objeto tipo Socket, a través del cual se establecerá
            //la comunicación con el cliente
            sCliente = sServidor.accept();

            //Obtengo una referencia a los flujos de datos de entrada y salida del socket cliente
            entrada  = new  Scanner(sCliente.getInputStream());
            salida = new PrintStream(sCliente.getOutputStream());

            //Sección modificable
            System.out.println("Cliente conectado: " + sCliente.getInetAddress() +":"+ sCliente.getPort());
            //llamada al método menu
            menuDeOpciones();
            //Cerramos la conexión
            finalizar();
        }catch (Exception e){
            finalizar();
            e.printStackTrace();
        }
    }

    //Metodo para cerra conexión
    public void finalizar(){
        final String MSJ_CONEXION = "CONEXION CERRADA....";
        try {
            entrada.close();
            salida.close();
            sCliente.close();
            sServidor.close();
            System.out.println(MSJ_CONEXION);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //Menu de opciones
    public void menuDeOpciones(){
        salida.println("                CONSULTA DE PELICULAS                     ");
        salida.println("**********************************************************");
        salida.println("Que operacion desea realizar?");
        salida.println("Opcion 1 Mostrar las peliculas por ID");
        salida.println("Opcion 2 Mostrar todas las peliculas");
        salida.println("Opcion 3 Consultar datos de una pelicula por nombre");
        salida.println("Opcion 4 Consultar pelicula por anio de lanzamiento");
        salida.println("Opcion 5 Eliminar una pelicula por nombre");
        salida.println("Opcion 6 Editar datos de una pelicula");
        salida.println("Opcion 7 Agregar nueva pelicula");
        salida.println("Opcion 8 para salir");
        
        salida.println("**********************************************************");
        int entradaOp = entrada.nextInt();

        switch (entradaOp){
            case 1:
                salida.println("Ingresa un ID entre el 1 y 1000 para consultar las peliculas disponibles en la DB \n");
                int entradaId = entrada.nextInt();
                ConectionDb db = new ConectionDb(entradaId);
                db.conectarDb();
                ArrayList<Peliculas> peliculasId = db.consultarPeliculasId();
                salida.println(peliculasId.get(0).toString());
                menuDeOpciones();

            case 2:
                ConectionDb consultaTodas = new ConectionDb();
                consultaTodas.conectarDb();
                ArrayList<Peliculas> peliculas = consultaTodas.consultarPeliculas();
                for (Peliculas pelicula : peliculas) {
                    salida.println(pelicula.toString());
                    salida.println(" ");
                }
                menuDeOpciones();
            case 3:
                ConectionDb consultaNombre = new ConectionDb();
                consultaNombre.conectarDb();
                salida.println("Ingrese el nombra a buscar");
                String nombrePelicula = entrada.next();
                ArrayList<Peliculas> peliculasCoincidente = consultaNombre.consultarPeliculasNombre(nombrePelicula);
                for (Peliculas p : peliculasCoincidente) {
                    salida.println(p.toString());
                    salida.println(" ");
                }
                menuDeOpciones();
            case 4:
                ConectionDb consultaAnio = new ConectionDb();
                consultaAnio.conectarDb();
                salida.println("Ingrese el año de la pelicula: ");
                int anio = entrada.nextInt();
                ArrayList<Peliculas> peliculasAnio = consultaAnio.consultarPeliculasAnio(anio);
                for (Peliculas p : peliculasAnio) {
                    salida.println(p.toString());
                    salida.println(" ");
                }
                menuDeOpciones();
            case 5:
                ConectionDb consultaEliminacion = new ConectionDb();
                consultaEliminacion.conectarDb();
                salida.println("Ingrese el nombre de la pelicula a eliminar: ");
                String nameDelete = entrada.next();
                String resultadoOperacion = consultaEliminacion.eliminarRegistro(nameDelete);
                salida.println(resultadoOperacion);
                menuDeOpciones();
            case 6:
                ConectionDb consultaUpdate = new ConectionDb();
                consultaUpdate.conectarDb();
                salida.println("Id a editar");
                int idEd = entrada.nextInt();
                salida.println("Ingrese el nuevo nombre de la pelicula: ");
                String title = entrada.next();
                salida.println("Ingrese el anio de lanzamiento: ");
                int year = entrada.nextInt();
                salida.println("Ingrese la descripcion de lanzamiento: ");
                String descripcion = entrada.next();
                
                String resultadoEdit = consultaUpdate.EditarPelicula(idEd, title, descripcion, year);
                salida.println(resultadoEdit);
                menuDeOpciones(); 

            case 7:
                ConectionDb consultaInsert = new ConectionDb();
                consultaInsert.conectarDb();
                salida.println("Ingrese el nombre de la pelicula: ");
                String titulo = entrada.next();
                salida.println("Ingrese la descripcion: ");
                String descripcion2 = entrada.next();
                
                String resultadoInsert = consultaInsert.InsertarPelicula(titulo, descripcion2);
                salida.println(resultadoInsert);
                menuDeOpciones(); 

            case 8:
                salida.println("Programa finalizado");
                finalizar();
                break;

            default:
                salida.println("Opcion invalida.....\n");
                salida.println(" ");
                menuDeOpciones();
        }
    }

}
