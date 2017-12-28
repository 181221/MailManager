public class Mail {

    private String til;
    private String fra;
    private String msg;
    private String subject;
    private String result;

    public Mail(String til, String fra, String msg, String subject) {
        this.til = til;
        this.fra = fra;
        this.msg = msg;
        this.subject = subject;
        result = "";
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTil() {
        return til;
    }

    public void setTil(String til) {
        this.til = til;
    }

    public String getFra() {
        return fra;
    }

    public void setFra(String fra) {
        this.fra = fra;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
