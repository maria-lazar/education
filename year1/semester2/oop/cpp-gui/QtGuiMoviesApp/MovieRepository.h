#pragma once
#include <vector>
#include <algorithm>
#include <fstream>
#include <map>
#include <stdlib.h>
#include "Movie.h"
#include "Exceptions.h"
using namespace std;
using namespace domain;

class PureAbstractRepository {
public:
	virtual const vector<Movie>& findAll() const = 0;

	virtual const Movie save(const Movie& movie) = 0;

	virtual const Movie remove(const int index) = 0;

	virtual const Movie update(const int index, const string& genre, const string& actor) = 0;

	virtual const Movie find(const int index) const = 0;

	virtual const int findByTitleYear(const string& title, const int year) = 0;

	virtual const int findByTitle(const string& title) = 0;

	virtual vector<Movie> getRandomMovies(size_t number) = 0;

	virtual ~PureAbstractRepository() {}
};

class MovieRepository: public PureAbstractRepository
{
private:
	vector<Movie> movies;

public:
	/**
* Constructor
*/
	MovieRepository() noexcept {}

	/**
	* Destructor
	*/
	~MovieRepository() {}

	/**
	* Returns the current vector of Movies stored
	*/
	const vector<Movie>& findAll() const noexcept;

	/**
	* Adds a Movie to the vector of Movies
	* return: the added Movie
	*/
	virtual const Movie save(const Movie& movie);

	/**
	* Removes a Moviefrom the current vector
	* return: the removed Movie
	*/
	virtual const Movie remove(const int index);

	/**
	* Modifies a Movie
	* return: the old Movie
	*/
	virtual const Movie update(const int index, const string& genre, const string& actor);

	/**
	* Returns the Movie at the given index
	* return: the found Movie
	*/
	const Movie find(const int index) const;

	/**
	* Returns the index of the Movie with the given year and title
	* return: -1, if there isn't one
	*/
	const int findByTitleYear(const string& title, const int year);

	/**
	* Returns the index of the Movie with the given title
	* return: -1, if there isn't one
	*/
	const int findByTitle(const string& title);

	/**
	* Generates a vector with the given number of random movies
	* Throws StorageException if there aren't enough movies
	*/
	vector<Movie> getRandomMovies(size_t number);
};

class MovieRepositoryF: public MovieRepository {
private:
	string file;
	void readFromFile();
	void writeToFile();
	

public:
	MovieRepositoryF(string f): MovieRepository(), file{ f }{
		readFromFile();
	};
	
	const Movie save(const Movie& movie) override;

	const Movie remove(const int index) override;

	const Movie update(const int index, const string& genre, const string& actor) override;
};

class ProbabilityRepository : PureAbstractRepository {
private:
	float probability;
public:
	ProbabilityRepository(float p) : probability{ p } {}

	const Movie save(const Movie& movie);
	
	const Movie remove(const int index);

	const Movie update(const int index, const string& genre, const string& actor);

	const vector<Movie>& findAll() const noexcept;

	const Movie find(const int index) const;

	const int findByTitleYear(const string& title, const int year);

	const int findByTitle(const string& title);

	vector<Movie> getRandomMovies(size_t number);
};
