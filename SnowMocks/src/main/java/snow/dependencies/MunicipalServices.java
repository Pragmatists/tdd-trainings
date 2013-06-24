package snow.dependencies;


public interface MunicipalServices {
	void sendSnowplow() throws SnowplowMalfunctioningException;

	void sendSander();
}
