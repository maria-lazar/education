#include "QtGuiMoviesApp.h"
#include <QtWidgets/QApplication>
#include "Movie.h"
#include "MovieRepository.h"
#include "ShoppingRepository.h"
#include "MovieService.h"
#include "TestService.h"
#include "TestMovie.h"
#include "ui.h"
#include <iostream>

int main(int argc, char *argv[])
{
	try {
		//MovieRepository movieRepository;
		MovieRepositoryF movieRepository("in.txt");
		//ShoppingRepository shoppingRepository;
		ShoppingRepositoryF shoppingRepository(movieRepository, "bag.txt");
		ProbabilityRepository pR(0.5);
		MovieService movieService(movieRepository, shoppingRepository);
		//MovieUi ui(movieService, pR);
		//ui.run();
		//testService();
		//testMovie();
		QApplication a(argc, argv);
		QtGuiMoviesApp w(movieService);
		w.show();
		return a.exec();
	}
	catch (StorageException& se) {
		std::cout << se.what() << std::endl;
	}
}
