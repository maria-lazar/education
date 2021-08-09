#pragma once

class Change {
public:
	virtual void undo() = 0;
	virtual void apply() = 0;
	virtual ~Change() {}
};

class AddMovieChange : public Change {
private:
	const string title;
	const string genre;
	const int year;
	const string actor;
	MovieService& movieService;

public:
	AddMovieChange(MovieService& mS) : movieService(mS), year{ 0 }{}
	AddMovieChange(MovieService& movieService, const string title, const string genre, int year, const string actor) :
		movieService{ movieService }, title{ title }, genre{ genre }, year{ year }, actor{ actor }{};

	void undo() {
		movieService.remove(title, year);
	}

	void apply() {
		movieService.create(title, genre, year, actor);
	}
};

class RemoveMovieChange : public Change {
private:
	const string title;
	string genre = "";
	const int year;
	string actor = "";
	MovieService& movieService;

public:
	RemoveMovieChange(MovieService& movieService, const string title, int year) :
		movieService{ movieService }, title{ title }, year{ year }{};

	void undo() {
		movieService.create(title, genre, year, actor);
	}

	void apply() {
		Movie movie = movieService.remove(title, year);
		genre = movie.getGenre();
		actor = movie.getActor();
	}
};

class UpdateMovieChange : public Change {
private:
	const string title;
	string genre;
	const int year;
	string actor;
	MovieService& movieService;

public:
	UpdateMovieChange(MovieService& movieService, const string title, const string genre, int year, const string actor) :
		movieService{ movieService }, title{ title }, genre{ genre }, year{ year }, actor{ actor }{};

	void undo() {
		movieService.modify(title, genre, year, actor);
	}

	void apply() {
		Movie movie = movieService.modify(title, genre, year, actor);
		genre = movie.getGenre();
		actor = movie.getActor();
	}
};

