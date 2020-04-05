#include "vector.h"
#include "domain.h"

typedef int(*Filter)(Medication* m, void* arg);

/*
	Initializes the list of medications
*/
void service_init();

/*
	Frees the memory occupied by medications
*/
void service_destroy();

/*
	Frees the memory occupied by a vector of medications
*/
void service_destroy_vector(Vector* v);

/*
	Adds a medication with the given properties to the list of medications if it doesn't already exist in the list,
	updates quantity if there is already one,
	returns a message saying if the medication to be added is invalid
	id: int >= 0
	name: pointer to c string
	c: float
	result: pointer to c string
*/
void create_medication(int id, char* name, float c, char* result);

/*
	Updates the name and concentration of the medication with the given id,
	if it doesn't exist one or the medication is invalid sends a message through the result parameter
	id: int >= 0
	name: pointer to c string
	c: float
	errors: pointer to c string
*/
void update_medication_service(int id, char* name, float c, char* result);

/*
	Removes the medication with the given id,
	if it doesn't exist one or the id is invalid sends a message through the result parameter
	id: int >= 0
	result: pointer to c string
*/
void remove_medication_service(int id, char* result);

/*
	Returns the list of medications
	return: pointer to Vector
*/
Vector* get_medications();

/*
	Returns the list of medications sorted by name
	If flag = 1: ascending, else descending
	flag: int
	return: pointer to Vector
*/
Vector* get_medications_name(int flag);

/*
	Returns the list of medications sorted by quantity
	If flag = 1: ascending, else descending
	flag: int
	return: pointer to Vector
*/
Vector* get_medications_quantity(int flag);

/*
	Verifies if a medication has the quantity smaller than the one given
	m: pointer to Medication
	arg: pointer to void
	return: 1 if the quantity <= arg, 0 otherwise
*/
int filter_by_quantity(Medication* m, void* arg);

/*
	Verifies if a medication name starts with the letter(parameter) given
	m: pointer to Medication
	arg: pointer to void
	return: 1 if the name starts with arg, 0 otherwise
*/
int filter_by_name(Medication* m, void* arg);

/*
	Returns the list of medications with a given property
	f: Filter(function)
	arg: pointer to void
	return: pointer to Vector
*/
Vector* filter_medications(Filter f, void* arg);

void test_service();