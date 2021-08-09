#include "MovieService.h"
#include <exception>
#include <functional>

const vector<Movie>& MovieService::getAll() const noexcept{
	return movieRepository.findAll();
}

const vector<Movie>& MovieService::getAllShopping() const noexcept {
	return shoppingRepository.findAll();
}

const Movie& MovieService::create(const string& title, const string& genre, int year, const string& actor) {
	MovieValidator movieValidator;
	movieValidator.validateTitle(title);
	movieValidator.validateGenre(genre);
	movieValidator.validateActor(actor);
	movieValidator.validateYear(year);
	const int index = movieRepository.findByTitleYear(title, year);
	if (index != -1) {
		throw StorageException(string("Movie already exists\n"));
	}
	Movie movie(title, genre, year, actor);
	return movieRepository.save(movie);
}

const Movie MovieService::remove(const string& title, const int year) {
	MovieValidator movieValidator;
	movieValidator.validateTitle(title);
	movieValidator.validateYear(year);
	const int index = movieRepository.findByTitleYear(title, year);
	if (index == -1) {
		throw StorageException(string("No such movie found\n"));
	}
	shoppingRepository.remove(title);
	notify();
	//shoppingRepository.saveBag();
	return movieRepository.remove(index);
}

const Movie MovieService::modify(const string& title, const string& genre, const int year, const string& actor) {
	MovieValidator movieValidator;
	movieValidator.validateTitle(title);
	movieValidator.validateGenre(genre);
	movieValidator.validateActor(actor);
	movieValidator.validateYear(year);
	const int index = movieRepository.findByTitleYear(title, year);
	if (index == -1) {
		throw StorageException(string("No such movie found\n"));
	}
	shoppingRepository.update(title, genre, actor);
	//shoppingRepository.saveBag();
	notify();
	return movieRepository.update(index, genre, actor);
}

const Movie& MovieService::search(const string& title, const int year) const {
	MovieValidator movieValidator;
	movieValidator.validateTitle(title);
	movieValidator.validateYear(year);
	const int index = movieRepository.findByTitleYear(title, year);
	if (index == -1) {
		throw StorageException(string("No such movie found\n"));
	}
	return movieRepository.find(index);
}

vector<Movie> MovieService::filter(vector<Movie> movies, std::function<bool(const Movie &)> func) const {
//	const vector<Movie>& movies = movieRepository.findAll();
	vector<Movie> filteredMovies;
	std::copy_if(movies.begin(), movies.end(), std::back_inserter(filteredMovies), func);
	return filteredMovies;
}

const vector<Movie> MovieService::sort(vector<Movie> movies, std::function<bool(const Movie&, const Movie&)>cmp) const {
	//vector<Movie> sortedMovies = movieRepository.findAll();
	std::sort(movies.begin(), movies.end(), cmp);
	return movies;
}

void MovieService::addMovieBag(string& title) {
	MovieValidator movieValidator;
	movieValidator.validateTitle(title);
	const int indexRepo = movieRepository.findByTitle(title);
	if (indexRepo == -1) {
		throw StorageException(string("No such movie found\n"));
	}
	const int indexShopping = shoppingRepository.findMovie(title);
	if (indexShopping != -1) {
		throw StorageException(string("Movie already added to shopping bag"));
	}
	const vector<Movie>& v = movieRepository.findAll();
	Movie m = v.at(indexRepo);
	shoppingRepository.addToBag(m);
	notify();
}

const int MovieService::getSizeShoppingBag() const noexcept{
	return shoppingRepository.getSize();
}

void MovieService::clearBag() noexcept{
	shoppingRepository.clear();
	notify();
}

void MovieService::generateBag(size_t number) {
	vector<Movie> v = movieRepository.getRandomMovies(number);
	shoppingRepository.replace(v);
	notify();
}

vector<MovieDTO> MovieService::getMap() {
	vector<Movie> movies = movieRepository.findAll();
	map<string, int> map;
	for_each(movies.begin(), movies.end(), [&map](const auto& m) {
		string g = m.getGenre();
		if (map.find(g) == map.end()){
			map[g] = 1;
		}
		else {
			map[g]++;
		}
	});
	vector<MovieDTO> v;
	for_each(map.begin(), map.end(), [&v](const auto& e) {
		MovieDTO m(e.first, e.second);
		v.push_back(m);
	});
	return v;
}

void MovieService::saveBag() {
	shoppingRepository.saveBag();
}

