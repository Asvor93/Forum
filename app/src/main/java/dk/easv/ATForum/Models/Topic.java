package dk.easv.ATForum.Models;

public class Topic {
    private String id;
    private String topicName;
    private String description;
    private User author;
    private String categoryId;
    private Comment[] comments;
}
