MovieList
=========

1. Zaimplementuj dodawanie filmu do listy filmow,
2. Zaimplementuj `JpaMovieRepository`,
3. Zaimplementuj usuwanie filmu z listy,
4. Zaimplementuj usuwanie filmu z bazy danych.


aby umożliwić pobranie zaznaczonego elementu z listy musimy:
w initLayout() dodać
movies.setSelectable(true);

a potem aby pobrać zaznaczony obiekt:

Movie rowId = (Movie) movies.getValue(); 
