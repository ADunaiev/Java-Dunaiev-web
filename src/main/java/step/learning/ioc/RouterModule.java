package step.learning.ioc;

import com.google.inject.servlet.ServletModule;
import step.learning.servlets.*;

public class RouterModule extends ServletModule {
    @Override
    protected void configureServlets() {
        serve( "/").with(HomeServlet.class);
        serve( "/cart").with(CartServlet.class);
        serve( "/action").with(ActionServlet.class);
        serve( "/signup").with(SignupServlet.class);
        serve( "/product").with(ProductServlet.class);
    }
}
