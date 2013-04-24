package fileinfo;

import org.junit.runners.Suite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({FileInfoInCommandLineModeTest.class, FileInfoInConsoleModeTest.class, FileInfoInGuiModeTest.class})
public class FileInfoTest {

}
