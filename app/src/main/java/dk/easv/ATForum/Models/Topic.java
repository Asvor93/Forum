package dk.easv.ATForum.Models;

import java.io.Serializable;
import java.util.List;

public class Topic implements Serializable {
    private String id;
    private String topicName;
    private String description;
    private User author;
    private String categoryId;
    private List<Comment> comments;

    public Topic(String id, String topicName, String description, User author, String categoryId) {
        this.id = id;
        this.topicName = topicName;
        this.description = description;
        this.author = author;
        this.categoryId = categoryId;
    }

    public Topic(String topicName, String description, User author, String categoryId) {
        this.topicName = topicName;
        this.description = description;
        this.author = author;
        this.categoryId = categoryId;
    }

    public Topic(String topicName, String description) {
        this.topicName = topicName;
        this.description = description;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Topic() {
    }

    public String getId() {
        return id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id='" + id + '\'' +
                ", topicName='" + topicName + '\'' +
                ", description='" + description + '\'' +
                ", author=" + author +
                ", categoryId='" + categoryId + '\'' +
                ", comments=" + comments +
                '}';
    }
}
