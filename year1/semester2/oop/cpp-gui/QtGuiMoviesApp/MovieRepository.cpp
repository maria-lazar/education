#include "MovieRepository.h"
#include <iostream>

const vector<Movie>& MovieRepository::findAll() const noexcept{
	return movies;
}

const Movie MovieRepository::save(const Movie& movie) {
	movies.push_back(movie);
	return movies.back();
}

const int MovieRepository::findByTitle(const string& title) {
	std::vector<Movie>::iterator it = std::find_if(movies.begin(), movies.end(), [&title](const Movie& m) noexcept{
		if (m.getTitle() == title)
			return true;
		return false;
	});
	if (it == movies.end()) {
		return -1;
	}
	return distance(movies.begin(), it);
}

const int MovieRepository::findByTitleYear(const string& title, const int year) {
	std::vector<Movie>::iterator it = std::find_if(movies.begin(), movies.end(), [&title, &year](const Movie& m) noexcept{
		if (m.getTitle() == title && m.getYear() == year)
			return true;
		return false;
	});
	if (it == movies.end()) {
		return -1;
	}
	return distance(movies.begin(), it);
}

const Movie MovieRepository::remove(const int index) {
	Movie movie = movies.at(index);
	movies.erase(movies.begin() + index);
	return movie;
}

const Movie MovieRepository::update(const int index, const string& genre, const string& actor) {
	const Movie movie = movies.at(index);
	movies.at(index).setGenre(genre);
	movies.at(index).setActor(actor);
	return movie;
}

const Movie MovieRepository::find(const int index) const{
	return movies.at(index);
}

vector<Movie> MovieRepository::getRandomMovies(size_t number) {
	if (number > movies.size()) {
		throw StorageException(string("Not enough movies"));
	}
	vector<Movie> vRandom;
	size_t k = 1;
	while (k <= number) {
		const int index = rand() % movies.size();
		string title = movies.at(index).getTitle();
		std::vector<Movie>::iterator it = std::find_if(vRandom.begin(), vRandom.end(), [&title](const Movie& m) noexcept{
			if (m.getTitle() == title)
				return true;
			return false;
		});
		if (it == vRandom.end()) {
			vRandom.push_back(movies.at(index));
			k++;
		}	
	}
	return vRandom;
}

void MovieRepositoryF::readFromFile() {
	ifstream fin(file);
	if (!fin.is_open()) {
		throw StorageException(string("Cannot open file data"));
	}
	while (!fin.eof()) {
		string title;
		getline(fin, title, ',');
		if (fin.eof()) {
			break;
		}
		string genre, actor;
		string sYear;
		getline(fin, genre, ',');
		getline(fin, sYear, ',');
		getline(fin, actor);
		int year;
		try {
			year = stoi(sYear);
			MovieValidator mV;
			mV.validateTitle(title);
			mV.validateGenre(genre);
			mV.validateYear(year);
			mV.validateActor(actor);
		}
		catch (StorageException) {
			throw StorageException(string("Corrupted file data"));
		}
		catch (invalid_argument) {
			throw StorageException(string("Corrupted file data"));
		}
		Movie m(title, genre, year, actor);
		MovieRepository::save(m);
	}
	fin.close();
}

void MovieRepositoryF::writeToFile() {
	ofstream fout(file);
	if (!fout.is_open()) {
		throw StorageException(string("Cannot open file data"));
	}
	const vector<Movie>& moviesAll = MovieRepository::findAll();
	for (size_t i = 0; i < moviesAll.size(); i++) {
		Movie m = moviesAll.at(i);
		fout << m.getTitle() << ',';
		fout << m.getGenre() << ',';
		fout << m.getYear() << ',';
		fout << m.getActor() << endl;
	}
	fout.close();
}

const Movie MovieRepositoryF::save(const Movie& movie){
	MovieRepository::save(movie);
	writeToFile();
	return movie;
}

const Movie MovieRepositoryF::remove(const int index){
	const vector<Movie>& m = MovieRepository::findAll();
	Movie movie = m.at(index);
	MovieRepository::remove(index);
	writeToFile();
	return movie;
}

const Movie MovieRepositoryF::update(const int index, const string& genre, const string& actor){
	const vector<Movie>& m = MovieRepository::findAll();
	Movie movie = m.at(index);
	MovieRepository::update(index, genre, actor);
	writeToFile();
	return movie;
}

const Movie ProbabilityRepository::save(const Movie& movie) {
	//PureAbstractRepository::save(movie);
	float nr = (float)rand() / RAND_MAX;
	std::cout << nr << std::endl;
	if (nr < probability) {
		throw StorageException("Exception thrown");
	}
	return movie;
}

const Movie ProbabilityRepository::remove(int index) {
	//PureAbstractRepository::remove(index);
	float nr = (float)rand() / RAND_MAX;
	if (nr < probability) {
		throw StorageException("Exception thrown");
	}
	return Movie("a", "a", index, "a");
}

const Movie ProbabilityRepository::update(const int index, const string& genre, const string& actor) {
	//PureAbstractRepository::update(index, genre, actor);
	float nr = (float)rand() / RAND_MAX;
	if (nr < probability) {
		throw StorageException("Exception thrown");
	}
	return Movie(genre, genre, index, actor);
}


const Movie ProbabilityRepository::find(const int index) const {
	//PureAbstractRepository::find(index);
	float nr = (float)rand() / RAND_MAX;
	if (nr < probability) {
		throw StorageException("Exception thrown");
	}
	return Movie("a", "a", index, "a");
}

const int ProbabilityRepository::findByTitleYear(const string& title, const int year) {
	//PureAbstractRepository::findByTitleYear(title, year);
	float nr = (float)rand() / RAND_MAX;
	if (nr < probability) {
		throw StorageException("Exception thrown");
	}
	string t = title;
	return year;
}

const int ProbabilityRepository::findByTitle(const string& title) {
	//PureAbstractRepository::findByTitle(title);
	float nr = (float)rand() / RAND_MAX;
	if (nr < probability) {
		throw StorageException("Exception thrown");
	}
	string t = title;
	return 0;
}

const vector<Movie>& ProbabilityRepository::findAll() const noexcept {
	vector<Movie> v;
	return v;
}

vector<Movie> ProbabilityRepository::getRandomMovies(size_t number) {
	//PureAbstractRepository::getRandomMovies(number);
	float nr = (float)rand() / RAND_MAX;
	if (nr < probability) {
		throw StorageException("Exception thrown");
	}
	size_t n;
	n = number;
	vector<Movie> v;
	return v;
}