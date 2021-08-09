#include "ui.h"
#include "Movie.h"
#include "Exceptions.h"
#include <iostream>
#include <stdlib.h>
#include <memory>
using namespace std;
using namespace domain;

void ensureDeleted(void* p) {
	if (p != nullptr) {
		delete p;
	}
}

void MovieUi::printMovies(const vector<Movie>& movies) {
	if (movies.size() == 0) {
		cout << "No movies\n";
	}
	else {
		for (const Movie& m : movies) {
			cout << m.toString() << endl;
		}
	}
}

void MovieUi::printAll() {
	const vector<Movie>& movies = movieService.getAll();
	printMovies(movies);
	cout << "Shopping bag: " << endl;
	const vector<Movie>& sMovies = movieService.getAllShopping();
	printMovies(sMovies);
}

void MovieUi::addMovie() {
	//std::unique_ptr<AddMovieChange> addMovieChange;
	try {
		string title, genre, actor, sYear;
		int year;
		cout << "Title: ";
		cin >> title;
		cout << "Genre: ";
		cin >> genre;
		cout << "Year: ";
		cin >> sYear;
		year = stoi(sYear);
		cout << "Starring actor: ";
		cin >> actor;
		cout << endl;
		std::unique_ptr<AddMovieChange> addMovieChange = std::make_unique<AddMovieChange>(movieService, title, genre, year, actor);
		addMovieChange->apply();
		prevChanges.push_back(std::make_unique<AddMovieChange>(movieService, title, genre, year, actor));
		for (size_t i = 0; i < nextChanges.size(); i++) {
			delete nextChanges.at(i);
		}
		nextChanges.clear();
		cout << "Movie added" << endl;
	}
	catch (std::invalid_argument) {
		cout << "Invalid data type" << endl;
	}
	catch (const StorageException& se) {
		cout << se.what() << endl;
		//ensureDeleted(addMovieChange);
	}
	catch (const ValidationException& ve) {
		cout << ve.what() << endl;
		//ensureDeleted(addMovieChange);
	}
	catch (...) {
		//ensureDeleted(addMovieChange);
	}
}

void MovieUi::deleteMovie() {
	try {
		string title, sYear;
		int year;
		cout << "Title: ";
		cin >> title;
		cout << "Year: ";
		cin >> sYear;
		year = stoi(sYear);
		std::unique_ptr<RemoveMovieChange> removeMovieChange = std::make_unique<RemoveMovieChange>(movieService, title, year);
		removeMovieChange->apply();
		prevChanges.push_back(move(removeMovieChange));
		//prevChanges.push_back(std::make_unique<RemoveMovieChange>(movieService, title, year));
		for (size_t i = 0; i < nextChanges.size(); i++) {
			delete nextChanges.at(i);
		}
		nextChanges.clear();
		cout << "Movie deleted" << endl;
	}
	catch (std::invalid_argument) {
		cout << "Invalid data type" << endl;
	}
	catch (const StorageException& se) {
		//ensureDeleted(removeMovieChange);
		//delete removeMovieChange;
		cout << se.what() << endl;
	}
	catch (const ValidationException& ve) {
		//ensureDeleted(removeMovieChange);
		//delete removeMovieChange;
		cout << ve.what() << endl;
	}
	catch (...) {
		//ensureDeleted(removeMovieChange);
		//delete removeMovieChange;
	}
}

