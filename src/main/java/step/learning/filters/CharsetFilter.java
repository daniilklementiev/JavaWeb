package step.learning.filters;

import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Singleton
public class CharsetFilter implements Filter {

    private FilterConfig filterConfig;
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter                                    // основная активность фильтра
                        (ServletRequest servletRequest,     // !!! запрос и ответ
                         ServletResponse servletResponse,   // в общем случае - не только HTTP
                         FilterChain filterChain)           // цепочка фильтров
            throws IOException, ServletException
                        {          // (~delegate next)
                            HttpServletRequest req = (HttpServletRequest) servletRequest;
                            HttpServletResponse resp = (HttpServletResponse) servletResponse;
                            String charset = StandardCharsets.UTF_8.name();
                            req.setCharacterEncoding(charset);
                            resp.setCharacterEncoding(charset);

                            // для передачи данных из фильтра можно использовать атрибуты запроса
                            req.setAttribute("charset", charset);

                            // фильтр кодировки
                            filterChain.doFilter(servletRequest, servletResponse);
                            // после этого - обратно
    }

    public void destroy() {
        this.filterConfig = null;
    }
}
