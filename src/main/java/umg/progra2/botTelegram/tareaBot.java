package umg.progra2.botTelegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class tareaBot extends TelegramLongPollingBot {

    private final Map<Long, String> userCarnets = new HashMap<>();
    private final Map<Long, Boolean> infoPending = new HashMap<>();

    @Override
    public String getBotUsername() {
        return "@Nexus0102_bot";  // Nombre del bot
    }

    @Override
    public String getBotToken() {
        return "7319124619:AAFnZ50FGHqnCXALuogX9x0fkvbRyily_fc";  // Token del bot
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            long chat_id = update.getMessage().getChatId();

            System.out.println("El chat_id del usuario es: " + chat_id); //mostrar el id del usuario

            String message_text = update.getMessage().getText();
            String nombre = update.getMessage().getFrom().getFirstName();
            nombre = (nombre != null) ? nombre : "Usuario";

            // Mensaje de inicio
            if (message_text.toLowerCase().equals("/start")) {
                sendText(chat_id, "¡Saludos! ( •̀ ω •́ )✧\n" +
                        "Esta es la lista de comandos disponibles:\n" +
                        "/info - Muestra tu información de usuario.\n" +
                        "/progra - Muestra un poema.\n" +
                        "/hola - Muestra un saludo con la fecha y hora actual.\n" +
                        "/cambio (Escribe el numero para cambio) - Convierte entre EUR y GTQ. Ej: /cambio 798"+
                        " (luego mostrara el cambio en GTQ\n"+
                        "/grupal_mensaje (enviar un mensaje a otras personas) Ej: /grupal_mensaje Hola a todos :D\n"+
                        "(Debe de escribir el mensaje después del comando /grupal_mensaje) de lo contrario no se envia\n"+
                        "/chiste - Muestra un chiste\n"+
                        "/frase - Muestra una frase inspiradora o motivacional"
                );
            }

            // Comando /info
            if (message_text.toLowerCase().startsWith("/info")) {
                if (userCarnets.containsKey(chat_id)) {
                    String carnet = userCarnets.get(chat_id);
                    String info = "Nombre: " + nombre + "\nNúmero de Carnet: " + carnet + "\nSemestre: 4to Semestre";
                    sendText(chat_id, info);

                } else {
                    sendText(chat_id, "Escribe tu número de carnet.");
                    infoPending.put(chat_id, true); // informacion pendiente
                }


            } else if (infoPending.containsKey(chat_id) && infoPending.get(chat_id)) {
                //guardar carnet
                userCarnets.put(chat_id, message_text);
                infoPending.put(chat_id, false);
                sendText(chat_id, "Gracias. Escribe /info para obtener tu información completa.");


                //Comando /hola
            } else if (message_text.toLowerCase().startsWith("/hola")) {
                DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM");
                DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss"); // Formato para la hora


                LocalDateTime now = LocalDateTime.now();
                String fecha = now.format(dtfDate);
                String hora = now.toLocalTime().format(dtfTime);

                sendText(chat_id, "Hola, " + nombre + ", hoy es " + fecha + " y la hora actual es " + hora + ".");

                //Comando /progra
            } else if (message_text.toLowerCase().startsWith("/progra")) {
                sendText(chat_id, "Codificando sueños, línea a línea,\n" +
                        "Un mundo digital, a mi medida.\n" +
                        "Un desafío constante, que me llena,\n" +
                        "Creando historias, con cada idea.");
            } else if (message_text.toLowerCase().startsWith("/cambio")) {

                try {
                    double euros = Double.parseDouble(message_text.split(" ")[1]);
                    double tipoDeCambio = 8.5; // Tipo de cambio
                    double quetzales = euros * tipoDeCambio;
                    sendText(chat_id, String.format("Son %.2f quetzales.", quetzales));

                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    sendText(chat_id, "¡Error!, Escribe un número válido después de /cambio.");
                }

                //grupal mensaje
            } else if (message_text.toLowerCase().startsWith("/grupal_mensaje")) {
                // ingreso de mensaje por el usuario
                String mensaje = message_text.substring("/grupal_mensaje".length()).trim();

                // Lista de chat_ids
                List<Long> listaIds = List.of(5454689659L, 6864167860L, 1573724835L, 6375701250L);
                // Mensaje para cada chat_id
                for (Long id : listaIds) {
                    sendText(id, mensaje);
                }
            }

            else if (message_text.toLowerCase().startsWith("/chiste")) {
                String[] chistes = {
                        "¿Por qué los programadores prefieren la oscuridad? Porque la luz atrae a los bugs.",
                        "¡Error 404! Chiste no encontrado. ",
                        "¿Qué hace un desarrollador a la hora de comer? Depura los platos."
                };
                int randomIndex = (int) (Math.random() * chistes.length);
                sendText(chat_id, chistes[randomIndex]);
            }

            else if (message_text.toLowerCase().startsWith("/frase")) {
                String[] frase = {
                        "El único modo de hacer un gran trabajo es amar lo que haces. - Steve Jobs",
                        "El éxito es la suma de pequeños esfuerzos repetidos día tras día. - Robert Collier",
                        "No hay caminos para la paz, la paz es el camino. - Mahatma Gandhi."
                };
                int randomIndex = (int) (Math.random() * frase.length);
                sendText(chat_id, frase[randomIndex]);
            }
        }
    }
    // Método para enviar mensajes
    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .text(what)
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);

        }
    }
}

