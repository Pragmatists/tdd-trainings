package search;

import java.util.ArrayList;
import java.util.List;

class Finder {

    private List<Post> posts;

    public Finder(List<Post> listOfPosts) {
        this.posts = listOfPosts;
    }

    /*
     * znajduje pierwszy z brzegu post, ktory zawiera tresc "content"
     */
    public Post findFirst(String content) {
        return posts.get(0);
    }
    
    /*
     * znajduje wszystkie posty, ktory zawieraja tresc "content"
     */

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
