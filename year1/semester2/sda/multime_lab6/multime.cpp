#include "multime.h"
#include <stdlib.h>
#include <iostream>
#define null INT_MAX 
using namespace std;

bool prime(int n) {
	bool prim = true;
	for (int i = 2; i <= n / 2; i++) {
		if (n % i == 0) {
			prim = false;
			break;
		}
	}
	return prim;
}

int d1(TElem c, int m) {
	return c % m;
}

int d2(TElem c, int m) {
	return 13 - c % 13;
}

/*int d(TElem c, int i, int m) {
	return (d1(c, m) + i * d2(c, m)) % m;
}*/

int d(TElem c, int i, int m) {
	return (d1(c, m) + i) % m;
}

Multime::Multime() {
	m = 16;
	size = 0;
	elems = new TElem[m];
	for (int i = 0; i < m; i++) {
		elems[i] = INT_MAX;
	}
}


Multime::Multime(int nr) {
	m = nr;
	size = 0;
	elems = new TElem[m];
	for (int i = 0; i < m; i++) {
		elems[i] = INT_MAX;
	}
}

Multime::~Multime() {
	delete elems;
}

void Multime::ensureEnoughSize() {
	if (size < m) {
		return;
	}
	TElem* v = new TElem[m];
	for (int i = 0; i < m; i++) {
		v[i] = elems[i];
	}
	delete elems;
	int m_vechi = m;
	m = m * 2;
	
	int i = m + 1;
	while (prime(i) == false) {
		i++;
	}
	m = i;

	int s = size;
	elems = new TElem[m];
	for (int i = 0; i < m; i++) {
		elems[i] = INT_MAX;
	}
	for (int i = 0; i < m_vechi; i++) {
		if (v[i] != INT_MAX) {
			adauga(v[i]);
		}
	}
	size = s;
}

bool Multime::adauga(TElem e) {
	int i = 0;
	bool gasit = false;
	ensureEnoughSize();
	while (i != m && gasit == false) {
		int j = d(e, i, m);
		if (elems[j] == e) {
			return false;
		}
		else {
			if (elems[j] == INT_MAX || elems[j] == INT_MIN) {
				elems[j] = e;
				gasit = true;
				size++;
			}
			else {
				i++;
			}
		}
	}
	return gasit;
}

Multime Multime::reuniune(Multime m1) {
	Multime m2 = *this;
	IteratorMultime it = m1.iterator();
	it.prim();
	while (it.valid()) {
		TElem e = it.element();
		int i = 0;
		bool gasit = false;
		while (i != m && gasit == false) {
			int j = d(e, i, m);
			if (elems[j] != e) {
				if (elems[j] == INT_MAX || elems[j] == INT_MIN) {
					ensureEnoughSize();
					elems[j] = e;
					gasit = true;
					size++;
				}
				else {
					i++;
				}
			}
			else {
				break;
			}
		}
		it.urmator();
	}
	Multime m3 = *this;
	*this = m2;
	return m3;
}

bool Multime::cauta(TElem elem) const {
	int i = 0;
	bool gasit = false;
	while (i != m && gasit == false) {
		int j = d(elem, i, m);
		if (elems[j] == elem) {
			return true;
		}
		else {
			if (elems[j] == INT_MAX) {
				break;
			}
			i++;
		}
	}
	return false;
}

int Multime::dim() const {
	return size;
}

bool Multime::vida() const {
	return size == 0;
}

IteratorMultime Multime::iterator() const{
	return IteratorMultime(*this);
}

bool Multime::sterge(TElem e){
	int i = 0;
	bool gasit = false;
	int j;
	while (i != m && gasit == false) {
		j = d(e, i, m);
		if (elems[j] == e) {
			gasit = true;
			break;
		}
		else {
			if (elems[j] == INT_MAX) {
				break;
			}
			i++;
		}
	}
	if (gasit == false) {
		return false;
	}
	elems[j] = INT_MIN;
	size--;
	return true;
}
