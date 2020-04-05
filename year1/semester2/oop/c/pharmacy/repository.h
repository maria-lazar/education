#include "domain.h"
#include "vector.h"

/*
	Adds a medication to the list of medications
	med: pointer to Medication
*/
void insert_medication(Medication* med);

/*
	Initializes the medications list
*/
void repository_init();

/*
	Frees the memory occupied by medications
*/
void repository_destroy();

/*
	Frees the memory occupied by a vector
*/
void vector_destroy(Vector* v);

/*
	Finds the medication in the list of medications with the given id if it exists,
	returns NULL otherwise
	id: int >= 0
	return: pointer to Medication or NULL
*/
Medication* find_medication(int id);

/*
	Deletes a medication from the list of medications
	m: pointer to Medication
*/
void delete_medication(Medication* m);

/*
	Returns the list of medications
	return: pointer to Vector
*/
Vector* find_all_medications();

/*
	Returns a copy of the list of medications
	return: pointer to Vector
*/
Vector* find_medications_copy();

/*
	Update a medication
	id: int
	name: pointer to c string
	c: float
*/
void update_medication_repository(int id, char* name, float c);

void test_repository();
