#include "ShoppingRepository.h"



ShoppingRepository::~ShoppingRepository()
{
}

int ShoppingRepository::findMovie(const string& title) {
	std::vector<Movie>::iterator it = std::find_if(shoppingBag.begin(), shoppingBag.end(), [&title](const Movie& m) noexcept{
		if (m.getTitle() == title)
			return true;
		return false;
	});
	if (it == shoppingBag.end()) {
		return -1;
	}
	return distance(shoppingBag.begin(), it);
}

void ShoppingRepository::addToBag(const Movie& m) {
	shoppingBag.push_back(m);
}

void ShoppingRepository::clear() noexcept{
	shoppingBag.clear();
}

void ShoppingRepository::remove(const string& title) {
	int index = findMovie(title);
	if (index != -1) {
		shoppingBag.erase(shoppingBag.begin() + index);
	}
}

void ShoppingRepository::update(const string& title, const string& genre, const string& actor) {
	int index = findMovie(title);
	if (index != -1) {
		shoppingBag.at(index).setGenre(genre);
		shoppingBag.at(index).setActor(actor);
	}
}

void ShoppingRepository::replace(vector<Movie> v) {
	shoppingBag = v;
}

size_t ShoppingRepository::findIndexInRepository(string title) {
	return movieRepository.findByTitle(title);
}

void ShoppingRepositoryF::readFromFile() {
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
		catch (ValidationException) {
			throw StorageException(string("Corrupted file data"));
		}
		catch (invalid_argument) {
			throw StorageException(string("Corrupted file data"));
		}
		if (findIndexInRepository(title) != -1) {
			Movie m(title, genre, year, actor);
			ShoppingRepository::addToBag(m);
		}
	}
	fin.close();
}

void ShoppingRepositoryF::writeToFile() {
	ofstream fout(file);
	if (!fout.is_open()) {
		throw StorageException(string("Cannot open file data"));
	}
	const vector<Movie>& moviesAll = ShoppingRepository::findAll();
	for (size_t i = 0; i < moviesAll.size(); i++) {
		Movie m = moviesAll.at(i);
		fout << m.getTitle() << ',';
		fout << m.getGenre() << ',';
		fout << m.getYear() << ',';
		fout << m.getActor() << endl;
	}
	fout.close();
}

void ShoppingRepositoryF::saveBag() {
	writeToFile();
}