void MovieUi::updateMovie() {
	try {
		string title, genre, actor, sYear;
		int year;
		cout << "Title: ";
		cin >> title;
		cout << "Year: ";
		cin >> sYear;
		year = stoi(sYear);
		cout << "New genre: ";
		cin >> genre;
		cout << "New actor: ";
		cin >> actor;
		std::unique_ptr<UpdateMovieChange> updateMovieChange = std::make_unique<UpdateMovieChange>(movieService, title, genre, year, actor);
		updateMovieChange->apply();
		prevChanges.push_back(move(updateMovieChange));
		//prevChanges.push_back(std::make_unique<UpdateMovieChange>(movieService, title, genre, year, actor));
		for (size_t i = 0; i < nextChanges.size(); i++) {
			delete nextChanges.at(i);
		}
		nextChanges.clear();
		cout << "Movie updated" << endl;
	}
	catch (std::invalid_argument) {
		cout << "Invalid data type" << endl;
	}
	catch (const StorageException& se) {
		cout << se.what() << endl;
		//ensureDeleted(updateMovieChange);
		//delete updateMovieChange;
	}
	catch (const ValidationException& ve) {
		//delete updateMovieChange;
		cout << ve.what() << endl;
		//ensureDeleted(updateMovieChange);
	}
	catch (...) {
		//delete updateMovieChange;
		//ensureDeleted(updateMovieChange);
	}
}

void MovieUi::searchMovie() {
	try {
		string title, sYear;
		int year;
		cout << "Title: ";
		cin >> title;
		cout << "Year: ";
		cin >> sYear;
		year = stoi(sYear);
		const Movie& movie = movieService.search(title, year);
		cout << movie.toString() << endl;
	}
	catch (std::invalid_argument) {
		cout << "Invalid data type" << endl;
	}
	catch (const StorageException& se) {
		cout << se.what() << endl;
	}
	catch (const ValidationException& ve) {
		cout << ve.what() << endl;
	}
}

void MovieUi::filterTitle() {
	try {
		string title;
		cout << "Title: ";
		cin >> title;
		MovieValidator movieValidator;
		movieValidator.validateTitle(title);
		const vector<Movie> filteredMovies = movieService.filter(movieService.getAll(), [&title](const Movie &m) noexcept{
			return m.getTitle() == title;
		});
		printMovies(filteredMovies);
	}
	catch (const ValidationException& ve) {
		cout << ve.what() << endl;
	}
}

void MovieUi::filterYear() {
	try {
		string sYear;
		int year;
		cout << "Year: ";
		cin >> sYear;
		year = stoi(sYear);
		MovieValidator movieValidator;
		movieValidator.validateYear(year);
		vector<Movie> filteredMovies;
		filteredMovies = movieService.filter(movieService.getAll(), [&year](const Movie &m) noexcept {
			return m.getYear() == year;
		});
		printMovies(filteredMovies);
	}
	catch (std::invalid_argument) {
		cout << "Invalid data type" << endl;
	}
	catch (const ValidationException& ve) {
		cout << ve.what() << endl;
	}
}

void MovieUi::sortTitle() {
	vector<Movie> sortedMovies;
	sortedMovies = movieService.sort(movieService.getAll(), [](const Movie& m1, const Movie& m2) noexcept{
		return m1.getTitle() <= m2.getTitle();
	});
	printMovies(sortedMovies);
}

void MovieUi::sortActor() {
	vector<Movie> sortedMovies;
	sortedMovies = movieService.sort(movieService.getAll(), [](const Movie& m1, const Movie& m2) noexcept{
		return m1.getActor() <= m2.getActor();
	});
	printMovies(sortedMovies);
}

void MovieUi::sortYearGenre() {
	vector<Movie> sortedMovies;
	sortedMovies = movieService.sort(movieService.getAll(), [](const Movie& m1, const Movie& m2) noexcept{
		if (m1.getYear() == m2.getYear()) {
			return m1.getGenre() <= m2.getGenre();
		}
		return m1.getYear() <= m2.getYear();
	});
	printMovies(sortedMovies);
}

void MovieUi::addMovieToBag() {
	try {
		string title;
		cout << "Title: ";
		cin >> title;
		movieService.addMovieBag(title);
		cout << "Shopping bag items: " << movieService.getSizeShoppingBag() << endl;
	}
	catch (const StorageException& se) {
		cout << se.what() << endl;
	}
	catch (const ValidationException& ve) {
		cout << ve.what() << endl;
	}
}

