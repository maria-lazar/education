#define _CRTDBG_MAP_ALLOC
#include <stdlib.h>
#include <crtdbg.h>
#include "Movie.h"
#include "Vector.h"
#include "MovieRepository.h"
#include "MovieService.h"
#include "TestService.h"
#include "TestMovie.h"
#include "ui.h"
using namespace std;
using namespace domain;

void run() {
	MovieRepository movieRepository;
	MovieService movieService(movieRepository);
	MovieUi ui(movieService);
	testService();
	testMovie();
	ui.run();
}

int main() {
	run();
	_CrtDumpMemoryLeaks();
	return 0;
}