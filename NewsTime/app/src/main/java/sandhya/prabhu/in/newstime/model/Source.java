package sandhya.prabhu.in.newstime.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Source implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public Source(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
