package tdd.mimuw.movielist;

import javax.swing.JFrame;

import tdd.mimuw.movielist.view.SwingMovieList;

@SuppressWarnings("serial")
public class MainApplicationFrame extends JFrame {

	public MainApplicationFrame(SwingMovieList swingMovieList) {
		super("Movie List");
		add(swingMovieList);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}
}
