package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dto.models.SignupFormModel;
import step.learning.services.culture.ResourceProvider;
import step.learning.services.formparse.FormParseResult;
import step.learning.services.formparse.FormParseService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class SignupServlet extends HttpServlet {
    private final ResourceProvider resourceProvider;
    private final FormParseService fromParseService;

    @Inject
    public SignupServlet(ResourceProvider resourceProvider, FormParseService fromParseService) {
        this.resourceProvider = resourceProvider;
        this.fromParseService = fromParseService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer regStatus = (Integer) session.getAttribute("reg-status");
        if(regStatus != null) {
            session.removeAttribute("reg-status");
            req.setAttribute("reg-data", regStatus.toString());
            if(regStatus == 2) {
                SignupFormModel formModel = (SignupFormModel) session.getAttribute("reg-model");
                req.setAttribute("reg-model", session.getAttribute("reg-data"));
                Map<String, String> validationErrors = formModel == null ? new HashMap<String, String>() : formModel.getValidationErrorMessages();
                for(String key : validationErrors.keySet()) {
                   //  req.setAttribute("reg-" + key + "-error", resourceProvider.getString(validationErrors.get(key)));
                    validationErrors.put(key, resourceProvider.getString(validationErrors.get(key)));
                }
                req.setAttribute("validationErrors", validationErrors);
            }
        }
        req.setAttribute("page-body", "signup.jsp");
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        // trying to create a model
        SignupFormModel formModel;
        try {
            FormParseResult formParseResult = fromParseService.parse(req);
            formModel = new SignupFormModel(formParseResult);
            session.setAttribute("reg-status", 2);
            session.setAttribute("reg-model", formModel);
        }
        catch (ParseException e) {
            // log message
            session.setAttribute("reg-status", 1);
        }
        resp.sendRedirect(req.getRequestURI());
    }
}
