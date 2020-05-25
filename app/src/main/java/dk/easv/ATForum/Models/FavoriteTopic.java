package dk.easv.ATForum.Models;

import java.util.List;

public class FavoriteTopic {
    private String id;

    private List<Topic> favoriteTopics;

    public FavoriteTopic() {
    }

    public FavoriteTopic(String id, List<Topic> topics) {
        this.id = id;
        this.favoriteTopics = topics;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Topic> getFavoriteTopics() {
        return favoriteTopics;
    }

    public void setFavoriteTopics(List<Topic> favoriteTopics) {
        this.favoriteTopics = favoriteTopics;
    }

    @Override
    public String toString() {
        return "FavoriteTopic{" +
                "id='" + id + '\'' +
                ", topics=" + favoriteTopics +
                '}';
    }
}
