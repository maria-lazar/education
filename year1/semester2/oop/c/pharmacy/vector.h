#ifndef VECTOR_H_
#define VECTOR_H_

typedef void* Element;

typedef struct {
	int size;
	int max;
	Element* elems;
} Vector;

/*
	Set default values for a Vector
	v: pointer to a Vector
*/
void init(Vector* v);

/*
	Destroys a Vector
	v: pointer to a Vector
*/
void destroy(Vector* v);

/*
	Adds an element at the end of the Vector
	v: pointer to a Vector
	e: element
*/
void append(Vector* v, Element e);

/*
	Returns the size of a Vector(the number of elements)
	v: pointer to a Vector
	return: int >= 0
*/
int size(Vector* v);

/*
	Returns the element from Vector with the given index,
	NULL if the index is not valid
	v: pointer to a Vector
	index: int >= 0
	return: Element or NULL
*/
Element element(Vector* v, int index);

/*
	Remove the element with the given index from Vector
	v: pointer to a Vector
	index: int >= 0
*/
void remove_element(Vector* v, int index);

void test_vector();

#endif
