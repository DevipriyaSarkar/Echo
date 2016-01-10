package echo.echo.echo;

/**
 * Created by Devipriya on 09-Jan-16.
 */
public class AlarmItems {
    public String event;  //call, wifi
    public String remind;  //text to remind
    public int check;  //checks if on
    public String detail;  //detail explanation
    public int id;  //unique key
    public String key; //person's name / wifi SSID

    public AlarmItems() {
        this.event = "Incoming Call";
        this.remind = "Label";
        this.check = 0;
        this.detail = "Detail";
        this.id = 0;
        this.key = "1";
    }

    public AlarmItems(int id, String event, String remind, String key,int check) {
        this.event = event;
        this.remind = remind;
        this.check = check;
        this.detail = event+" : "+key;
        this.id = id;
        this.key=event;
    }

    public String getEvent() {
        return event;
    }

    public String getRemind() {
        return remind;
    }

    public int isCheck() {
        return check;
    }

    public String getDetail() {
        return detail;
    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
