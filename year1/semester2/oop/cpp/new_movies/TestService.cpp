#include "TestService.h"

void testGetAll() {
	MovieRepository movieRepository;
	MovieService movieService(movieRepository);
	assert(movieService.getAll().size() == 0);
	movieService.create("a", "b", 1900, "c");
	movieService.create("d", "e", 2000, "f");
	myvector<Movie> m = movieService.getAll();
	assert(m.size() == 2);
	assert(m.at(0).getTitle() == "a");
	assert(m.at(0).getGenre() == "b");
	assert(m.at(0).getYear() == 1900);
	assert(m.at(0).getActor() == "c");
	assert(m.at(1).getTitle() == "d");
	assert(m.at(1).getGenre() == "e");
	assert(m.at(1).getYear() == 2000);
	assert(m.at(1).getActor() == "f");
}

void testCreate() {
	MovieRepository movieRepository;
	MovieService movieService(movieRepository);
	assert(movieService.getAll().size() == 0);
	movieService.create("a", "b", 1900, "c");
	movieService.create("d", "e", 2000, "f");
	myvector<Movie> m = movieService.getAll();
	assert(m.size() == 2);
	assert(m.at(0).getTitle() == "a");
	assert(m.at(0).getGenre() == "b");
	assert(m.at(0).getYear() == 1900);
	assert(m.at(0).getActor() == "c");
	assert(m.at(1).getTitle() == "d");
	assert(m.at(1).getGenre() == "e");
	assert(m.at(1).getYear() == 2000);
	assert(m.at(1).getActor() == "f");
	try {
		movieService.create("a", "z", 1900, "z");
	}
	catch (StorageException) {
		assert(true);
	}
}

void testRemove() {
	MovieRepository movieRepository;
	MovieService movieService(movieRepository);
	assert(movieService.getAll().size() == 0);
	movieService.create("a", "b", 1900, "c");
	movieService.create("d", "e", 2000, "f");
	myvector<Movie> m = movieService.getAll();
	assert(m.size() == 2);
	try {
		movieService.remove("c", 1900);
		assert(false);
	}
	catch (StorageException) {
		assert(true);
	}
	m = movieService.getAll();
	assert(m.size() == 2);
	movieService.remove("a", 1900);
	m = movieService.getAll();
	assert(m.size() == 1);
	assert(m.at(0).getTitle() == "d");
	assert(m.at(0).getGenre() == "e");
	assert(m.at(0).getYear() == 2000);
	assert(m.at(0).getActor() == "f");
}

void testModify() {
	MovieRepository movieRepository;
	MovieService movieService(movieRepository);
	movieService.create("a", "b", 1900, "c");
	movieService.create("d", "e", 2000, "f");
	myvector<Movie> m = movieService.getAll();
	assert(m.at(0).getTitle() == "a");
	assert(m.at(0).getGenre() == "b");
	assert(m.at(0).getYear() == 1900);
	assert(m.at(0).getActor() == "c");
	movieService.modify("a", "z", 1900, "z");
	m = movieService.getAll();
	assert(m.at(0).getTitle() == "a");
	assert(m.at(0).getGenre() == "z");
	assert(m.at(0).getYear() == 1900);
	assert(m.at(0).getActor() == "z");
	try {
		movieService.modify("z", "z", 1900, "z");
		assert(false);
	}
	catch (StorageException) {
		assert(true);
	}
}

void testSearch() {
	MovieRepository movieRepository;
	MovieService movieService(movieRepository);
	movieService.create("a", "b", 1900, "c");
	movieService.create("d", "e", 2000, "f");
	Movie movie = movieService.search("a", 1900);
	myvector<Movie> m = movieService.getAll();
	assert(movie.getTitle() == "a");
	assert(movie.getGenre() == "b");
	assert(movie.getYear() == 1900);
	assert(movie.getActor() == "c");
	try {
		movieService.search("z", 1900);
		assert(false);
	}
	catch (StorageException) {
		assert(true);
	}
}

