#include "domain.h"
#include <stdlib.h>
#include <string.h>
#include <assert.h>

int get_id(Medication* m) {
	return m->id;
}

char* get_name(Medication* m) {
	return m->name;
}

float get_concentration(Medication* m) {
	return m->concentration;
}

int get_quantity(Medication* m) {
	return m->quantity;
}

void set_name(Medication* m, char* n) {
	medication_destroy(m);
	m->name = (char*)malloc(sizeof(char) * (strlen(n) + 1));
	strcpy(m->name, n);
}

void set_concentration(Medication* m, float c) {
	m->concentration = c;
}

void set_quantity(Medication* m, int q) {
	m->quantity = q;
}

void medication_init(Medication* m, int id, char* name, float c) {
	m->id = id;
	m->name = (char*) malloc(sizeof(char) * (strlen(name) + 1));
	strcpy(m->name, name);
	m->concentration = c;
	m->quantity = 1;
}

void medication_destroy(Medication* m) {
	free(m->name);
}

void id_is_valid(int id, char* errors) {
	strcpy(errors, "");
	if (id <= 0) {
		strcat(errors, "Id must be positive\n");
	}
}

void medication_is_valid(int id, char* name, float con, char* errors) {
	id_is_valid(id, errors);
	if (strlen(name) == 0) {
		strcat(errors, "Name must be introduced\n");
	}
	if (con <= 0) {
		strcat(errors, "Concentration must be positive\n");
	}
}

//TESTS

void test_medication_init() {
	Medication m;
	medication_init(&m, 1, "nurofen", 9.5);
	assert(get_id(&m) == 1);
	assert(get_concentration(&m) == 9.5);
	assert(get_quantity(&m) == 1);
	assert(strcmp(get_name(&m), "nurofen") == 0);
	medication_destroy(&m);
}

void test_get_id() {
	Medication m;
	medication_init(&m, 1, "nurofen", 9.5);
	assert(get_id(&m) == 1);
	medication_destroy(&m);
}

void test_get_concentration() {
	Medication m;
	medication_init(&m, 1, "nurofen", 9.5);
	assert(get_concentration(&m) == 9.5);
	medication_destroy(&m);
}

void test_get_quantity() {
	Medication m;
	medication_init(&m, 1, "nurofen", 9.5);
	assert(get_quantity(&m) == 1);
	medication_destroy(&m);
}

void test_get_name() {
	Medication m;
	medication_init(&m, 1, "nurofen", 9.5);
	assert(get_id(&m) == 1);
	medication_destroy(&m);
}

void test_set_name() {
	Medication m;
	medication_init(&m, 1, "nurofen", 9.5);
	assert(strcmp(get_name(&m), "nurofen") == 0);
	set_name(&m, "ibuprofen");
	assert(strcmp(get_name(&m), "ibuprofen") == 0);
	medication_destroy(&m);
}

void test_set_concentration() {
	Medication m;
	medication_init(&m, 1, "nurofen", 9.5);
	assert(get_concentration(&m) == 9.5);
	set_concentration(&m, 10);
	assert(get_concentration(&m) == 10);
	medication_destroy(&m);
}

void test_set_quantity() {
	Medication m;
	medication_init(&m, 1, "nurofen", 9.5);
	assert(get_quantity(&m) == 1);
	set_quantity(&m, 2);
	assert(get_quantity(&m) == 2);
	medication_destroy(&m);
}

void test_medication_is_valid() {
	char errors[80];
	strcpy(errors, "");
	medication_is_valid(1, "nurofen", 9.5, errors);
	assert(strlen(errors) == 0);
	strcpy(errors, "");
	medication_is_valid(-1, "nurofen", 10, errors);
	assert(strcmp(errors, "Id must be positive\n") == 0);
	strcpy(errors, "");
	medication_is_valid(1, "", 10, errors);
	assert(strcmp(errors, "Name must be introduced\n") == 0);
	strcpy(errors, "");
	medication_is_valid(1, "nurofen", -9, errors);
	assert(strcmp(errors, "Concentration must be positive\n") == 0);
	strcpy(errors, "");
	medication_is_valid(-1, "", -1, errors);
	assert(strcmp(errors, "Id must be positive\nName must be introduced\nConcentration must be positive\n") == 0);
}

void test_domain() {
	test_medication_init();
	test_medication_is_valid();
	test_get_concentration();
	test_set_concentration();
	test_get_id();
	test_get_name();
	test_set_name();
	test_get_quantity();
	test_set_quantity();
}