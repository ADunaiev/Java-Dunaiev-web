package step.learning.ioc;

import com.google.inject.AbstractModule;
import step.learning.services.db.DbService;
import step.learning.services.db.MySqlDbService;
import step.learning.services.form.FormParseService;
import step.learning.services.form.HybridFormParser;
import step.learning.services.hash.HashService;
import step.learning.services.hash.Md5HashService;
import step.learning.services.kdf.HashKdfService;
import step.learning.services.kdf.KdfService;

public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        // конфігурація служб
        // Буде запит на HashService -- повернути об'єкт Md5HashService
        bind(HashService.class).to(Md5HashService.class);  // ASP: Service.AddSingleton<Hash,Md5>
        bind(DbService.class).to(MySqlDbService.class);
        bind(FormParseService.class).to(HybridFormParser.class);
        bind(KdfService.class).to(HashKdfService.class);

    }
}
/*

Інверсія управління (Inversion of control) - архетектурний стиль
згідно з яким питання життевого циклу об'єктів вирішуються окремим
спеціальний модулем (контейнером залежностей, інжектором).
Життевий цикл об'єкту - CRUD, у простішому випадку мова йде про
створення (С) об'єктів: чи стрворувати новий інстанс класу / чи
залишати раніше створений.
Також на модуль IoC покладається заповнення (Resolve) об'єктів залежностей,
впровадження (Inject) у нього залежностей - посилань на інші об'єкти,
що їх створює IoC.

Без IoC:
class Klass {
    service = new Service()
    ...
}
k1 = new Klass()
k2 = new Klass()
k1 и k2 різні service

З IoC
class Klass {
    @Inject Service service
    ...
}
k1 = Injector.Resolve(Klass)
k2 = Injector.Resolve(Klass)
у k1 та k2 однакові service
 */
/*
Впровадження на базі Google Guice
Spring - аналог
підключаємо до проєкту

створюємо клас - "слухач" створення контексту (розгортанню застосунку)
(див. IocContextListener)

створюємо класи-конфігуратори:
 = ServiceModule (цей клас) - для налаштування служб за DIP (з SOLID)
    згідно з яким залежності слід створювати не від класів, а від інтерфейсів.
    Але, оскільки об'єкт з інтерфейсу створити не можна, то необхідно встановити
    зв'язок між інтерфейсом та його реалізацією.
 = RouterModule - для маршрутізації сервлетів.
    створюємо інструкції маршрутізації (дивись клас) та замінюємо сервлетні аннотації
    (@WebServlet) на @Singleton для всіх сервлетів.

 Змінюємо налаштування сервера (див. web.xml)
 */