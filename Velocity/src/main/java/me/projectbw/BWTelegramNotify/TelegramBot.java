package me.projectbw.BWTelegramNotify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class TelegramBot {
    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);
    private String botToken;
    private String chatId;

    // Метод для установки токена бота и chatId
    public void setConfig(String botToken, String chatId) {
        this.botToken = botToken;
        this.chatId = chatId;
    }

    // Метод для отправки сообщения в Telegram
    public void sendMessage(String message) {
        if (botToken == null || chatId == null) {
            logger.error("Bot token or chat ID is not set.");
            return;
        }

        // Создание объекта сообщения
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        try {
            // Создание и регистрация бота для отправки сообщения
            TelegramBotsApi botsApi = new TelegramBotsApi();
            botsApi.registerBot(new TelegramBotAbsSender());
            Message sentMessage = botsApi.execute(sendMessage); // Отправка сообщения
            logger.info("Message sent: " + sentMessage.getText());
        } catch (TelegramApiException e) {
            logger.error("Failed to send message: ", e);
        }
    }

    // Вспомогательный класс для работы с Telegram Bot API
    private static class TelegramBotAbsSender extends AbsSender {

        @Override
        public String getBotUsername() {
            return "YOUR_BOT_USERNAME"; // Укажите имя вашего бота
        }

        @Override
        public String getBotToken() {
            return botToken; // Используем токен, переданный в setConfig()
        }

        @Override
        public Message sendApiMethod(SendMessage sendMessage) throws TelegramApiException {
            // Логика отправки сообщения
            return execute(sendMessage);
        }
    }
}