#pragma once
#include "Movie.h"
#include "Vector.h"
using namespace std;
using namespace domain;

class MovieRepository
{
private:
	myvector<Movie> movies;
public:
	/**
	* Constructor
	*/
 	MovieRepository() noexcept{}
	
	/**
	* Destructor
	*/
	~MovieRepository() {}
	
	/**
	* Returns the current vector of Movies stored
	*/
	const myvector<Movie>& findAll() const noexcept;

	/**
	* Adds a Movie to the vector of Movies
	* return: the added Movie
	*/
	const Movie& save(const Movie& movie);

	/**
	* Removes a Moviefrom the current vector
	* return: the removed Movie
	*/
	const Movie& remove(const int index);
	
	/**
	* Modifies a Movie
	* return: the old Movie
	*/
	const Movie update(const int index, const string& genre, const string& actor);
	
	/**
	* Returns the Movie at the given index
	* return: the found Movie
	*/
	const Movie& find(const int index) const;
	
	/**
	* Returns the index of the Movie with the given year and title
	* return: -1, if there isn't one
	*/
	const int findByTitleYear(const string& title, const int year);
};

