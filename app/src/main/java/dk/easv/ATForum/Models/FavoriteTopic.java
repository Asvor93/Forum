package dk.easv.ATForum.Models;

import java.util.List;

public class FavoriteTopic {
    private String uid;

    private List<Topic> favoriteTopics;

    public FavoriteTopic() {
    }

    public FavoriteTopic(String uid, List<Topic> topics) {
        this.uid = uid;
        this.favoriteTopics = topics;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
                "id='" + uid + '\'' +
                ", topics=" + favoriteTopics +
                '}';
    }
}
