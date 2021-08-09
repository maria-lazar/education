#pragma once
#include <string>
#include <functional>
#include <exception>
#include "Movie.h"
#include "Vector.h"
#include "MovieRepository.h"
using namespace domain;
using namespace std;

typedef const int(*Filter)(const Movie& movie, const void* arg);

typedef const int(*Cmp)(const Movie& m1, const Movie& m2);

class MovieService
{
private:
	MovieRepository& movieRepository;
public:
	/**
	* constructor
	*/
	MovieService(MovieRepository& mR) noexcept: movieRepository(mR) {}
	
	/**
	* destructor
	*/
	~MovieService() {}

	/**
	* Returns the current vector of Movies stored in movieRepository
	*/
	const myvector<Movie>& getAll() const noexcept;

	/**
	* Adds a Movie with the given properties
	* If such a Movie already exists, throws StorageException
	* If the Movie properties are invalid, throws ValidationException
	* return: the reference of the added Movie
	*/
	const Movie& create(const string& title, const string& genre, const int year, const string& actor);
	
	/**
	* Removes a Movie with the given properties
	* If such a Movie doesn't exist, throws StorageException
	* If the Movie properties are invalid, throws ValidationException
	* return: the removed Movie
	*/
	const Movie& remove(const string& title, const int year);

	/**
	* Modifies the Movie with the given properties
	* If such a Movie doesn't exist, throws StorageException
	* If the Movie properties are invalid, throws ValidationException
	* return: the old Movie
	*/
	const Movie modify(const string& title, const string& genre, const int year, const string& actor);
	
	/**
	* Searches for the Movie with the given properties
	* If such a Movie doesn't exist, throws StorageException
	* If the Movie properties are invalid, throws ValidationException
	* return: the reference of the found Movie
	*/
	const Movie& search(const string& title, const int year) const;
	
	/**
	* Filters the list of Movies by the given filter function
	* return: vector of found Movies
	*/
	myvector<Movie> filter(std::function<bool(const Movie &)> func) const;

	/**
	* Sorts the list of Movies by the given compare function
	* return: vector of sorted Movies
	*/
	myvector<Movie> sort(std::function<bool(const Movie&, const Movie&)>func) const;
};

