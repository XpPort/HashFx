# HashFX

Тестовое меню для кусрвовой работы, демонстрирующие работоспособность алгоритма bCrypt.

- Интерфейс TornadoFX (JavaFX)
- Хэширование BCrypt
- Язык разработки Kotlin (поддержка пакета KotlinX)


------------

Входными данными для функции bcrypt являются строка пароля (длиной до 72 байт), числовая стоимость и 16-байтовое (128-битное) значение соли. Соль обычно является случайным значением. Функция bcrypt использует эти входные данные для вычисления 24-байтового (192-битного) хэша. Конечный результат функции bcrypt представляет собой строку вида:
    
$ 2<a / b / x / y> $[стоимость] $ [соль из 22 символов] [хэш из 31 символа]

Например, при вводе пароля abc123xyz, стоимости 12 и случайной соли результатом bcrypt является строка
    
    $ 2a $ 12 $ R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUW
    \__/\/ \____________________/\_____________________________/
    Стоимость хэша соли Alg
    

      1. $2a$ Идентификатор алгоритма хэширования (bcrypt)
      2. 12 Стоимость ввода (212, т.е. 4096 раундов)
      3. R9h/cIPz0gi.URNNX3kh2O: Кодировка входной соли на основе base-64
      4. PST9/PgBkqquzi.Ss7KIUgO2t0jWMUW: Кодировка на основе 64 первых 23 байт вычисленного 24-байтового хэша

Кодировка base-64 в bcrypt использует таблицу ./ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789,[9] которая отличается от КодировкаBase64 4648 RFC.


# HashFX GUI

![](https://i.imgur.com/0JMGJmt.png)
****
![](https://i.imgur.com/Rms1sWv.png)
