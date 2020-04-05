#pragma once
#include <stdlib.h>

typedef int TElem;

class IteratorMultime;

class Multime {
	friend class IteratorMultime;
private:
	/* aici e reprezentarea */
	TElem* elems;
	int m;
	int size;

	//complexitate O(m) m - capacitate
	void ensureEnoughSize();

public:
	//constructorul implicit
	Multime();
	Multime(const Multime& m1) {
		this->elems = new TElem[m1.m];
		for (int i = 0; i < m1.m; i++) {
			elems[i] = m1.elems[i];
		}
		this->m = m1.m;
		this->size = m1.size;
	}

	Multime(int nr);

	//adauga un element in multime
	//returneaza adevarat daca elementul a fost adaugat (nu exista deja in multime)
	// complexitate O((1/(1 - a))) a - size/m
	bool adauga(TElem e);

	//sterge un element din multime
	//returneaza adevarat daca elementul a existat si a fost sters
	// complexitate O((1/(1 - a))) a - size/m
	bool sterge(TElem e);

	//verifica daca un element se afla in multime
	// complexitate O((1/(1 - a))) a - size/m
	bool cauta(TElem elem) const;


	//intoarce numarul de elemente din multime;
	int dim() const;

	//verifica daca multimea e vida;
	bool vida() const;

	//returneaza un iterator pe multime
	IteratorMultime iterator() const;

	// complexitate theta(m1) m1 - capacitate m1
	Multime reuniune(Multime m1);

	// destructorul multimii
	~Multime();

};

class IteratorMultime {
private:
	const Multime& multime;
	int curent = 0;

public:
	IteratorMultime(const Multime& m) : multime(m) {
		for (int i = 0; i < multime.m; i++) {
			if (multime.elems[i] == INT_MAX || multime.elems[i] == INT_MIN) {
				curent++;
			}
			else{
				break;
			}
		}
	}
	//reseteaza pozitia iteratorului la inceputul containerului
	void prim() {
		curent = 0;
		for (int i = 0; i < multime.m; i++) {
			if (multime.elems[i] == INT_MAX || multime.elems[i] == INT_MIN) {
				curent++;
			}
			else {
				break;
			}

		}
	}

	//muta iteratorul in container
	// arunca exceptie daca iteratorul nu e valid
	void urmator() {
		if (!valid()) {
			throw;
		}
		curent++;
		for (int i = curent; i < multime.m; i++) {
			if (multime.elems[i] == INT_MAX || multime.elems[i] == INT_MIN) {
				curent++;
			}
			else {
				break;
			}
		}
	}

	//verifica daca iteratorul e valid (indica un element al containerului)
	bool valid() const {
		return curent < multime.m;
	}

	//returneaza valoarea elementului din container referit de iterator
	//arunca exceptie daca iteratorul nu e valid
	TElem element() const {
		if (!valid()) {
			throw;
		}
		return multime.elems[curent];
	}

};