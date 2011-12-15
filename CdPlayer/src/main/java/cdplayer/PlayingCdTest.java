package cdplayer;

import static org.junit.Assert.*;

import org.junit.*;

public class PlayingCdTest {

	@Test
	public void shouldNotPlayIfNoCd() throws Exception {
		CdPlayer player = new CdPlayer();
		try {
			player.play();
			fail();
		} catch (DriveEmptyException e) {
			// ok
		}
	}
}
