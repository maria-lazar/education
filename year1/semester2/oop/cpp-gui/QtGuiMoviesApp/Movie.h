#pragma once
#include <string>
#include "Exceptions.h"
using namespace std;

namespace domain {
	class Movie
	{
	public:
		Movie() noexcept;

		Movie(const Movie& other);

		Movie(const string& t, const string& g, int y, const string& a);

		~Movie() {}

		string toString() const {
			//string s = title +  ", " + genre + ", " + to_string(year) + ", " + actor;
			string s = title;
			return s;
		}

		const string& getTitle() const noexcept{ return title; }
		
		const string& getGenre() const noexcept{ return genre; }
		
		const int getYear() const noexcept{ return year; }
		
		const string& getActor() const noexcept{ return actor; }

		void setTitle(const string& t);

		void setGenre(const string& g);

		void setYear(const int y) noexcept;

		void setActor(const string& a);

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

	class MovieDTO {
	private:
		string genre;
		int count;
	public:
		MovieDTO(string genre, int count): genre(genre), count(count){}

		string getGenre() {
			return genre;
		}

		int getCount() {
			return count;
		}
	};
}
