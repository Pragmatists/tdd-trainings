package snow;

public interface MunicipalServices {
	void sendSnowplow() throws SnowplowMalfunctioningException;

	void sendSander();
}
