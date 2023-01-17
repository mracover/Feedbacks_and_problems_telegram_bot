package com.mracover.telegram_bot.components;

public interface StringCommandFeedback {
    String FEEDBACK_TEXT_NAME = "Вы решили оставить отзыв по товару. Вам будут даны вопросы, на которые необходимо дать ответ." +
            "Вы можете выйти из этого меню воспользовавшись командой /start. \n" +
            "\n" +
            "Введите ваше имя:";
    String FEEDBACK_TEXT_EMAIL = "Введите ваш email:";
    String FEEDBACK_TEXT_PRODUCT = "Введите id вашего товара, который указан в магазине (например \"231\"):";
    String FEEDBACK_TEXT_MESSAGE = "Введите ваш отзыв:";
    String FEEDBACK_IMAGE = "Загрузите фотографию (если вы не хотите оставлять фотографии, то отправьте \"-\"):";
    String FEEDBACK_END = "Ваш отзыв успешно оставлен, спасибо за покупку! Будем ждать еще.";
}
