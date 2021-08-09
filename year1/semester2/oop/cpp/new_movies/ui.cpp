#include "ui.h"
#include "Movie.h"
#include "Exceptions.h"
#include "Vector.h"
#include <iostream>
using namespace std;
using namespace domain;

void MovieUi::printMovies(const myvector<Movie>& movies) {
	if (movies.size() == 0) {
		cout << "No movies\n";
	}
	else {
		for (size_t i = 0; i < movies.size(); i++) {
			cout << movies.at(i).toString() << endl;
		}
	}
}

void MovieUi::printAll() {
	const myvector<Movie>& movies = movieService.getAll();
	printMovies(movies);
}

void MovieUi::addMovie() {
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
		movieService.create(title, genre, year, actor);
		cout << "Movie added" << endl;
	}
	catch (std::invalid_argument) {
		cout << "Invalid data type" << endl;
	}
	catch(const StorageException& se){
		cout << se.what() << endl;
	}
	catch (const ValidationException& ve) {
		cout << ve.what() << endl;
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
		movieService.remove(title, year);
		cout << "Movie deleted" << endl;
	}
	catch(std::invalid_argument){
		cout << "Invalid data type" << endl;
	}
	catch (const StorageException& se) {
		cout << se.what() << endl;
	}
	catch (const ValidationException& ve) {
		cout << ve.what() << endl;
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
		movieService.modify(title, genre, year, actor);
		cout << "Movie updated" << endl;
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
		const myvector<Movie> filteredMovies = movieService.filter([&title](const Movie &m) {
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
		myvector<Movie> filteredMovies;
		filteredMovies = movieService.filter([&year](const Movie &m) noexcept{
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
	myvector<Movie> sortedMovies = movieService.sort([](const Movie& m1, const Movie& m2) {
		return m1.getTitle() >= m2.getTitle();
	});
	printMovies(sortedMovies);
}

void MovieUi::sortActor() {
	myvector<Movie> sortedMovies = movieService.sort([](const Movie& m1, const Movie& m2) {
		return m1.getActor() >= m2.getActor();
	});
	printMovies(sortedMovies);
}

void MovieUi::sortYearGenre() {
	myvector<Movie> sortedMovies = movieService.sort([](const Movie& m1, const Movie& m2) {
		if (m1.getYear() == m2.getYear()) {
			return m1.getGenre() >= m2.getGenre();
		}
		return m1.getYear() >= m2.getYear();
	});
	printMovies(sortedMovies);
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
		default:
			cout << "Invalid command\n";
		}
	}
}

