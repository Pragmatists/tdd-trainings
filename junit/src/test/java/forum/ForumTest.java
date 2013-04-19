package forum;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

/**
 * Prosze uzupelnic klase @link{forum.Forum} tak, aby wszystkie testy sie zazielenily.
 * W tej klasie mozna zmieniac tylko linie oznaczone przez "TODO:"
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
    public void userShouldBeAbleToAppendANewPost() {
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
    public void userShouldBeAbleToEditAPost() {
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
    public void userShouldNotBeAbleToEditAPostNotBelongingToTheForum() {
        //given
        Post post1 = aPost();
        Post aPostNotBelongingToTheForum = aPost();
        Post newPost = aPost();
        Forum forum = aForumWith(post1);
        
        //when
        forum.editPost(aPostNotBelongingToTheForum, newPost);
        //TODO: prosze uzupelnic!
        
        //then
        //spodziewamy sie IllegalArgumentException (zobacz atrybut "expected" w anotacji @Test) 
        
    }
    
    // --

    private Forum aForumWith(Post ...posts) {
        Forum forum = new Forum();
        for (Post post : posts) {
            forum.addPost(post);
        }
        return forum;
    }

    
    
    private Post aPost() {
        return new Post("someContent");
    }

    private Forum aForum() {
        return new Forum();
    }
}