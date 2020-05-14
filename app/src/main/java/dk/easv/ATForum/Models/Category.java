package dk.easv.ATForum.Models;

import java.io.Serializable;
import java.util.Arrays;

public class Category implements Serializable {
    private String uid;
    private String categoryName;
    private String description;
    private Topic[] topic;

    public Category() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDescription() {
        return description;
    }

    public Topic[] getTopic() {
        return topic;
    }

    public void setTopic(Topic[] topic) {
        this.topic = topic;
    }

    public Category(String categoryName, String description) {
        this.categoryName = categoryName;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Category{" +
                "uid='" + uid + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", description='" + description + '\'' +
                ", topic=" + Arrays.toString(topic) +
                '}';
    }
}
