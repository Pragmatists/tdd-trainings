package cdplayer;

public class CdPlayer {

	private Cd cd;

	public void insert(Cd cd) throws CdAlreadyInsideException {
		if (this.cd != null)
			throw new CdAlreadyInsideException();

		this.cd = cd;
	}

	public boolean hasCdInside() {
		return cd != null;
	}

	public void eject() throws DriveEmptyException {
		if (cd == null)
			throw new DriveEmptyException();

		cd = null;
	}

	public void play() throws DriveEmptyException {
		throw new DriveEmptyException();
	}
}
