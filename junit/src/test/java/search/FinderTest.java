package search;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Uzywajac wiedzy wyniesionej z klasy ForumTest, zupelnij testy tak, aby wykryc bledy w klasie Finder.
 * Dopisz wlasne testy sprawdzajace warunki brzegowe (np. nulle, puste listy, itp.).
 * W miare znajdowania bledow, poprawiaj klase Finder.
 * 
 * Dla natchnienia do pisania testow zerknij np. na http://pl.wikiquote.org/wiki/Shrek
 * 
 */
public class FinderTest {

    private Finder finder;
    private List<Post> listOfPosts;

    

    @Before
    public void setUp() {
        listOfPosts = new ArrayList<Post>();
        finder = new Finder(listOfPosts);
    }

    @Test
    public void shouldFindPostContaingText() throws Exception {
        // given
        Post post = new Post("A jak zobaczysz długi tunel, to nie idź w stronę światła!");
        givenPosts(post);

        // when
        Post postFound = finder.findFirst("jak zobaczysz");

        // then
        assertThat(postFound).isEqualTo(post);
    }

    @Test
    @Ignore
    public void shouldFindOnlyPostsContainingGivenText() throws Exception {
        // given
        String text = "gada";
        Post postContainingGivenText = new Post("On gada!");
        Post postNotContainingGivenText = new Post("No! Ale to tylko jedna z jego wad!");
        givenPosts(postContainingGivenText, postNotContainingGivenText);

        // when
        //TODO:
        
        //then
        //TODO:
    }

    @Test
    @Ignore
    public void shouldFindAllPostsContainingText() throws Exception {
        // given
        //TODO:

        // when
        finder.findAll("zrobię jajecznicę");

        // then
        //TODO:
    }
    

    private void givenPosts(Post... posts) {
        listOfPosts.addAll(Arrays.asList(posts));
    }
}