void testFilter() {
	MovieRepository movieRepository;
	MovieService movieService(movieRepository);
	movieService.create("a", "b", 1900, "c");
	movieService.create("d", "e", 2000, "f");
	movieService.create("a", "e", 2000, "f");
	myvector<Movie> m = movieService.getAll();
	assert(m.size() == 3);
	string title = "a";
	myvector<Movie> fMovies = movieService.filter([&title](const Movie &m) {
		return m.getTitle() == title;
	});
	assert(fMovies.size() == 2);
	assert(fMovies.at(0).getTitle() == "a");
	assert(fMovies.at(0).getGenre() == "b");
	assert(fMovies.at(0).getYear() == 1900);
	assert(fMovies.at(0).getActor() == "c");
	assert(fMovies.at(1).getTitle() == "a");
	assert(fMovies.at(1).getGenre() == "e");
	assert(fMovies.at(1).getYear() == 2000);
	assert(fMovies.at(1).getActor() == "f");
	int year = 2000;
	fMovies = move(movieService.filter([&year](const Movie &m) noexcept {
		return m.getYear() == year;
	}));
	assert(fMovies.size() == 2);
	assert(fMovies.at(0).getTitle() == "d");
	assert(fMovies.at(0).getGenre() == "e");
	assert(fMovies.at(0).getYear() == 2000);
	assert(fMovies.at(0).getActor() == "f");
	assert(fMovies.at(1).getTitle() == "a");
	assert(fMovies.at(1).getGenre() == "e");
	assert(fMovies.at(1).getYear() == 2000);
	assert(fMovies.at(1).getActor() == "f");
	year = 5;
	fMovies = move(movieService.filter([&year](const Movie &m) noexcept {
		return m.getYear() == year;
	}));
	assert(fMovies.size() == 0);
}

void testSort() {
	MovieRepository movieRepository;
	MovieService movieService(movieRepository);
	assert(movieService.sort([](const Movie& m1, const Movie& m2) {
		return m1.getActor() >= m2.getActor();
	}).size() == 0);
	movieService.create("a", "b", 2001, "c");
	movieService.create("d", "e", 1900, "f");
	movieService.create("b", "e", 2000, "e");
	myvector<Movie> sMovies = movieService.sort([](const Movie& m1, const Movie& m2) {
		return m1.getTitle() >= m2.getTitle();
	});
	assert(sMovies.at(0).getTitle() == "a");
	assert(sMovies.at(0).getGenre() == "b");
	assert(sMovies.at(0).getYear() == 2001);
	assert(sMovies.at(0).getActor() == "c");
	assert(sMovies.at(1).getTitle() == "b");
	assert(sMovies.at(1).getGenre() == "e");
	assert(sMovies.at(1).getYear() == 2000);
	assert(sMovies.at(1).getActor() == "e");
	assert(sMovies.at(2).getTitle() == "d");
	assert(sMovies.at(2).getGenre() == "e");
	assert(sMovies.at(2).getYear() == 1900);
	assert(sMovies.at(2).getActor() == "f");
	sMovies = movieService.sort([](const Movie& m1, const Movie& m2) {
		return m1.getActor() >= m2.getActor();
	});
	assert(sMovies.at(0).getTitle() == "a");
	assert(sMovies.at(0).getGenre() == "b");
	assert(sMovies.at(0).getYear() == 2001);
	assert(sMovies.at(0).getActor() == "c");
	assert(sMovies.at(1).getTitle() == "b");
	assert(sMovies.at(1).getGenre() == "e");
	assert(sMovies.at(1).getYear() == 2000);
	assert(sMovies.at(1).getActor() == "e");
	assert(sMovies.at(2).getTitle() == "d");
	assert(sMovies.at(2).getGenre() == "e");
	assert(sMovies.at(2).getYear() == 1900);
	assert(sMovies.at(2).getActor() == "f");
	sMovies = movieService.sort([](const Movie& m1, const Movie& m2) {
		if (m1.getYear() == m2.getYear()) {
			return m1.getGenre() >= m2.getGenre();
		}
		return m1.getYear() >= m2.getYear();
	});
	assert(sMovies.at(0).getTitle() == "d");
	assert(sMovies.at(0).getGenre() == "e");
	assert(sMovies.at(0).getYear() == 1900);
	assert(sMovies.at(0).getActor() == "f");
	assert(sMovies.at(1).getTitle() == "b");
	assert(sMovies.at(1).getGenre() == "e");
	assert(sMovies.at(1).getYear() == 2000);
	assert(sMovies.at(1).getActor() == "e");
	assert(sMovies.at(2).getTitle() == "a");
	assert(sMovies.at(2).getGenre() == "b");
	assert(sMovies.at(2).getYear() == 2001);
	assert(sMovies.at(2).getActor() == "c");
}

void testErase() {
	myvector<int> v;
	try {
		v.back();
	}
	catch (std::out_of_range) {
		assert(true);
	}
	try {
		v.at(1);
	}
	catch (std::out_of_range) {
		assert(true);
	}
	v.push_back(1);
	v.push_back(2);
	v.push_back(3);
	v.push_back(4);
	v.erase(3);
	assert(v.size() == 3);
	try {
		v.erase(3);
	}
	catch (std::out_of_range) {
		assert(true);
	}
}

void testService() {
	testGetAll();
	testCreate();
	testRemove();
	testModify();
	testSearch();
	testFilter();
	testSort();
	testErase();
}