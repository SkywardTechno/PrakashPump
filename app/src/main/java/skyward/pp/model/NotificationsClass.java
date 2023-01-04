package skyward.pp.model;

/**
 * Created by ANDROID 1 on 09-05-2017.
 */
public class NotificationsClass {

    String Id;
    String Title;
    String Description;

    public NotificationsClass(String id, String title, String description) {
        Id = id;
        Title = title;
        Description = description;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
