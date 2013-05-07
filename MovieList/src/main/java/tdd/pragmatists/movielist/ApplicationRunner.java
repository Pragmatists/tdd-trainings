package tdd.pragmatists.movielist;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import tdd.pragmatists.movielist.db.MovieRepository;
import tdd.pragmatists.movielist.presenter.MovieListPresenter;
import tdd.pragmatists.movielist.view.SwingMovieList;

public class ApplicationRunner {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("real-services.xml");
		
		final SwingMovieList swingMovieList = new SwingMovieList();
		MovieListPresenter movieListPresenter = new MovieListPresenter(
				swingMovieList, context.getBean(MovieRepository.class));
		swingMovieList.observeWith(movieListPresenter);

		JFrame frame = new MainApplicationFrame(swingMovieList);
		frame.addComponentListener ( new ComponentAdapter () {
        	
            @Override
			public void componentShown ( ComponentEvent e ) {
                swingMovieList.shown();
            }
        });
		frame.setVisible(true);
	}
}
