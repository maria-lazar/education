#include "repository.h"
#include "vector.h"
#include "domain.h"
#include <stdlib.h>
#include <assert.h>
#include <string.h>

Vector medications;

void repository_init() {
	init(&medications);
}

void repository_destroy() {
	for (int i = 0; i < size(&medications); i++) {
		Medication* m = element(&medications, i);
		medication_destroy(m);
		free(m);
	}
	destroy(&medications);
}

void vector_destroy(Vector* v) {
	for (int i = 0; i < size(v); i++) {
		Medication* m = element(v, i);
		medication_destroy(m);
		free(m);
	}
	destroy(v);
	free(v);
}

void insert_medication(Medication* med) {
	append(&medications, med);
}

Medication* find_medication(int id) {
	for (int i = 0; i < size(&medications); i++) {
		Medication* m = element(&medications, i);
		if (get_id(m) == id) {
			return m;
		}
	}
	return NULL;
}

/*
	Returns the index from the list of medications of the medication with the given id,
	-1 if the medication doesn't exist
	id: int >= 0
*/
int find_medication_index(int id) {
	for (int i = 0; i < size(&medications); i++) {
		Medication* m = element(&medications, i);
		if (get_id(m) == id) {
			return i;
		}
	}
	return -1;
}

void delete_medication(Medication* m) {
	int i = find_medication_index(get_id(m));
	remove_element(&medications, i);
	medication_destroy(m);
	free(m);
}

void update_medication_repository(int id, char* name, float c) {
	Medication* m = find_medication(id);
	set_name(m, name);
	set_concentration(m, c);
}

Vector* find_all_medications() {
	return &medications;
}

Vector* find_medications_copy() {
	Vector* v = (Vector*)malloc(sizeof(Vector));
	init(v);
	for (int i = 0; i < size(&medications); i++) {
		Medication* m = (Medication*)malloc(sizeof(Medication));
		Medication* m2 = (Medication*)element(&medications, i);
		medication_init(m, get_id(m2), get_name(m2), get_concentration(m2));
		set_quantity(m, get_quantity(m2));
		append(v, m);
	}
	return v;
}


//TESTS

void test_insert_medication() {
	repository_init();
	assert(size(&medications) == 0);
	Medication* m = (Medication*) malloc(sizeof(Medication));
	medication_init(m, 1, "nurofen", 9.5);
	insert_medication(m);
	assert(size(&medications) == 1);
	Medication* med = element(&medications, 0);
	assert(get_id(med) == 1);
	assert(strcmp(get_name(med), "nurofen") == 0);
	assert(get_concentration(med) == 9.5);
	assert(get_quantity(med) == 1);
	repository_destroy();
}

void test_find_medication() {
	repository_init();
	assert(find_medication(1) == NULL);
	Medication* m = (Medication*)malloc(sizeof(Medication));
	medication_init(m, 1, "nurofen", 9.5);
	insert_medication(m);
	Medication* m1 = (Medication*)malloc(sizeof(Medication));
	medication_init(m1, 2, "ibuprofen", 10.5);
	insert_medication(m1);
	Medication* med;
	med = find_medication(1);
	assert(med == m);
	med = find_medication(2);
	assert(med == m1);
	med = find_medication(5);
	assert(med == NULL);
	repository_destroy();
}

void test_find_medication_index() {
	repository_init();
	assert(find_medication_index(1) == -1);
	Medication* m = (Medication*)malloc(sizeof(Medication));
	medication_init(m, 1, "nurofen", 9.5);
	insert_medication(m);
	Medication* m1 = (Medication*)malloc(sizeof(Medication));
	medication_init(m1, 2, "ibuprofen", 10.5);
	insert_medication(m1);
	int i;
	i = find_medication_index(1);
	assert(i == 0);
	i = find_medication_index(2);
	assert(i == 1);
	i = find_medication_index(5);
	assert(i == -1);
	repository_destroy();
}

void test_delete_medication() {
	repository_init();
	Medication* m = (Medication*)malloc(sizeof(Medication));
	Medication* m1 = (Medication*)malloc(sizeof(Medication));
	medication_init(m, 1, "nurofen", 9.5);
	medication_init(m1, 2, "ibuprofen", 10.5);
	insert_medication(m);
	insert_medication(m1);
	delete_medication(m);
	assert(size(&medications) == 1);
	Medication* med = element(&medications, 0);
	assert(get_id(med) == 2);
	assert(strcmp(get_name(med), "ibuprofen") == 0);
	assert(get_concentration(med) == 10.5);
	assert(get_quantity(med) == 1);
	delete_medication(m1);
	assert(size(&medications) == 0);
	repository_destroy();
}

void test_find_all_medications() {
	repository_init();
	Medication* m = (Medication*)malloc(sizeof(Medication));
	medication_init(m, 1, "nurofen", 9.5);
	insert_medication(m);
	Medication* m1 = (Medication*)malloc(sizeof(Medication));
	medication_init(m1, 2, "ibuprofen", 10);
	insert_medication(m1);
	Medication* med = element(&medications, 0);
	assert(get_id(med) == 1);
	assert(strcmp(get_name(med), "nurofen") == 0);
	assert(get_concentration(med) == 9.5);
	assert(get_quantity(med) == 1);
	med = element(&medications, 1);
	assert(get_id(med) == 2);
	assert(strcmp(get_name(med), "ibuprofen") == 0);
	assert(get_concentration(med) == 10);
	assert(get_quantity(med) == 1);
	repository_destroy();
}

void test_find_medications_copy() {
	repository_init();
	Medication* m = (Medication*)malloc(sizeof(Medication));
	medication_init(m, 1, "nurofen", 9.5);
	insert_medication(m);
	Medication* m1 = (Medication*)malloc(sizeof(Medication));
	medication_init(m1, 2, "ibuprofen", 10);
	insert_medication(m1);
	Vector* v = find_medications_copy();
	assert(element(v, 0) != element(&medications, 0));
	assert(element(v, 1) != element(&medications, 1));
	vector_destroy(v);
	repository_destroy();
}

void test_repository() {
	test_insert_medication();
	test_find_medication();
	test_find_medication_index();
	test_delete_medication();
	test_find_all_medications();
	test_find_medications_copy();
}