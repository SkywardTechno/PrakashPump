package skyward.pp.model;

/**
 * Created by ADNROID 2 on 24-10-2016.
 */
public class AdminMultipleListClass {

    String literature_list;
    String literature_name;
    String literature_path;

    public AdminMultipleListClass(String literature_list, String literature_name, String literature_path) {
        this.literature_list = literature_list;
        this.literature_name = literature_name;
        this.literature_path = literature_path;
    }

    public String getLiterature_list() {
        return literature_list;
    }

    public void setLiterature_list(String literature_list) {
        this.literature_list = literature_list;
    }

    public String getLiterature_name() {
        return literature_name;
    }

    public void setLiterature_name(String literature_name) {
        this.literature_name = literature_name;
    }

    public String getLiterature_path() {
        return literature_path;
    }

    public void setLiterature_path(String literature_path) {
        this.literature_path = literature_path;
    }
}
