package search;

import java.util.ArrayList;
import java.util.List;

class Finder {

    private List<Post> posts;

    public Finder(List<Post> listOfPosts) {
        this.posts = listOfPosts;
    }

    public Post findFirst(String string) {
        return posts.get(0);
    }

    public List<Post> findAll(String string) {
        List<Post> result = new ArrayList<Post>();
        for (Post post : posts) {
            if (!post.containsText(string)) {
                result.add(post);
            }
        }
        return result;
    }

}
