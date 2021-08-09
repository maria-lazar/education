#pragma once
#include <vector>
#include <algorithm>
#include <fstream>
#include "Movie.h"
#include "MovieRepository.h"
using namespace domain;
using namespace std;

class ShoppingRepository
{
private:
	std::vector<Movie> shoppingBag;
	MovieRepository& movieRepository;

public:
	ShoppingRepository(MovieRepository& mR) noexcept : movieRepository{ mR } {};

	int findMovie(const string& title);

	void addToBag(const Movie& m);

	int getSize() noexcept{	return shoppingBag.size();}

	const vector<Movie>& findAll() noexcept{ return shoppingBag; }

	void remove(const string& title);

	void update(const string& title, const string& genre, const string& actor);

	void clear() noexcept;
	
    void replace(vector<Movie> v);

	size_t findIndexInRepository(string title);

	virtual void saveBag() = 0;

	~ShoppingRepository();
};

class ShoppingRepositoryF : public ShoppingRepository {
private:

	string file;
	void readFromFile();
	void writeToFile();

public:
	ShoppingRepositoryF(MovieRepository& mR, string f) : ShoppingRepository(mR), file{ f } {
		readFromFile();
		writeToFile();
	};
	void saveBag();
};

