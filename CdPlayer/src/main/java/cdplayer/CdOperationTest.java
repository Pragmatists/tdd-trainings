package cdplayer;

import static org.junit.Assert.*;

import org.junit.*;

public class CdOperationTest {
	CdPlayer player = new CdPlayer();
	Cd sampleCd = new Cd("Нас не догонят");

	@Test
	public void shouldAcceptACdIfEmpty() throws Exception {

		player.insert(sampleCd);

		assertTrue(player.hasCdInside());
	}

	@Test
	public void isEmptyOnStart() throws Exception {
		assertFalse(player.hasCdInside());
	}

	@Test
	public void shouldNotAcceptAnotherCdIfOneIsInside() throws Exception {
		player.insert(sampleCd);
		Cd cd2 = new Cd("Белые розы");

		try {
			player.insert(cd2);
			fail();
		} catch (CdAlreadyInsideException e) {
			// ok
		}
	}

	@Test
	public void shouldEjectCdFromInside() throws Exception {
		player.insert(sampleCd);

		player.eject();

		assertFalse(player.hasCdInside());
	}

	@Test
	public void shouldNotEjectFromEmptyDrive() throws Exception {
		try {
			player.eject();
			fail();
		} catch (DriveEmptyException e) {
			// ok
		}
	}

}
