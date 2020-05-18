package dk.easv.ATForum.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
    private String id;
    private String message;
    private User author;
    private String topicId;
    @ServerTimestamp Date timestamp;
    public Comment() {
    }

    public Comment(String message, User author) {
        this.message = message;
        this.author = author;
    }

    public Comment(String message, User author, String topicId) {
        this.message = message;
        this.author = author;
        this.topicId = topicId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", author=" + author +
                ", topicId='" + topicId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
