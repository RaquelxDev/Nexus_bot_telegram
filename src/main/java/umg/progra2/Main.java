package umg.progra2;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import umg.progra2.botTelegram.tareaBot;

public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            tareaBot bot = new tareaBot();
            botsApi.registerBot(bot);
            System.out.println("El bot está funcionando...");
        } catch (Exception ex) {
            System.out.println("error: " + ex.getMessage());
        }
    }
}
