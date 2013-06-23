package tdd.movies.infrastructure;


import tdd.movies.application.MovieListPresenter;
import tdd.movies.application.MovieListView;
import tdd.movies.domain.Movie;
import tdd.movies.domain.MovieRepository;

import com.vaadin.annotations.Title;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Title("MovieList")
public class VaadinMovieListView extends UI implements MovieListView{

    private static final long serialVersionUID = -7030484106124270025L;

    private Table movies = new Table();

    private TextField title = new TextField("Title");
    private TextField directors = new TextField("Directors");
    private TextField year = new TextField("Year");
    
    private Button add = new Button("Add");
    
    private MovieListPresenter presenter;
    
    @Override
    protected void init(VaadinRequest request) {
        
        initLayout();
        
        presenter = new MovieListPresenter(new SpringInjector().getBean(MovieRepository.class), this);
        presenter.onStart();
    }

    private void initLayout() {
        
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        title.setWidth("300px");
        directors.setWidth("300px");
        year.setWidth("100px");
        layout.addComponent(new FormLayout(title, directors, year, add));
        layout.addComponent(buildMoviesTable());
        
        setContent(layout);
    }

    private Component buildMoviesTable() {
        
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("Title", String.class, "");
        container.addContainerProperty("Directors", String.class, "");
        container.addContainerProperty("Year", Integer.class, 0);
        
        movies.setContainerDataSource(container);
        movies.setVisibleColumns(new String[]{"Title", "Directors", "Year"});
        
        return movies;
    }

    @Override
    public void addMovieToList(Movie movie) {
        movies.addItem(new Object[]{ movie.getTitle(), movie.getDirector(), movie.getYear() }, movie);
    }
    
}
