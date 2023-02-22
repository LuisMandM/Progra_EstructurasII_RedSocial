package com.ikasgela;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Main {

    public static Map<String, Usuario> usuarios = new TreeMap<>();
    public static List<Mensaje> mensajes = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean running = true;
        Usuario online_User;

        do {
            System.out.print("\n-------------- Turuter --------------\n" +
                    "-------------------------------------\n1. Nuevo usuario \n" +
                    "2. Iniciar sesión o cambiar de usuario\n3.Salir\nOpcion: ");
            try {
                int opcion_Inicio = Integer.parseInt(br.readLine());
                switch (opcion_Inicio) {
                    case 1:
                        CrearUsuario();
                        break;
                    case 2:
                        if (usuarios.size() > 0) {
                            online_User = ValidarHandle();
                            if (online_User == null) {
                                System.out.println("Usuario No encontrado Intenta nuevamente");
                            } else {
                                boolean current_Sesion = true;
                                System.out.println("-------------------------------------\n" +
                                        "Sesión iniciada como: " + online_User.getHandle() + "\n" +
                                        "-------------------------------------"
                                );
                                do {
                                    System.out.print(
                                            "\n1. Publicar mensaje \n" +
                                                    "2. Seguir a otro usuario \n" +
                                                    "3. Dar un \"like\" a un mensaje de otro usuario\n" +
                                                    "4. Ver los mensajes de hoy de usuarios a los que sigues\n" +
                                                    "5. Mostrar el número de seguidores y \"likes\" de la cuenta actual \n" +
                                                    "6. Ver todos los usuarios y sus seguidores (ordenados por handle " +
                                                    "y destacando los verificados)\n" +
                                                    "7. Mostrar los 5 usuarios con más seguidores\n8.Cerrar Sesion" +
                                                    "\nOpcion: ");
                                    int opcion = Integer.parseInt(br.readLine());

                                    switch (opcion) {
                                        case 1:
                                            PublicarMensaje(online_User);
                                            break;
                                        case 2:
                                            SeguirUsuario(online_User);
                                            break;
                                        case 3:
                                            DarLike();
                                            break;
                                        case 4:
                                            RevisionMensajesDia(online_User);
                                            break;
                                        case 5:
                                            int total_Likes = 0;
                                            for (Mensaje publicacion : online_User.getPublicaciones()) {
                                                total_Likes += publicacion.getLikes();
                                            }
                                            System.out.println("Seguidores: " + online_User.getSeguidores().size()
                                                    + "\nTotal likes: " + total_Likes);
                                            break;
                                        case 6:
                                            ShowUsuarios();
                                            break;
                                        case 7:
                                            Top5_Users();
                                            break;
                                        case 8:
                                            current_Sesion = false;
                                            System.out.println("Cerrando sesion..");
                                            break;
                                        default:
                                            System.out.println("Opcion Invalida");
                                            break;
                                    }
                                } while (current_Sesion);
                            }
                        } else {
                            System.out.println("Sin usuarios registrados aún");
                        }
                        break;
                    case 3:
                        running = false;
                        System.out.println("Saliendo");
                        break;
                    default:
                        System.out.println("Opcion Invalida");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Tipo de Dato Ingresado\n");
            }
        } while (running);

    }

    private static void Top5_Users() {
        if (usuarios.size() >= 5) {
            Map<Integer, Usuario> top = new TreeMap<>();

            for (Map.Entry<String, Usuario> entry : usuarios.entrySet()) {
                int num_seguidores = entry.getValue().getSeguidores().size();
                top.put(num_seguidores, entry.getValue());
            }

            LinkedList<Usuario> usuarios_top = new LinkedList<>();
            top.forEach((seguidores, usuario) -> usuarios_top.addFirst(usuario));


            if (usuarios_top.size() >= 5) {
                System.out.printf("TOP 5 USUARIOS MAS SEGUIDOS\n%-15s | %-25s | %-5s\n",
                        "@Handle", "Nombre", "Followers");
                for (int i = 0; i < 5; i++) {
                    Usuario usuario_top = usuarios_top.get(i);
                    System.out.printf("%-15s | %-25s | %-5d\n", usuario_top.getHandle()
                            , usuario_top.getNombre(), usuario_top.getSeguidores().size());
                }
            } else {
                System.out.println("Sin datos suficientes para mostrar top, intente despues");
            }
        } else {
            System.out.println("No es posible visualizar esta informacion en este momento");
        }
    }

    private static void ShowUsuarios() {
        System.out.printf("%-20s | %-5s\n", "Usuario", "Num_Followers");
        Map<String, Usuario> destacados = new TreeMap<>();
        for (Map.Entry<String, Usuario> entry : usuarios.entrySet()) {
            Nivel nivel = entry.getValue().getNivel();
            if (nivel == Nivel.VERIFICADO) {
                destacados.put(entry.getKey(), entry.getValue());
            }
        }

        destacados.forEach((handle, usuario)
                -> System.out.printf("%-20s | %-5d\n",
                handle, usuario.getSeguidores().size()));

        Map<String, Usuario> usuarios_Ordenados = new TreeMap<>(usuarios);
        for (Map.Entry<String, Usuario> entry : usuarios_Ordenados.entrySet()) {
            if (!destacados.containsKey(entry.getKey())) {
                System.out.printf("%-20s | %-5d\n",
                        entry.getKey(), entry.getValue().getSeguidores().size());
            }
        }
    }

    private static void RevisionMensajesDia(Usuario online_User) {
        if (online_User.getSeguidos().size() > 0) {
            int publicaciones_mostradas = 0;
            LocalDate hoy = LocalDate.now();
            for (Usuario seguido : online_User.getSeguidos()) {
                for (Mensaje publicacion : seguido.getPublicaciones()) {
                    LocalDateTime fecha = publicacion.getFecha_Publicacion();
                    LocalDate fecha_publi = LocalDate.of(fecha.getYear(), fecha.getMonth(),
                            fecha.getDayOfMonth());
                    if (fecha_publi.equals(hoy)) {
                        System.out.println(publicacion);
                        publicaciones_mostradas++;
                    }
                }
            }
            if (publicaciones_mostradas == 0) {
                System.out.println("Sin publicaciones de hoy");
            }
        } else {
            System.out.println("Aun no sigues a nadie");
        }
    }

    private static void DarLike() throws IOException {
        Usuario user = ValidarHandle();
        if (user != null) {

            if (user.getPublicaciones().size() > 0) {
                Mensaje mensaje = Seleccion_Menu("Indique el mensaje a dar like: ",
                        user.getPublicaciones());
                if (mensaje != null) mensaje.DarLike();
                else
                    System.out.println("Error en la seleccion del mensaje intente de nuevo");
            } else {
                System.out.println(user.getHandle() + " aun no tiene publicaciones.");
            }

        } else {
            System.out.println("Usuario No encontrado Intenta nuevamente");
        }
    }

    private static void PublicarMensaje(Usuario online_User) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Indique el texto del mensaje: ");
        String mensaje = br.readLine();

        Mensaje mensaje_actual = new Mensaje(mensaje, LocalDateTime.now(), online_User);
        online_User.NewMensaje(mensaje_actual);
        mensajes.add(mensaje_actual);

        System.out.println("Mensaje Publicado exitosamente");
    }

    private static void SeguirUsuario(Usuario online_User) throws IOException {
        Usuario seguir = ValidarHandle();
        boolean seguido = false;
        if (seguir != null) {
            for (Usuario seguidor : seguir.getSeguidores()) {
                if (seguidor.getHandle().equals(online_User.getHandle())) {
                    seguido = true;
                    break;
                }
            }

            if (seguido) {
                System.out.println("Ya sigues a este usuario");
            } else {
                online_User.NewSeguido(seguir);
                seguir.NewSeguidor(online_User);
            }
        } else {
            System.out.println("Usuario No encontrado Intenta nuevamente");
        }
    }

    private static Usuario ValidarHandle() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Usuario online_User = null;
        String inicio_Sesion = "@";
        System.out.print("Indique el Handle, sin '@': ");
        inicio_Sesion = inicio_Sesion.concat(br.readLine());
        for (Map.Entry<String, Usuario> entry : usuarios.entrySet()) {
            if (entry.getKey().equals(inicio_Sesion)) {
                online_User = entry.getValue();
                break;
            }
        }
        return online_User;
    }

    private static void CrearUsuario() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean handle_ok = false;
        String handle = "@";
        do {
            System.out.print("Indique el Handle que desea sin '@': ");
            handle = handle.concat(br.readLine());
            if (usuarios.containsKey(handle)) {
                System.out.println("Handle ya usado, intente nuevamente");
            } else {
                handle_ok = true;
            }
        } while (!handle_ok);


        handle = handle.replace(" ", "");
        System.out.print("Indique su nombre: ");
        String nombre = br.readLine();

        try {
            System.out.print("Indique el nivel deseado:\n-Normal\n-Verificado\nOpcion: ");
            Nivel nivel = Nivel.valueOf(br.readLine().toUpperCase());
            Usuario usuario_Create = new Usuario(handle, nombre, LocalDateTime.now(), nivel);
            usuarios.put(handle, usuario_Create);
            System.out.println("Usuario Creado Exitosamente");
        } catch (IllegalArgumentException e) {
            System.out.println("Error en el tipo de dato ingresado");
        }
    }

    /**
     * Funcion para retornar un objeto seleccionado de una lista mostrada en pantalla
     * y seleccionada mediante el teclado
     *
     * @param texto     Texto que va en la cabecera de presentacion del menú
     * @param coleccion Coleccion o referencia a esta, de donde se estima sacar el objeto
     * @param <E>       Tipo generico
     * @return Objeto de tipo dado por coleccion
     * @throws IOException - Uso de BufferedReader
     */
    private static <E> E Seleccion_Menu(String texto, List<E> coleccion) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(texto);
        for (int i = 0; i < coleccion.size(); i++) {
            System.out.println((i + 1) + "-" + coleccion.get(i));
        }
        System.out.print("Opcion: ");
        int selec_Coleccion;
        try {
            selec_Coleccion = Integer.parseInt(br.readLine()) - 1;
            if (selec_Coleccion >= coleccion.size() || selec_Coleccion < 0) {
                System.out.println("Error: Opción fuera de los limites");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Tipo de dato ingresado invalido");
            return null;
        }
        return coleccion.get(selec_Coleccion);
    }

}