void MovieUi::clearShoppingBag() {
	movieService.clearBag();
	cout << "Shopping bag items: " << movieService.getSizeShoppingBag() << endl;
}

void MovieUi::generateShoppingBag() {
	try {
		string sNumber;
		cout << "Number of movies: ";
		cin >> sNumber;
		const size_t number = stoi(sNumber);
		if (number < 0) {
			cout << "Invalid number";
		}
		else {
			movieService.generateBag(number);
			cout << "Shopping bag items: " << movieService.getSizeShoppingBag() << endl;
		}
	}
	catch (std::invalid_argument) {
		cout << "Invalid data type" << endl;
	}
	catch (const StorageException& se) {
		cout << se.what() << endl;
	}
}

void MovieUi::getMap() {
	vector<MovieDTO> v = movieService.getMap();
	for (size_t i = 0; i < v.size(); i++) {
		cout << v.at(i).getGenre() << " " << v.at(i).getCount() << endl;
	}
}

void MovieUi::saveShoppingBag() {
	movieService.saveBag();
	cout << "Shopping bag items: " << movieService.getSizeShoppingBag() << endl;
}

void MovieUi::undo() {
	if (prevChanges.size() == 0) {
		cout << "No action to undo\n";
		return;
	}
	//std::unique_ptr<Change> lastChange = );
	prevChanges.back()->undo();
	prevChanges.pop_back();
	//lastChange-;
	//nextChanges.push_back(lastChange);
}

void MovieUi::redo() {
	if (nextChanges.size() == 0) {
		cout << "No action to redo\n";
		return;
	}
	Change* nextChange = nextChanges.back();
	nextChanges.pop_back();
	nextChange->apply();
	//prevChanges.push_back(nextChange);
}

void MovieUi::prob() {
	try {
		probRepo.save(Movie("a", "a", 2000, "a"));
	}
	catch (StorageException& se) {
		cout << se.what() << endl;
	}
}

void MovieUi::menu() {
	cout << "0 - Exit" << endl;
	cout << "1 - Add movie" << endl;
	cout << "2 - Print movies" << endl;
	cout << "3 - Delete movie" << endl;
	cout << "4 - Update movie" << endl;
	cout << "5 - Search movie by title and year" << endl;
	cout << "6 - Filter by title" << endl;
	cout << "7 - Filter by year" << endl;
	cout << "8 - Sort by title" << endl;
	cout << "9 - Sort by actor" << endl;
	cout << "10 - Sort by year and genre" << endl;
	cout << "11 - Add movie to shopping bag" << endl;
	cout << "12 - Clear shopping bag" << endl;
	cout << "13 - Generate shopping bag" << endl;
	cout << "14 - Get list of genres" << endl;
	cout << "15 - Save shopping bag" << endl;
	cout << "16 - Undo" << endl;
	cout << "17 - Redo" << endl;
	cout << "18 - Probability" << endl;
}

void MovieUi::run() {
	int cmd = -1;
	while (1) {
		menu();
		cin >> cmd;
		if (cmd == 0) {
			break;
		}
		switch (cmd) {
		case 1:
			addMovie();
			break;
		case 2:
			printAll();
			break;
		case 3:
			deleteMovie();
			break;
		case 4:
			updateMovie();
			break;
		case 5:
			searchMovie();
			break;
		case 6:
			filterTitle();
			break;
		case 7:
			filterYear();
			break;
		case 8:
			sortTitle();
			break;
		case 9:
			sortActor();
			break;
		case 10:
			sortYearGenre();
			break;
		case 11:
			addMovieToBag();
			break;
		case 12:
			clearShoppingBag();
			break;
		case 13:
			generateShoppingBag();
			break;
		case 14:
			getMap();
			break;
		case 15:
			saveShoppingBag();
			break;
		case 16:
			undo();
			break;
		case 17:
			redo();
			break;
		case 18:
			prob();
			break;
		default:
			cout << "Invalid command\n";
		}
	}
}

