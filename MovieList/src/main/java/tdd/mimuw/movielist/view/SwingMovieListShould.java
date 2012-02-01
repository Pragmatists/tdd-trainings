package tdd.mimuw.movielist.view;

import static org.mockito.Mockito.*;

import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.testing.FestSwingTestCaseTemplate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tdd.mimuw.movielist.MainApplicationFrame;

public class SwingMovieListShould extends FestSwingTestCaseTemplate {

	private FrameFixture window;
	private GuiEventObserver guiEventObserver = mock(GuiEventObserver.class);

	@Before
	public void onSetUp() {
		setUpRobot();
		MainApplicationFrame frame = createMainApplicationFramInSwingThread();
		window = new FrameFixture(robot(), frame);
		window.show();
	}

	private MainApplicationFrame createMainApplicationFramInSwingThread() {
		MainApplicationFrame frame = GuiActionRunner
				.execute(new GuiQuery<MainApplicationFrame>() {
					protected MainApplicationFrame executeInEDT() {
						return new MainApplicationFrame(createSwingMovieList());
					}
				});
		return frame;
	}

	private SwingMovieList createSwingMovieList() {
		SwingMovieList swingMovieList = new SwingMovieList();
		swingMovieList.observeWith(guiEventObserver);
		return swingMovieList;
	}

	@After
	public void after() {
		cleanUp();
	}

	@Test
	public void notifyObserverOnAdd() throws Exception {
		window.button("Add").click();

		verify(guiEventObserver).add();
	}

}
