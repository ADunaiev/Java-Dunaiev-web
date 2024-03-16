package step.learning.ioc;

import com.google.inject.servlet.ServletModule;
import step.learning.servlets.ActionServlet;
import step.learning.servlets.CartServlet;
import step.learning.servlets.HomeServlet;
import step.learning.servlets.SignupServlet;

public class RouterModule extends ServletModule {
    @Override
    protected void configureServlets() {
        serve( "/").with(HomeServlet.class);
        serve( "/cart").with(CartServlet.class);
        serve( "/action").with(ActionServlet.class);
        serve( "/signup").with(SignupServlet.class);
    }
}
