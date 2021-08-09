#include "MovieRepository.h"

const myvector<Movie>& MovieRepository::findAll() const noexcept{
	return movies;
}

const Movie& MovieRepository::save(const Movie& movie) {
	movies.push_back(movie);
	return movies.back();
}

const int MovieRepository::findByTitleYear(const string& title, const int year) {
	for (size_t i = 0; i < movies.size(); i++) {
		if (movies.at(i).getTitle() == title && movies.at(i).getYear() == year) {
			return i;
		}
	}
	return -1;
}

const Movie& MovieRepository::remove(const int index) {
	const Movie& movie = movies.at(index);
	movies.erase(index);
	return movie;
}

const Movie MovieRepository::update(const int index, const string& genre, const string& actor) {
	Movie& movie = movies.at(index);
	movie.setGenre(genre);
	movie.setActor(actor);
	return movie;
}

const Movie& MovieRepository::find(const int index) const{
	return movies.at(index);
}
