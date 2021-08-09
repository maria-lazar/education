#pragma once
#include "MovieService.h"

const int filterByTitle(const Movie& m, const void* arg);
const int filterByYear(const Movie& m, const void* arg) noexcept;
const int cmpByTitle(const Movie& m1, const Movie& m2);
const int cmpByActor(const Movie& m1, const Movie& m2);
const int cmpByYearGenre(const Movie& m1, const Movie& m2);

class MovieUi
{
private:
	MovieService& movieService;

	void menu();
public:

	MovieUi(MovieService& m) noexcept:movieService(m) {}
	~MovieUi() {}
	
	void printMovies(const myvector<Movie>& movies);
	
	void printAll();

	void addMovie();

	void deleteMovie();

	void updateMovie();

	void searchMovie();

	//const int filterByTitle(Movie& m, void* arg);

	void filterYear();
	
	void filterTitle();
	
	void sortTitle();

	void sortActor();

	void sortYearGenre();

	void run();
};

