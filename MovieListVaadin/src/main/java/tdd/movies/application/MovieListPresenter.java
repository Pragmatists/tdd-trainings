package tdd.movies.application;

import tdd.movies.domain.Movie;
import tdd.movies.domain.MovieRepository;

public class MovieListPresenter {

    private MovieListView view;
    private MovieRepository repository;
    
    public MovieListPresenter(MovieRepository repository, MovieListView view) {
        
        this.view = view;
        this.repository = repository;
    }

    public void onStart() {
        
        for (Movie movie : repository.loadAll()) {
            view.addMovieToList(movie);
        }
    }
    
    
}
