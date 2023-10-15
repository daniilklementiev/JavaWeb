package step.learning.dto.models;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.InvalidFileNameException;
import step.learning.services.formparse.FormParseResult;
import sun.java2d.pipe.SpanShapeRenderer;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class SignupFormModel {
    private static final SimpleDateFormat formDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    //    public SignupFormModel(HttpServletRequest request) throws ParseException {
    //        this.setLogin(request.getParameter("reg-login"));
    //        this.setName(request.getParameter("reg-name"));
    //        this.setPassword(request.getParameter("reg-password"));
    //        this.setRepeat(request.getParameter("reg-repeat"));
    //        this.setEmail(request.getParameter("reg-email"));
    //        this.setBirthdate(request.getParameter("reg-birthdate"));
    //        this.setIsAgree(request.getParameter("reg-agree"));
    //    }

    public SignupFormModel(FormParseResult formParseResult) throws ParseException {
        Map<String, String> fields = formParseResult.getFields();
        this.setLogin(fields.get("reg-login"));
        this.setName(fields.get("reg-name"));
        this.setPassword(fields.get("reg-password"));
        this.setRepeat(fields.get("reg-repeat"));
        this.setEmail(fields.get("reg-email"));
        this.setBirthdate(fields.get("reg-birthdate"));
        this.setIsAgree(fields.get("reg-agree"));

        this.setAvatar(formParseResult);
    }

    /*
    * Валидация каждого поля и формирование уведомления про ошибки.
    * Пустой ответ означает успешную валидацию
    * @return словарь "имя поля" - "уведомление об ошибке"
    * */
    public Map<String, String> getValidationErrorMessages(){
        Map<String, String> result = new HashMap<>();
        if(login == null || login.isEmpty()) {
            result.put("login", "signup_login_empty");
        }
        else if(login.length() < 2 ) {
            result.put("login", "signup_login_too_short");
        }
        else if( ! Pattern.matches( "^[a-zA-Z0-9_-]+$", login ) ) {
            result.put("login", "signup_login_pattern_mismatch");

        }

        if(name == null || name.isEmpty()) {
            result.put("name", "Name is required");
        }
        if(password == null || password.isEmpty()) {
            result.put("password", "Password is required");
        }
        if(repeat == null || repeat.isEmpty()) {
            result.put("repeat", "Repeat password is required");
        }
        assert repeat != null;
        if(repeat.equals(password)) {
            result.put("repeat", "Passwords must be equal");
        }
        if(email == null || email.isEmpty()) {
            result.put("email", "Email is required");
        }
        if(birthDate == null) {
            result.put("birthdate", "Birthdate is required");
        }
        if(isAgree == null || !isAgree) {
            result.put("agree", "You must agree with terms");
        }

        return result;
    }

    // region fields
    private String login;
    private String name;
    private String password;
    private String repeat;
    private String email;
    private Date birthDate;
    private Boolean isAgree;
    private String avatar; // filename or url

    // endregion

    // region accessors
    public void setAvatar(FormParseResult formParseResult) {
        Map<String, FileItem> files = formParseResult.getFiles();
        if(! files.containsKey("reg-avatar")) {
            this.avatar = null;
            return;
        }
        FileItem fileItem = files.get("reg-avatar");
        String uploadedFilename = fileItem.getName();
        int dotIndex = uploadedFilename.lastIndexOf('.');
        String ext = uploadedFilename.substring(dotIndex);
        String[] extensions = {".jpg", ".jpeg", ".png", ".ico", ".gif"}; // valid extensions
        if(!Arrays.asList(extensions).contains(ext)){
             // throw exception if extension is invalid
            throw new RuntimeException("Invalid file extension");
        }
        // generate file dir
        String uploadDir = formParseResult.getRequest().getServletContext().getRealPath("./upload/avatar/");
        // generate random file name
        String savedFilename;
        File savedFile;
        do {
            savedFilename = UUID.randomUUID().toString().substring(0, 8) + ext;
            savedFile = new File(uploadDir, savedFilename);
        } while( savedFile.exists() );
        try {
            fileItem.write(savedFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.setAvatar(savedFilename);
    }
    public void setBirthdate(String birthdate) throws ParseException {
        if(birthdate == null || birthdate.isEmpty()) {
            this.birthDate = null;
        }
        else {
            setBirthDate(formDateFormat.parse(birthdate));
        }
    }

    public void setIsAgree(String input) {
        this.setAgree(
        "on".equalsIgnoreCase(input) || "true".equalsIgnoreCase(input)
        );
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Boolean getAgree() {
        return isAgree;
    }

    public void setAgree(Boolean agree) {
        isAgree = agree;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    // endregion
}
