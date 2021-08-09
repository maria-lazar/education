#pragma once
#include "MovieService.h"
#include "Change.h"

class MovieUi
{
private:
	MovieService& movieService;
	ProbabilityRepository& probRepo;
	vector<std::unique_ptr<Change>> prevChanges;
	vector<Change*> nextChanges;
	void menu();

public:

	MovieUi(MovieService& m, ProbabilityRepository& pR) noexcept: movieService(m), probRepo(pR) {}
	~MovieUi() {
		/*
		for (size_t i = 0; i < prevChanges.size(); i++) {
			delete prevChanges.at(i);
		}
		for (size_t i = 0; i < nextChanges.size(); i++) {
			delete nextChanges.at(i);
		}*/
	}
	
	void printMovies(const vector<Movie>& movies);
	
	void printAll();

	void addMovie();

	void deleteMovie();

	void updateMovie();

	void searchMovie();

	void filterYear();
	
	void filterTitle();
	
	void sortTitle();

	void sortActor();

	void sortYearGenre();

	void addMovieToBag();

	void clearShoppingBag();

	void generateShoppingBag();

	void getMap();

	void saveShoppingBag();

	void undo();
	
	void redo();
	
	void prob();

	void run();
};

