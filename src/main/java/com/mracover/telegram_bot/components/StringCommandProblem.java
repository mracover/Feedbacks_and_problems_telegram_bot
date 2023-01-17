package com.mracover.telegram_bot.components;

public interface StringCommandProblem {
     String PROBLEM_TEXT_NAME = "Вы решили сообщить о проблеме или задать вопрос по товару. Вам будут даны вопросы, на которые необходимо дать ответ." +
             "Вы можете выйти из этого меню воспользовавшись командой /start. \n" +
                     "\n" +
             "Введите ваше имя:";
     String PROBLEM_TEXT_EMAIL = "Введите ваш email для обратной связи:";
     String PROBLEM_TEXT_PRODUCT = "Введите id вашего товара, который указан в магазине (Если у вас общий вопрос введите число 0):";
     String PROBLEM_TEXT_MESSAGE = "Введите вашу проблему:";
     String PROBLEM_IMAGE = "Загрузите фотографию одним сообщение (если вы не хотите оставлять фотографии, то отправьте \"-\"):";
     String PROBLEM_END = "Ваша проблема успешно отправлена, в течении нескольких дней вам ответ на почту";
}
