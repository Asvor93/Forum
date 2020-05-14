package dk.easv.ATForum.Models;

public class Topic {
    private String id;
    private String topicName;
    private String description;
    private User author;
    private String categoryId;
    private Comment[] comments;

    public Topic(String id, String topicName, String description, User author, String categoryId) {
        this.id = id;
        this.topicName = topicName;
        this.description = description;
        this.author = author;
        this.categoryId = categoryId;
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

    public Comment[] getComments() {
        return comments;
    }

    public void setComments(Comment[] comments) {
        this.comments = comments;
    }
}
