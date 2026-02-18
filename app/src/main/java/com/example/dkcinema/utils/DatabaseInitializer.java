package com.example.dkcinema.utils;

import android.content.Context;

import com.example.dkcinema.database.AppDatabase;
import com.example.dkcinema.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class DatabaseInitializer {
    public static void populateMovies(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);

        if (db.movieDao().getAllMovies().isEmpty()) {
            List<Movie> movies = new ArrayList<>();


            movies.add(new Movie(
                    "Начало",
                    "Профессиональный вор промышленных секретов Кобб получает задание внедрить идею в подсознание человека через сновидения.",
                    "https://images-s.kinorium.com/movie/poster/472809/w1500_52479049.jpg",
                    "Фантастика",
                    2010,
                    0
            ));

            movies.add(new Movie(
                    "Тёмный рыцарь",
                    "Бэтмен вступает в смертельную схватку с Джокером, который сеет хаос в Готэме.",
                    "https://avatars.mds.yandex.net/i?id=9bb133ef55124ade92e02311ca8c9c96_l-10812190-images-thumbs&n=13",
                    "Боевик",
                    2008,
                    0
            ));

            movies.add(new Movie(
                    "Интерстеллар",
                    "Группа исследователей отправляется в путешествие через червоточину, чтобы найти новый дом для человечества.",
                    "https://ir.ozone.ru/s3/multimedia-6/6386271618.jpg",
                    "Фантастика",
                    2014,
                    0
            ));

            movies.add(new Movie(
                    "Криминальное чтиво",
                    "Несколько переплетающихся историй о бандитах, боксёре и жене гангстера.",
                    "https://ir.ozone.ru/s3/multimedia-m/6190803370.jpg",
                    "Криминал",
                    1994,
                    0
            ));

            movies.add(new Movie(
                    "Крёстный отец",
                    "Глава мафиозной семьи передаёт власть своему сыну, который неохотно вступает в криминальный мир.",
                    "https://ir.ozone.ru/s3/multimedia-1-z/7610890679.jpg",
                    "Криминал",
                    1972,
                    0
            ));

            movies.add(new Movie(
                    "Побег из Шоушенка",
                    "Банкир Энди Дюфрейн приговорён к пожизненному заключению за убийство жены, которого не совершал. В тюрьме он находит друзей и надежду.",
                    "https://ir.ozone.ru/s3/multimedia-a/6386271802.jpg",
                    "Драма",
                    1994,
                    0
            ));

            movies.add(new Movie(
                    "Властелин колец: Братство кольца",
                    "Скромный хоббит Фродо получает задание уничтожить Кольцо Всевластья в огне вулкана, чтобы не дать тёмному властелину поработить мир.",
                    "https://ir.ozone.ru/s3/multimedia-y/6840322846.jpg",
                    "Фэнтези",
                    2001,
                    0
            ));

            movies.add(new Movie(
                    "Криминальное чтиво 2",
                    "Продолжение культового фильма с новыми переплетающимися историями.",
                    "https://images-s.kinorium.com/movie/fanart/100973/w1500_52662780.jpg",
                    "Криминал",
                    1995,
                    0
            ));

            movies.add(new Movie(
                    "Зелёная миля",
                    "Надзиратель в тюрьме для смертников знакомится с необычным заключённым, обладающим сверхъестественной силой.",
                    "https://images-s.kinorium.com/movie/poster/109273/h280_2704625.jpg",
                    "Драма",
                    1999,
                    0
            ));

            movies.add(new Movie(
                    "Форрест Гамп",
                    "От лица главного героя рассказывается история его необыкновенной жизни, полной удивительных событий.",
                    "https://images-s.kinorium.com/movie/poster/99975/w1500_51950370.jpg",
                    "Драма",
                    1994,
                    0
            ));

            movies.add(new Movie(
                    "1+1",
                    "Аристократ, прикованный к инвалидному креслу, нанимает в сиделки бывшего заключённого.",
                    "https://images-s.kinorium.com/movie/poster/537521/h280_37554871.jpg",
                    "Комедия",
                    2011,
                    0
            ));

            movies.add(new Movie(
                    "Бойцовский клуб",
                    "Сотрудник страховой компании страдает бессонницей и встречает загадочного Тайлера Дёрдена.",
                    "https://www.kino-teatr.ru/movie/poster/28548/95150.jpg",
                    "Триллер",
                    1999,
                    0
            ));

            movies.add(new Movie(
                    "Матрица",
                    "Хакер Нео узнаёт, что его мир — всего лишь виртуальная реальность, созданная машинами.",
                    "https://static.kinoafisha.info/k/movie_posters/1920x1080/upload/movie_posters/8/5/7/3788758/7a9d59fd45c4f4a0aa986ef8e396b056.jpg",
                    "Фантастика",
                    1999,
                    0
            ));

            movies.add(new Movie(
                    "Гладиатор",
                    "Римский полководец Максимус становится рабом и вынужден сражаться на арене за свою жизнь.",
                    "https://ir.ozone.ru/s3/multimedia-y/6146122594.jpg",
                    "Исторический",
                    2000,
                    0
            ));

            movies.add(new Movie(
                    "Титаник",
                    "История любви между пассажирами первого и третьего класса на борту злополучного лайнера.",
                    "https://ir.ozone.ru/s3/multimedia-5/6190792877.jpg",
                    "Драма",
                    1997,
                    0
            ));

            movies.add(new Movie(
                    "Аватар",
                    "Бывший морпех получает задание внедриться в тело аватара среди синих гуманоидов Пандоры.",
                    "https://images-s.kinorium.com/movie/poster/372833/w1500_53524769.jpg",
                    "Фантастика",
                    2009,
                    0
            ));

            movies.add(new Movie(
                    "Мстители: Финал",
                    "Оставшиеся в живых мстители пытаются вернуть исчезнувшую половину человечества.",
                    "https://images-s.kinorium.com/movie/poster/1442354/w1500_44798403.jpg",
                    "Боевик",
                    2019,
                    0
            ));

            movies.add(new Movie(
                    "Джентльмены",
                    "Американский эмигрант хочет продать свою империю по выращиванию марихуаны в Лондоне.",
                    "https://images-s.kinorium.com/movie/poster/1670490/w1500_48938407.jpg",
                    "Комедия",
                    2019,
                    0
            ));

            movies.add(new Movie(
                    "Остров проклятых",
                    "Два судебных пристава расследуют исчезновение пациентки в клинике для душевнобольных.",
                    "https://images-s.kinorium.com/movie/poster/431590/w1500_55185314.jpg",
                    "Триллер",
                    2010,
                    0
            ));

            movies.add(new Movie(
                    "Паразиты",
                    "Семья бедняков проникает в дом богатых, притворяясь высококвалифицированными специалистами.",
                    "https://static.kinoafisha.info/k/movie_posters/800x1200/upload/movie_posters/9/1/5/8355519/4f39cdcf3740f39361c78cfd32c1226e.jpeg",
                    "Триллер",
                    2019,
                    0
            ));

            movies.add(new Movie(
                    "Дюна",
                    "Молодой гений Пол Атрейдес отправляется на опасную планету Арракис, чтобы защитить свой народ.",
                    "https://images-s.kinorium.com/movie/poster/2808886/w1500_52317521.jpg",
                    "Фантастика",
                    2021,
                    0
            ));

            movies.add(new Movie(
                    "Социальная сеть",
                    "История создания Facebook и судебные разбирательства вокруг него.",
                    "https://cdn.ananasposter.ru/image/cache/catalog/poster/drugoe/75/46854-1000x830.jpg",
                    "Драма",
                    2010,
                    0
            ));

            movies.add(new Movie(
                    "Богемская рапсодия",
                    "История группы Queen и её лидера Фредди Меркьюри.",
                    "https://static.kinoafisha.info/k/movie_posters/1920x1080/upload/movie_posters/0/0/8/8329800/77ffa967529e0dc5d220803498c48a4e.jpeg",
                    "Музыка",
                    2018,
                    0
            ));

            movies.add(new Movie(
                    "Тайна Коко",
                    "Мальчик Мигель попадает в мир мёртвых, чтобы найти своего прадеда-музыканта.",
                    "https://images-s.kinorium.com/movie/poster/661432/w1500_52002011.jpg",
                    "Мультфильм",
                    2017,
                    0
            ));

            db.movieDao().insertAll(movies);
        }
    }
}