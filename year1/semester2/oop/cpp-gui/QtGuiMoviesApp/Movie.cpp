#include "Movie.h"
#include <iostream>
using namespace domain;


Movie::Movie() noexcept: title(""), genre(""), actor("") {
	this->year = 0;
}

Movie::Movie(const string& t, const string& g, int y, const string& a)
{
	this->title = t;
	this->genre = g;
	this->year = y;
	this->actor = a;
}

Movie::Movie(const Movie& other) {
	//std::cout << "Copy" << std::endl;
	this->title = other.title;
	this->genre = other.genre;
	this->year = other.year;
	this->actor = other.actor;
}

void Movie::setTitle(const string& t) {
	title = t;
}

void Movie::setGenre(const string& g) {
	genre = g;
}

void Movie::setYear(const int y) noexcept{
	year = y;
}

void Movie::setActor(const string& a) {
	actor = a;
}

void MovieValidator::validateTitle(const string& title) const {
	if (title.size() == 0) {
		throw ValidationException(string("Title must be introduced"));
	}
}

void MovieValidator::validateGenre(const string& genre) const {
	if (genre.size() == 0) {
		throw ValidationException(string("Genre must be introduced"));
	}
	bool letters = true;
	/*for (size_t i = 0; i < genre.size(); i++) {
		if (!isalpha(genre.at(i))) {
			letters = false;
		}
	}*/
	for (const size_t i : genre) {
		if (!isalpha(i)) {
			letters = false;
		}
	}
	if (letters == false) {
		throw ValidationException(string("Genre must contain only letters"));
	}
}

void MovieValidator::validateActor(const string& actor) const {
	if (actor.size() == 0) {
		throw ValidationException(string("Actor must be introduced"));
	}
	bool letters = true;
	/*for (size_t i = 0; i < actor.size(); i++) {
		if (!isalpha(actor.at(i))) {
			letters = false;
		}
	}*/
	for (const size_t i : actor) {
		if (!isalpha(i)) {
			letters = false;
		}
	}
	if (letters == false) {
		throw ValidationException(string("Actor name must contain only letters"));
	}
}

void MovieValidator::validateYear(int year) {
	if (year < 1870 || year > 2019) {
		throw ValidationException(string("Invalid year"));
	}
}
