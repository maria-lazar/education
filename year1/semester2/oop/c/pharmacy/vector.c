#include <stdlib.h>
#include <assert.h>
#include "vector.h"

void init(Vector* v) {
	v->size = 0;
	v->max = 5;
	v->elems = (Element*)malloc(sizeof(Element) * v->max);
}

void destroy(Vector* v) {
	free(v->elems);
}

/*
	Increases the maxSize of the Vector and reallocates its elements if the maxSize of the Vector is reached
	v: pointer to a Vector
*/
void ensure_available_space(Vector* v) {
	if (v->size == v->max) {
		v->max = v->max * 2;
		Element* elems = (Element*)malloc(sizeof(Element) * v->max);
		for (int i = 0; i < v->size; i++) {
			elems[i] = v->elems[i];
		}
		free(v->elems);
		v->elems = elems;
	}
}

void append(Vector* v, Element e) {
	ensure_available_space(v);
	v->elems[v->size++] = e;
}

int size(Vector* v) {
	return v->size;
}

Element element(Vector* v, int index) {
	if (index >= v->size) {
		return NULL;
	}
	return v->elems[index];
}

void remove_element(Vector* v, int index) {
	if (index >= v->size) {
		return;
	}
	for (int i = index; i < v->size; i++) {
		v->elems[i] = v->elems[i + 1];
	}
	v->size--;
}

//TESTS
	
void test_init() {
	Vector v;
	init(&v);
	assert(v.size == 0);
	assert(v.max == 5);
	destroy(&v);
}

void test_append() {
	Vector v;
	init(&v);
	int a = 1;
	int b = 2;
	int c = 3;
	int d = 4;
	int e = 5;
	int f = 6;
	int g = 7;
	append(&v, &a);
	assert(*(int*)(element(&v, 0)) == 1);
	append(&v, &b);
	assert(*(int*)(element(&v, 1)) == 2);
	assert(v.size == 2);
	append(&v, &c);
	append(&v, &d);
	append(&v, &e);
	append(&v, &f);
	append(&v, &g);
	assert(*(int*)(element(&v, 6)) == 7);
	destroy(&v);
}

void test_size() {
	Vector v;
	init(&v);
	int a = 1;
	assert(size(&v) == 0);
	append(&v, &a);
	assert(size(&v) == 1);
	destroy(&v);
}

void test_element() {
	Vector v;
	init(&v);
	int a = 1;
	int b = 2;
	int c = 3;
	int d = 4;
	assert(element(&v, 0) == NULL);
	append(&v, &a);
	assert(*(int*)(element(&v, 0)) == 1);
	append(&v, &b);
	append(&v, &c);
	append(&v, &d);
	assert(*(int*)(element(&v, 2)) == 3);
	assert(element(&v, 6) == NULL);
	destroy(&v);
}

void test_remove() {
	Vector v;
	init(&v);
	int a = 1;
	int b = 2;
	int c = 3;
	int d = 4;
	append(&v, &a);
	append(&v, &b);
	append(&v, &c);
	append(&v, &d);
	remove_element(&v, 0);
	assert(*(int*)(element(&v, 0)) == 2);
	assert(*(int*)(element(&v, 1)) == 3);
	assert(*(int*)(element(&v, 2)) == 4);
	assert(element(&v, 3) == NULL);
	remove_element(&v, 2);
	assert(*(int*)(element(&v, 0)) == 2);
	assert(*(int*)(element(&v, 1)) == 3);
	assert(element(&v, 2) == NULL);
	remove_element(&v, 6);
	assert(*(int*)(element(&v, 0)) == 2);
	assert(*(int*)(element(&v, 1)) == 3);
	assert(element(&v, 2) == NULL);
	destroy(&v);
}

void test_vector() {
	test_init();
	test_size();
	test_append();
	test_element();
	test_remove();
}