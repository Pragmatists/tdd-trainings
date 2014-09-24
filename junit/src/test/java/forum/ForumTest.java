package forum;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Prosze uzupelnic klase @link{forum.Forum} tak, aby wszystkie testy sie
 * zazielenily. W tej klasie mozna zmieniac tylko linie oznaczone przez "TODO:"
 */
public class ForumTest {

	@Test
	public void forumShouldBeEmptyAfterCreation() {
		// given

		// when
		Forum forum = new Forum();

		// then
		assertThat(forum.allPosts()).isEmpty();
	}

    @Ignore
	@Test
	public void userShouldBeAbleToAppendANewPost() {
		// given
		Forum forum = aForum();
		Post post = aPost();

		// when
		forum.addPost(post);

		// then
		assertThat(forum.allPosts()).containsOnly(post);
	}

    @Ignore
	@Test
	public void postsShouldBeListedDescendingByCreationDate() {
		// given
		Post post1 = aPost("1");
		Post post2 = aPost("2");
		Post post3 = aPost("3");
		Forum forum = aForumWith(post1, post2, post3);

		// when
		List<Post> allPosts = forum.allPosts();

		// then
		assertThat(allPosts).containsExactly(post3, post2, post1);
	}

    @Ignore
	@Test
	public void userShouldBeAbleToEditAPost() {
		// given
		Post post1 = aPost("A");
		Forum forum = aForumWith(post1);
		Post newPost = aPost("B");

		// when
		forum.editPost(post1, newPost);

		// then
		assertThat(forum.allPosts()).containsExactly(newPost);
	}

    @Ignore
	@Test(expected = IllegalArgumentException.class)
	public void userShouldNotBeAbleToEditAPostNotBelongingToTheForum() {
		// given
		Post post1 = aPost("A");
		Post aPostNotBelongingToTheForum = aPost("B");
		Post newPost = aPost("C");
		Forum forum = aForumWith(post1);

		// when
		forum.editPost(aPostNotBelongingToTheForum, newPost);
		// TODO: prosze uzupelnic!

		// then
		// spodziewamy sie IllegalArgumentException (zobacz atrybut "expected" w
		// anotacji @Test)

	}

	// TODO: przepisz powyższy test na dwa sposoby:
	// : - przy użyciu @Rule ExpectedException expectedException
	// : - przy użyciu biblioteki CatchException
	// w obu tych sposobach sprawdź nie tylko typ wyjątku ale również wiadomość
	// przykłady uzycia ww. sposobów znajdziesz w
	// exceptions.ExamplesForExceptionsTest

	// --

	private Forum aForumWith(Post... posts) {
		Forum forum = new Forum();
		for (Post post : posts) {
			forum.addPost(post);
		}
		return forum;
	}

	private Post aPost() {
		return aPost("someContent");
	}

	private Post aPost(String content) {
		return new Post(content);
	}

	private Forum aForum() {
		return new Forum();
	}
}