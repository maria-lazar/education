#include "multime.h"
#include "test_scurt.h"
#include "test_extins.h"
#include <iostream>
using namespace std;

void test() {
	Multime m;
	m.adauga(23);
	m.adauga(11);
	m.adauga(8);
	m.adauga(18);
	m.adauga(3);
	m.adauga(19);
	m.adauga(34);
	m.adauga(15);
	m.adauga(27);
	IteratorMultime it = m.iterator();
	while (it.valid()) {
		cout << it.element() << " ";
		it.urmator();
	}

}


int main() {
	//testAll();
	//testAllExtins();
	test();
	return 0;
}