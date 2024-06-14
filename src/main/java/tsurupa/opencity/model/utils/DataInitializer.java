package tsurupa.opencity.model.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tsurupa.opencity.model.Community;
import tsurupa.opencity.model.Event;
import tsurupa.opencity.model.Photo;
import tsurupa.opencity.model.User;
import tsurupa.opencity.repository.CommunityRepository;
import tsurupa.opencity.repository.EventRepository;
import tsurupa.opencity.repository.UserRepository;

import tsurupa.opencity.repository.PhotoRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(EventRepository eventRepository, UserRepository userRepository, PhotoRepository photoRepository, CommunityRepository communityRepository) {
        return args -> {
            // Проверьте, что пользователь с id 1 существует
            User user = userRepository.findById(1L).orElseGet(() -> {
                User newUser = new User();
                newUser.setId(1L); // Задайте id вручную
                newUser.setEmail("user@yandex.ru");
                newUser.setPassword("1");
                newUser.setRole(Role.user);
                newUser.setRegistationDate(LocalDateTime.now());
                return userRepository.save(newUser);
            });

            // Проверьте, что пользователь с id 1 существует
            User user2 = userRepository.findById(2L).orElseGet(() -> {
                User newUser = new User();
                newUser.setId(2L); // Задайте id вручную
                newUser.setEmail("moderator@yandex.ru");
                newUser.setPassword("1");
                newUser.setRole(Role.moderator);
                newUser.setRegistationDate(LocalDateTime.now());
                return userRepository.save(newUser);
            });

            // Проверьте, что пользователь с id 1 существует
            User user3 = userRepository.findById(3L).orElseGet(() -> {
                User newUser = new User();
                newUser.setId(3L); // Задайте id вручную
                newUser.setEmail("admin@yandex.ru");
                newUser.setPassword("1");
                newUser.setRole(Role.admin);
                newUser.setRegistationDate(LocalDateTime.now());
                return userRepository.save(newUser);
            });

            // Создание и настройка событий
            Event event1 = new Event();
            event1.setId(1L);
            event1.setTitle("Международный день защиты детей в экстрим-парке «УРАМ»");
            event1.setDescription("1 июня в экстрим-парке «УРАМ» в честь Международного дня защиты детей запланирована увлекательная программа: контесты, мастер-классы, танцевальные батлы и маркет для маленьких гостей и гостей города. Начало мероприятия в 10:00. На площадке пройдут соревнования по BMX и МТБ, самокатам и роликам, а также организуют беговелогонку, квесты и футбольные игры. Участники смогут научиться кататься на скейте или самокате, попробовать себя в экстрим-спорте, а также насладиться танцевальными батлами от лучших танцоров республики. Маркет будет представлен товарами от молодых предпринимателей, ориентированными на детей и подростков.");
            event1.setAddress("Экстрим парк Урам");
            event1.setDatetime_start(LocalDateTime.of(2024, 6, 1, 10, 0));
            event1.setDatetime_end(LocalDateTime.of(2024, 6, 1, 20, 0));
            event1.setPrice_min(0.0);
            event1.setPrice_max(0.0);
            event1.setTag(Tag.фестиваль); // Используйте правильное значение из вашего перечисления Tag
            event1.setUpdate_datetime(LocalDateTime.of(2024, 6, 9, 15, 6, 16));
            event1.setStatus(Status.activ); // Используйте правильное значение из вашего перечисления Status
            event1.setUser(user);

            Event event2 = new Event();
            event2.setId(2L);
            event2.setTitle("Хетаг Хугаев. Stand up");
            event2.setDescription("14 июня в 19:00 в КЦ Чулпан Stand up концерт Хетага Хугаева. Хетаг мастерски смешивает серьезные темы с легким и остроумным подходом, создавая уникальный юмористический стиль, способный заставить задуматься и одновременно улыбнуться. Каждое его выступление — это настоящий заряд энергии и позитива, который не оставляет равнодушным ни одного зрителя в зале.");
            event2.setAddress("проспект Победы 48а");
            event2.setDatetime_start(LocalDateTime.of(2024, 6, 14, 19, 0));
            event2.setDatetime_end(LocalDateTime.of(2024, 6, 14, 20, 0));
            event2.setPrice_min(1000.0);
            event2.setPrice_max(2500.0);
            event2.setTag(Tag.другое); // Используйте правильное значение из вашего перечисления Tag
            event2.setUpdate_datetime(LocalDateTime.now());
            event2.setStatus(Status.activ); // Используйте правильное значение из вашего перечисления Status
            event2.setUser(user);

            Event event3 = new Event();
            event3.setId(3L);
            event3.setTitle("Мультимедийный ресторан «Калейдоскоп»");
            event3.setDescription("Мировая премьера в Казани - новый для Казани мультимедийный ресторан  «Калейдоскоп» - сочетание медиаискусства, гастрономических шедевров и летописи Татарстана!\n" +
                    "\n" +
                    "Представление разворачивается в особом круглом зале, в котором стены становятся интерактивной сценой мультимедийного спектакля.\n" +
                    "\n" +
                    "Спектакль «Туган як»  посвящён истории Татарстана от Волжской Булгарии до наших дней. Пять ярких эпизодов, пять смен блюд современной татарской кухни и 360 градусов впечатлений, эмоций и вкусовых фантазий. \n" +
                    "\n" +
                    "Это шоу для тех, кто хочет понять и почувствовать Татарстан. «Туган як» — настоящий калейдоскоп впечатлений, где вкус и искусство сочетаются воедино, заставляя каждого зрителя пережить незабываемые моменты.");
            event3.setAddress("улица Туфана Миннуллина, 14/56");
            event3.setDatetime_start(LocalDateTime.of(2024, 6, 15, 18, 0));
            event3.setDatetime_end(LocalDateTime.of(2024, 6, 15, 20, 0));
            event3.setPrice_min(9800.0);
            event3.setPrice_max(9800.0);
            event3.setTag(Tag.другое); // Используйте правильное значение из вашего перечисления Tag
            event3.setUpdate_datetime(LocalDateTime.now());
            event3.setStatus(Status.verification); // Используйте правильное значение из вашего перечисления Status
            event3.setUser(user);

            Event event4 = new Event();
            event4.setId(4L);
            event4.setTitle("Спектакль \"Болганчык еллар. Мөһаҗирләр / Муть. Мухаджиры\"");
            event4.setDescription("Новый театральный проект Театра им. Г. Камала, в основу которого легли важные для татарской культуры тексты, представит татарский мир рубежа 19-20 вв., переживающий исторические потрясения, связанные с голодом, охватившим Поволжье в 1880-х, и первой всеобщей переписью населения Российской империи 1897 г.. Авторы спектакля постараются найти точки соприкосновения текста вековой давности с сегодняшним человеком, который, как и века назад, ищет, ждёт, стремится быть счастливым?!");
            event4.setAddress("ул. Татарстан 1");
            event4.setDatetime_start(LocalDateTime.of(2024, 6, 30, 18, 0));
            event4.setDatetime_end(LocalDateTime.of(2024, 6, 30, 20, 0));
            event4.setPrice_min(300.0);
            event4.setPrice_max(2000.0);
            event4.setTag(Tag.культура); // Используйте правильное значение из вашего перечисления Tag
            event4.setUpdate_datetime(LocalDateTime.now());
            event4.setStatus(Status.verification); // Используйте правильное значение из вашего перечисления Status
            event4.setUser(user);

            Community com1 = new Community();
            com1.setId(1L);
            com1.setTitle("PROSTranstvO_BY_ONSO");
            com1.setDescription("•Казань\uD83D\uDCCD\n" +
                    "•Hip-Hop School “I CAN”\n" +
                    "•Основана в 2011 году\uD83E\uDEB4\n" +
                    "•Любим танцевать-учим танцевать\uD83D\uDC8C\n" +
                    "•Аренда зала в центре Казани \uD83E\uDEF6\uD83C\uDFFC");
            com1.setContact_info("Заходи в наш инстаграмм: prostranstvo_by_onso");
            com1.setTag(Tag.танцы); // Используйте правильное значение из вашего перечисления Tag
            com1.setUpdate_datetime(LocalDateTime.now());
            com1.setStatus(Status.verification); // Используйте правильное значение из вашего перечисления Status
            com1.setUser(user);

            Community com2 = new Community();
            com2.setId(2L);
            com2.setTitle("DANCE CENTRE LEVEL UP");
            com2.setDescription("Ул. Камалеева 34А\n" +
                    "\n" +
                    "Левел ап- это группы разного уровня.\n" +
                    "Начиная от людей с нулевой подготовкой до танцоров с опытом!\n" +
                    "Это самые профессиональные педагоги, за плечами которых годы преподавательской деятельности!\n" +
                    "Это люди, которые помогут вам раскрыться, найти именно свой стиль, это люди которые знают историю культуры танца, танцевальную базу на которой строится сам танец, они помогут вам почувствовать музыку и своё тело!\n" +
                    "Это постановки, отчётники, вечеринки, знакомства!\n" +
                    "Это нельзя пропустить!\n" +
                    "ИДЁТ НАБОР ВО ВСЕ ГРУППЫ!\n" +
                    "\n" +
                    "-шикарный новый зал\n" +
                    "-приемлемые цены\n" +
                    "-новое покрытие и профессиональный свет\n" +
                    "-харизматичные и профессиональные преподаватели\n" +
                    "-душевая кабин, мужская и женская раздевалка, холл с диванчиком, стильный лофт дизайн\n" +
                    "-специально оборудованная вентиляционная система.\n" +
                    "-спектр разных направлений+детские группы.\n" +
                    "\n" +
                    "- HIP-HOP DANCE /HIP-HOP CHOREO\n" +
                    "- HIP-HOP FREESTYLE\n" +
                    "- HOUSE DANCE\n" +
                    "- DANCE CLASS\n" +
                    "- HIGH HEELS\n" +
                    "- CONTEMPOPARY\n" +
                    "- KIDS DANCE 4+\n" +
                    "- DANCEHALL\n" +
                    "- JAZZ-FUNK\n" +
                    "- POPPING\n" +
                    "-ДЕТСКАЯ РАЗВИВАЮЩАЯ ХОРЕОГРАФИЯ(от4 до 12 лет)\n" +
                    "\n" +
                    "Dance centre LeveL Up- это не просто ещё одна школа.. это школа танцев которая была создана самими танцорами и стала воплощением всех наших идей. Мы учли все плюсы и минусы залов, расписания, аппаратуры, вентиляции, освещения, дизайна, цены и атмосферы!\n" +
                    "А кто, как не сами танцоры и преподаватели знают лучше, что именно вы хотите от уроков? :)\n" +
                    "Здесь будет ваш 2й дом!");
            com2.setContact_info("Заходи в наш инстаграмм: dc_level_up," +
                    "\n+7 (987) 414-55-22");
            com2.setTag(Tag.танцы); // Используйте правильное значение из вашего перечисления Tag
            com2.setUpdate_datetime(LocalDateTime.now());
            com2.setStatus(Status.activ); // Используйте правильное значение из вашего перечисления Status
            com2.setUser(user);

            Community com3 = new Community();
            com3.setId(3L);
            com3.setTitle("Настольные игры MindGames Казань");
            com3.setDescription("MindGames - самый большой магазин настольных игр, комиксов и гик-атрибутики в Казани! Здесь каждый найдёт что-нибудь интересное для себя или в подарок, а для постоянных клиентов действует очень выгодная бонусная система ✨\n" +
                    "\n" +
                    "Мы с любовью выбираем и привозим для вас самые лучшие настольные игры, а также комиксы, тысячи мелочей по разным фильмам, сериалам, играм и аниме, и даже оригинальную коллекционную продукцию!\n" +
                    "\n" +
                    "Кроме того у нас есть большое игровое пространство, где мы собрали почти 500 самых лучших открытых настолок - и каждый может в них поиграть! В нем проводятся регулярные бесплатные игротеки и другие мероприятия по настольным играм.");
            com3.setContact_info("Наш адрес:\n" +
                    "\uD83D\uDD38Островского, 57Б\n" +
                    "с 10:00 до 22:00, тел: +7 987 410-12-14");
            com3.setTag(Tag.настолки); // Используйте правильное значение из вашего перечисления Tag
            com3.setUpdate_datetime(LocalDateTime.now());
            com3.setStatus(Status.activ); // Используйте правильное значение из вашего перечисления Status
            com3.setUser(user);

            // Список событий для добавления
            List<Event> events = Arrays.asList(event1, event2, event3, event4);

            // Добавление событий, если они не существуют
            for (Event event : events) {
                eventRepository.findById(event.getId()).orElseGet(() -> eventRepository.save(event));
            }

            // Список событий для добавления
            List<Community> communities = Arrays.asList(com1, com2, com3);

            // Добавление событий, если они не существуют
            for (Community community : communities) {
                communityRepository.findById(community.getId()).orElseGet(() -> communityRepository.save(community));
            }

            // Двумерный массив с названиями файлов и соответствующими идентификаторами сущностей
            String[][] fileEntityMappings = {
                    {"урам.jpg", "1", "0"},
                    {"СтендАп.jpg", "2", "0"},
                    {"ресторан.jpg", "3", "0"},
                    {"спектакльТатарский.jpg", "4", "0"},
                    {"ican.jpg", "1", "1"},
                    {"lvlUp.jpg", "2", "1"},
                    {"mindGames.jpg", "3", "1"},
                    // Добавьте другие файлы и идентификаторы здесь
            };

            // Путь к папке с фотографиями
            Path photoDir = Paths.get("src\\main\\resources\\События");

            // Загрузка файлов из массива
            for (String[] mapping : fileEntityMappings) {
                String fileName = mapping[0];
                long entityId = Long.parseLong(mapping[1]);

                Path filePath = photoDir.resolve(fileName);
                if (Files.exists(filePath)) {
                    try {
                        byte[] photoData = Files.readAllBytes(filePath);
                        eventRepository.findById(entityId).ifPresent(event -> {
                            Photo photo = new Photo();
                            photo.setData(photoData);
                            photo.setEntityId(event.getId());
                            photo.setType(mapping[2].equals("0") ? EntityType.event : EntityType.community);
                            photo.setUser(user);
                            photoRepository.save(photo);
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("File not found: " + filePath);
                }
            }

        };
    }
}

