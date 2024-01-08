package step.learning.ioc;

import com.google.inject.servlet.ServletModule;
import step.learning.filters.CharsetFilter;
import step.learning.filters.CultureFilter;
import step.learning.filters.DbFilter;
import step.learning.filters.UserAgentFilter;
import step.learning.servlets.*;

public class RouterModule extends ServletModule {
    @Override
    protected void configureServlets() {
        filter("/*").through( DbFilter.class        );
        filter("/*").through( CharsetFilter.class   );
        filter("/*").through( CultureFilter.class   );
        filter("/*").through( UserAgentFilter.class );


        serve("/").with(HomeServlet.class);
        serve("/auth").with(AuthServlet.class);
        serve("/signup").with(SignupServlet.class);
        serve("/product").with(AddProductServlet.class);
        serve("/cart").with(CartServlet.class);


        serveRegex("/(\\w\\w/)"             ).with(HomeServlet.class);
        serveRegex("/(\\w\\w/)signup"       ).with(SignupServlet.class);
        serveRegex("/\\w\\w/product"        ).with(AddProductServlet.class);
        serveRegex("/\\w\\w/cart"           ).with(CartServlet.class);
    }
}
