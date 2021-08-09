#pragma once
#include <string>
#include <functional>
#include <map>
#include <vector>
#include "Movie.h"
#include "MovieRepository.h"
#include "ShoppingRepository.h"
#include "observer.h"
using namespace domain;
using namespace std;

class MovieService: public Observable
{
private:
	PureAbstractRepository& movieRepository;
	ShoppingRepository& shoppingRepository;

public:
	/**
	* constructor
	*/
	MovieService(MovieRepository& mR, ShoppingRepository& sR) noexcept : movieRepository(mR), shoppingRepository(sR) {
		currentList = movieRepository.findAll();
	}

	void selectionChanged(int n) {
		notify2(n);
	}

	vector<Movie> currentList;

	/**
	* destructor
	*/
	~MovieService() {}

	/**
	* Returns the current vector of Movies stored in movieRepository
	*/
	const vector<Movie>& getAll() const noexcept;


	const vector<Movie>& getAllShopping() const noexcept;

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
	const Movie remove(const string& title, const int year);

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
	vector<Movie> filter(vector<Movie> movies, std::function<bool(const Movie &)> func) const;

	/**
	* Sorts the list of Movies by the given compare function
	* return: vector of sorted Movies
	*/
	const vector<Movie> sort(vector<Movie> movies, std::function<bool(const Movie&, const Movie&)>func) const;

	/**
	* Returns the size of the shopping bag
	* return: int
	*/
	const int getSizeShoppingBag() const noexcept;

	/**
	* Adds the movie with the given title in the shopping bag if it exists
	* Throws ValidationException if the title is not valid
	* Throws StorageException if the movie is already in the shopping bag or if it doesn't exist in the repository
	*/
	void addMovieBag(string& title);

	/**
	* Clears the shopping bag
	*/
	void clearBag() noexcept;

	/**
	* Adds the given number of random movies to the shopping bag
	* Throws StorageException if there aren't enough movies
	*/
	void generateBag(size_t number);

	vector<MovieDTO> getMap();

	void saveBag();

};

