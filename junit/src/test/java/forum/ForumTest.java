package forum;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;

import org.fest.assertions.core.Condition;
import org.junit.Test;

/**
 * Uzupelnij klase @link{forum.Forum} tak, aby wszystkie testy sie zazielenily.
 * Za kazdym razem: 
 * -- wez na tapete tylko jeden, kolejny test. 
 * -- po zmianach sprawdz, ze poprzednie testy nie przestaly dzialac.
 * 
 * Tylko jeden z testow, z linijka TODO: wymaga uzupelnienia.
 * Pozostalych prosze nie modyfikwac.
 */
public class ForumTest {

    
    
    @Test
    public void forumShouldBeEmptyAfterCreation() {
        //given
        
        //when
        Forum forum = new Forum();
        
        //then
        assertThat(forum.allPosts()).isEmpty();
    }

    @Test
    public void itShouldBePossibleToAddANewPost() {
        //given
        Forum forum = aForum();
        Post post = aPost();
        
        //when
        forum.addPost(post);
        
        //then
        assertThat(forum.allPosts()).containsOnly(post);
    }
    
    @Test
    public void postsShouldBeListedDescendingByCreationDate() {
        //given
        Post post1 = aPost();
        Post post2 = aPost();
        Post post3 = aPost();
        Forum forum = aForumWith(post1, post2, post3);
        
        //when
        List<Post> allPosts = forum.allPosts();
        
        //then
        assertThat(allPosts).containsExactly(post3, post2, post1);
    }
    
    @Test
    public void postsShouldBeListedDescendingByCreationDate2() {
      //given
        Post post1 = aPost();
        Post post2 = aPost();
        Post post3 = aPost();
        Forum forum = aForumWith(post1, post2, post3);
        
        //when
        List<Post> allPosts = forum.allPosts();
        List<Post> allPosts2 = forum.allPosts();
        
        //then
        assertThat(allPosts).containsExactly(post3, post2, post1);
        assertThat(allPosts2).containsExactly(post3, post2, post1);
    }
    
    @Test
    public void itShouldBePossibleToEditAPost() {
        //given
        Post post1 = aPost();
        Forum forum = aForumWith(post1);
        Post newPost = aPost();
        
        //when
        forum.editPost(post1, newPost);
        
        //then
        assertThat(forum.allPosts()).containsExactly(newPost);
    }
    
    
    
    @Test(expected=IllegalArgumentException.class)
    public void itShouldNotBeAbleToEditAPostNotBelongingToTheForum() {
        //given
        Post post1 = aPost();
        Post aPostNotBelongingToTheForum = aPost();
        Post newPost = aPost();
        Forum forum = aForumWith(post1);
        
        //when
        //TODO: prosze uzupelnic!
        
        //then
        //spodziewamy sie IllegalArgumentException (zobacz atrybut "expected" w anotacji @Test) 
        
    }
    
    /**
     * zwroc uwage na metode noDuplicates();
     */
    @Test
    public void forumShouldHaveNoDuplicatedPosts() throws Exception {
        //given
        Forum forum = aForumWith(aPost("duplicatedContent"));
        
        //when
        forum.addPost(aPost("duplicatedContent"));

        //then
        assertThat(forum.allPosts()).has(noDuplicates());
    }
    
    // --
    
    private Condition<List<Post>> noDuplicates() {
        return new Condition<List<Post>>() {
            @Override
            public boolean matches(List<Post> posts) {
                return new HashSet<Post>(posts).size() == posts.size();
            }
            
            @Override
            public String toString() {
                return "no duplicates";
            }
            
        };
    }

    private Forum aForumWith(Post ...posts) {
        Forum forum = new Forum();
        for (Post post : posts) {
            forum.addPost(post);
        }
        return forum;
    }

    
    private int uniqueId;
    
    private Post aPost() {
        return new Post("content" + uniqueId++);
    }
    
    private Post aPost(String string) {
        return new Post(string);
    }

    private Forum aForum() {
        return new Forum();
    }
}
