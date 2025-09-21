package org.example.Component;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component // Помечаем класс как Spring-компонент для автоматической регистрации
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}") // Инжектим имя бота из application.properties
    private String botUsername;

    @Value("${telegram.bot.token}") // Инжектим токен бота из application.properties
    private String botToken;

    public TelegramBot() {
        super();
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Проверяем, что update содержит сообщение и текст
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText(); // Получаем текст сообщения
            long chatId = update.getMessage().getChatId(); // Получаем ID чата

            // Создаем объект SendMessage для отправки ответа
            SendMessage message = SendMessage
                    .builder()
                    .chatId(chatId)
                    .text("you said: " + messageText)
                    .build();
            try {
                execute(message); // Отправляем сообщение через Telegram API
            } catch (TelegramApiException e) {
                System.err.println("Ошибка при отправке сообщения: " + e.getMessage());
            }
        }
    }
}


