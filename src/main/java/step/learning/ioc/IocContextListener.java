package step.learning.ioc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class IocContextListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        // налаштування модулю інжектору - модуля, який буде впроваджувати залежності
        return Guice.createInjector(
                new ServiceModule(),
                new RouterModule()
        );
    }
}
/*
Подія створення контексту - аналог запуску виконавчого коду.
Оскільки запуск веб-проектів відбувається через веб-сервер (Tomcat),
то проєкт одержує повідомлення/подію створення контексту.
 */
