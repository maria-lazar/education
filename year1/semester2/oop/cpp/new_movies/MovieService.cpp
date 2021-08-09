#include "MovieService.h"

const myvector<Movie>& MovieService::getAll() const noexcept{
	return movieRepository.findAll();
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

const Movie& MovieService::remove(const string& title, const int year) {
	MovieValidator movieValidator;
	movieValidator.validateTitle(title);
	movieValidator.validateYear(year);
	const int index = movieRepository.findByTitleYear(title, year);
	if (index == -1) {
		throw StorageException(string("No such movie found\n"));
	}
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

myvector<Movie> MovieService::filter(std::function<bool(const Movie &)> func) const {
	const myvector<Movie>& movies = movieRepository.findAll();
	myvector<Movie> filteredMovies;
	for (size_t i = 0; i < movies.size(); i++) {
		if (func(movies.at(i)))
			filteredMovies.push_back(movies.at(i));
	}
	return filteredMovies;
}

myvector<Movie> MovieService::sort(std::function<bool(const Movie&, const Movie&)>cmp) const {
	myvector<Movie> sortedMovies = movieRepository.findAll();
	if (sortedMovies.size() == 0) {
		return sortedMovies;
	}
	for (size_t i = 0; i < sortedMovies.size() - 1; i++) {
		for (size_t j = i + 1; j < sortedMovies.size(); j++) {
			if (cmp(sortedMovies.at(i), sortedMovies.at(j))) {
				Movie aux = sortedMovies.at(i);
				sortedMovies.at(i) = sortedMovies.at(j);
				sortedMovies.at(j) = aux;
			}
		}
	}
	return sortedMovies;
}
