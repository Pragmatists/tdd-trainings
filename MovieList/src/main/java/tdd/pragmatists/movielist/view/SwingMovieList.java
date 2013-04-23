package tdd.pragmatists.movielist.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tdd.pragmatists.movielist.db.Movie;
import tdd.pragmatists.movielist.presenter.MovieListPresenter;

@SuppressWarnings("serial")
public class SwingMovieList extends JPanel implements MovieView {

    private final class DeleteAction extends AbstractAction {
        private DeleteAction() {
            super("Delete");
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private final class AddAction extends AbstractAction {
        private AddAction() {
            super("Add");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            movieListPresenter.add();
        }
    }

    private JList movies;
    private JButton add;
    private JButton delete;
    private MovieListModel moviesModel = new MovieListModel();
    private JTextField newMovieTitle;
    private MovieListPresenter movieListPresenter;

    public SwingMovieList() {
        createComponents();
        layoutComponents();
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        add(movies, BorderLayout.CENTER);
        JPanel controls = new JPanel(new BorderLayout());
        JPanel newMovieControls = new JPanel();
        newMovieControls.add(new JLabel("New Movie: "));
        newMovieControls.add(newMovieTitle);
        newMovieControls.add(add);
        controls.add(newMovieControls, BorderLayout.CENTER);
        controls.add(delete, BorderLayout.SOUTH);
        add(controls, BorderLayout.SOUTH);
    }

    private void createComponents() {
        movies = new JList(moviesModel);
        movies.setPreferredSize(new Dimension(100, 300));
        add = new JButton(new AddAction());
        add.setName("Add");
        delete = new JButton(new DeleteAction());
        delete.setName("Delete");
        newMovieTitle = new JTextField(26);
    }

    public void observeWith(MovieListPresenter movieListPresenter) {
        this.movieListPresenter = movieListPresenter;
    }

    @Override
    public String getNewTitle() {
        return newMovieTitle.getText();
    }

    @Override
    public void showMovies(List<Movie> movies) {
        moviesModel.update(movies);
    }

    @Override
    public Movie getSelectedMovie() {
        return (Movie) movies.getSelectedValue();
    }

}
