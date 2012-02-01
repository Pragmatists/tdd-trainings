package tdd.pragmatists.movielist;

import javax.swing.JFrame;

import tdd.pragmatists.movielist.view.SwingMovieList;

@SuppressWarnings("serial")
public class MainApplicationFrame extends JFrame {

	public MainApplicationFrame(SwingMovieList swingMovieList) {
		super("Movie List");
		add(swingMovieList);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}
}
