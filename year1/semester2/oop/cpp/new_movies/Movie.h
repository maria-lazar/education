#pragma once
#include <string>
//#include <exception>
#include "Exceptions.h"
using namespace std;

namespace domain {
	class Movie
	{
	public:
		Movie();

		Movie(const Movie& other);

		Movie(string t, string g, int y, string a);

		~Movie() {}

		string toString() const {
			string s = "Title: " + title + " Genre: " + genre + " Year: " + to_string(year) + " Starring actor: " + actor;
			return s;
		}

		const string getTitle() const { return title; }
		
		const string getGenre() const { return genre; }
		
		const int getYear() const noexcept{ return year; }
		
		const string getActor() const { return actor; }

		void setTitle(const string t);

		void setGenre(const string g);

		void setYear(const int y) noexcept;

		void setActor(const string a);

	private:
		string title;
		string genre;
		int year;
		string actor;
	};
	
	class MovieValidator {
	public:
		void validateYear(int year);
		void validateTitle(const string& title) const;
		void validateGenre(const string& genre) const;
		void validateActor(const string& actor) const;
	};
}
