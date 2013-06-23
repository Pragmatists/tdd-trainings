package tdd.movies.domain;


public class Movie {

    private String title;
    private String director;
    private int year;

    private Movie() {
    }
    
    public Movie(String title, String director, int year) {
        this();
        this.title = title;
        this.director = director;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return String.format("'%s' (%s) by %s", title, year, director);
    }
    
}
