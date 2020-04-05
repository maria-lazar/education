#include "service.h"
#include "domain.h"
#include <string.h>
#include <stdlib.h>
#include <assert.h>
#include "repository.h"
#include "domain.h"

typedef int(*Cmp)(Medication* m1, Medication* m2);

void service_init() {
	repository_init();
}

void service_destroy() {
	repository_destroy();
}

void service_destroy_vector(Vector* v) {
	vector_destroy(v);
}

Vector* get_medications() {
	return find_all_medications();
}

/*
	Compares the names of 2 Medications
	m1: Medication
	m2: Medication
	return: int < 0(m1->name < m2->name); int > 0(m1->name > m2->name); 0(m1->name = m2->name)
*/
int compare_by_name(Medication* m1, Medication* m2) {
	return strcmp(get_name(m1), get_name(m2));
}

/*
	Compares the quantity of 2 Medications
	m1: Medication
	m2: Medication
	return: int < 0(m1->quantity < m2->quantity); int > 0(m1->quantity > m2->quantity); 0(m1->quantity = m2->quantity)
*/
int compare_by_quantity(Medication* m1, Medication* m2) {
	return get_quantity(m1) - get_quantity(m2);
}

/*
	Sorts a list medications by a property
	v: pointer to Vector
	cmp: Cmp(function)
	flag: int
*/
void sort_medications(Vector* v, Cmp cmp, int flag) {
	for (int i = 0; i < size(v) - 1; i++) {
		for (int j = i + 1; j < size(v); j++) {
			Medication* m1 = element(v, i);
			Medication* m2 = element(v, j);
			if (flag * cmp(m1, m2) > 0) {
				Medication* aux = m1;
				v->elems[i] = v->elems[j];
				v->elems[j] = aux;
			}
		}
	}
}

Vector* get_medications_name(int flag) {
	Vector* medications = find_medications_copy();
	if (flag == 1){
		sort_medications(medications, compare_by_name, flag);
	}
	else {
		sort_medications(medications, compare_by_name, -1);
	}
	return medications;
}

Vector* get_medications_quantity(int flag) {
	Vector* medications = find_medications_copy();
	if (flag == 1) {
		sort_medications(medications, compare_by_quantity, flag);
	}
	else {
		sort_medications(medications, compare_by_quantity, -1);
	}
	return medications;
}

int filter_by_quantity(Medication* m, void* arg) {
	return get_quantity(m) - *((int*)arg) <= 0 ? 1 : 0;
}

int filter_by_name(Medication* m, void* arg) {
	return (get_name(m)[0] == *((char*)arg));
}

Vector* filter_medications(Filter f, void* arg) {
	Vector* meds = (Vector*)malloc(sizeof(Vector));
	init(meds);
	Vector* medications = get_medications();
	for (int i = 0; i < size(medications); i++) {
		Medication* m = element(medications, i);
		if (f(m, arg)) {
			append(meds, m);
		}
	}
	return meds;
}

void create_medication(int id, char* name, float c, char* result) {
	Medication* m = find_medication(id);
	if (m == NULL) {
		medication_is_valid(id, name, c, result);
		if (strlen(result) > 0) {
			return;
		}
		m = (Medication*) malloc(sizeof(Medication));
		medication_init(m, id, name, c);
		insert_medication(m);
		strcpy(result, "Medication added");
	}
	else {
		set_quantity(m, get_quantity(m) + 1);
		strcpy(result, "Medication updated");
	}
}

void update_medication_service(int id, char* name, float c, char* result) {
	id_is_valid(id, result);
	if (strlen(result) > 0) {
		return;
	}
	Medication* m = find_medication(id);
	if (m == NULL) {
		strcpy(result, "Medication doesn't exist\n");
		return;
	}
	medication_is_valid(id, name, c, result);
	if (strlen(result) > 0) {
		return;
	}
	update_medication_repository(id, name, c);
	strcpy(result, "Medication updated");
}

void remove_medication_service(int id, char* result) {
	id_is_valid(id, result);
	if (strlen(result) > 0) {
		return;
	}
	Medication* m = find_medication(id);
	if (m == NULL) {
		strcpy(result, "Medication doesn't exist\n");
		return;
	}
	delete_medication(m);
	strcpy(result, "Medication deleted");
}

//TESTS

void test_create_medication() {
	service_init();
	char result[80];
	strcpy(result, "");
	create_medication(1, "nurofen", 9.5, result);
	Vector* v;
	v= get_medications();
	Medication* m; 
	m = element(v, 0);
	assert(get_id(m) == 1);
	assert(get_concentration(m) == 9.5);
	assert(get_quantity(m) == 1);
	assert(strcmp(get_name(m), "nurofen") == 0);
	strcpy(result, "");
	create_medication(2, "ibuprofen", 9, result);
	v = get_medications();
	m = element(v, 1);
	assert(get_id(m) == 2);
	assert(get_concentration(m) == 9);
	assert(get_quantity(m) == 1);
	assert(strcmp(get_name(m), "ibuprofen") == 0);
	create_medication(1, "nurofen", 9.5, result);
	v = get_medications();
	m = element(v, 0);
	assert(size(v) == 2);
	assert(get_quantity(m) == 2);
	strcpy(result, "");
	create_medication(-1, "nurofen", -1, result);
	v = get_medications();
	assert(size(v) == 2);
	assert(strlen(result) > 0);
	service_destroy();
}

void test_update_medication_service() {
	service_init();
	char result[80];
	strcpy(result, "");
	create_medication(1, "nurofen", 9.5, result);
	Vector* v;
	v = get_medications();
	Medication* m;
	m = element(v, 0);
	assert(get_id(m) == 1);
	assert(get_concentration(m) == 9.5);
	assert(get_quantity(m) == 1);
	assert(strcmp(get_name(m), "nurofen") == 0);
	strcpy(result, "");
	update_medication_service(1, "ibuprofen", 9, result);
	v = get_medications();
	m = element(v, 0);
	assert(get_id(m) == 1);
	assert(get_concentration(m) == 9);
	assert(strcmp(get_name(m), "ibuprofen") == 0);
	strcpy(result, "");
	update_medication_service(-1, "ibuprofen", 9, result);
	assert(strcmp(result, "Id must be positive\n") == 0);
	update_medication_service(2, "ibuprofen", 9, result);
	assert(strcmp(result, "Medication doesn't exist\n") == 0);
	update_medication_service(1, "", -9, result);
	assert(strcmp(result, "Name must be introduced\nConcentration must be positive\n") == 0);
	service_destroy();
}

void test_remove_medication_service() {
	service_init();
	char result[80];
	strcpy(result, "");
	remove_medication_service(1, result);
	assert(strcmp(result, "Medication doesn't exist\n") == 0);
	strcpy(result, "");
	create_medication(1, "nurofen", 9.5, result);
	strcpy(result, "");
	remove_medication_service(2, result);
	assert(strcmp(result, "Medication doesn't exist\n") == 0);
	remove_medication_service(1, result);
	Vector* medications = get_medications();
	assert(size(medications) == 0);
	service_destroy();
}

void test_get_medications() {
	service_init();
	char result[80];
	Vector* v;
	v = get_medications();
	assert(size(v) == 0);
	strcpy(result, "");
	create_medication(1, "nurofen", 9.5, result);
	v = get_medications();
	assert(size(v) == 1);
	Medication* m;
	m = element(v, 0);
	assert(get_id(m) == 1);
	assert(get_concentration(m) == 9.5);
	assert(get_quantity(m) == 1);
	assert(strcmp(get_name(m), "nurofen") == 0);
	service_destroy();
}

void test_compare_by_name() {
	service_init();
	char result[80];
	create_medication(1, "nurofen", 10, result);
	create_medication(2, "ibuprofen", 11, result);
	Vector* v = get_medications();
	assert(compare_by_name(element(v, 0), element(v, 1)) > 0);
	update_medication_service(1, "n", 11, result);
	update_medication_service(2, "n", 12, result);
	assert(compare_by_name(element(v, 0), element(v, 1)) == 0);
	update_medication_service(1, "i", 11, result);
	assert(compare_by_name(element(v, 0), element(v, 1)) < 0);
	service_destroy();
}

void test_compare_by_quantity() {
	service_init();
	char result[80];
	create_medication(1, "nurofen", 10, result);
	create_medication(1, "nurofen", 10, result);
	create_medication(2, "ibuprofen", 11, result);
	Vector* v = get_medications();
	assert(compare_by_quantity(element(v, 0), element(v, 1)) > 0);
	create_medication(2, "ibuprofen", 11, result);
	assert(compare_by_quantity(element(v, 0), element(v, 1)) == 0);
	create_medication(2, "i", 11, result);
	assert(compare_by_quantity(element(v, 0), element(v, 1)) < 0);
	service_destroy();
}

void test_sort_medications() {
	service_init();
	char result[80];
	create_medication(1, "b", 1, result);
	create_medication(1, "b", 1, result);
	create_medication(2, "a", 10, result);
	create_medication(3, "c", 11, result);
	create_medication(3, "c", 11, result);
	Vector* v = find_medications_copy();
	sort_medications(v, compare_by_name, 1);
	assert(get_id((Medication*)element(v, 0)) == 2);
	assert(get_id((Medication*)element(v, 1)) == 1);
	assert(get_id((Medication*)element(v, 2)) == 3);
	sort_medications(v, compare_by_name, -1);
	assert(get_id((Medication*)element(v, 0)) == 3);
	assert(get_id((Medication*)element(v, 1)) == 1);
	assert(get_id((Medication*)element(v, 2)) == 2);
	sort_medications(v, compare_by_quantity, 1);
	assert(get_id((Medication*)element(v, 0)) == 2);
	assert(get_id((Medication*)element(v, 1)) == 1);
	assert(get_id((Medication*)element(v, 2)) == 3);
	sort_medications(v, compare_by_name, -1);
	assert(get_id((Medication*)element(v, 0)) == 3);
	assert(get_id((Medication*)element(v, 1)) == 1);
	assert(get_id((Medication*)element(v, 2)) == 2);
	service_destroy_vector(v);
	service_destroy();
}

void test_get_medications_name() {
	service_init();
	char result[80];
	create_medication(1, "b", 1, result);
	create_medication(1, "b", 1, result);
	create_medication(2, "a", 10, result);
	create_medication(3, "c", 11, result);
	create_medication(3, "c", 11, result);
	Vector* v = get_medications_name(1);
	assert(get_id((Medication*)element(v, 0)) == 2);
	assert(get_id((Medication*)element(v, 1)) == 1);
	assert(get_id((Medication*)element(v, 2)) == 3);
	vector_destroy(v);
	v = get_medications_name(-1);
	assert(get_id((Medication*)element(v, 0)) == 3);
	assert(get_id((Medication*)element(v, 1)) == 1);
	assert(get_id((Medication*)element(v, 2)) == 2);
	service_destroy_vector(v);
	service_destroy();
}

void test_get_medications_quantity() {
	service_init();
	char result[80];
	create_medication(1, "b", 1, result);
	create_medication(1, "b", 1, result);
	create_medication(2, "a", 10, result);
	create_medication(3, "c", 11, result);
	create_medication(3, "c", 11, result);
	Vector* v = get_medications_quantity(1);
	assert(get_id((Medication*)element(v, 0)) == 2);
	assert(get_id((Medication*)element(v, 1)) == 1);
	assert(get_id((Medication*)element(v, 2)) == 3);
	vector_destroy(v);
	v = get_medications_quantity(-1);
	assert(get_id((Medication*)element(v, 0)) == 1);
	assert(get_id((Medication*)element(v, 1)) == 3);
	assert(get_id((Medication*)element(v, 2)) == 2);
	service_destroy_vector(v);
	service_destroy();
}

void test_filter_medications() {
	service_init();
	char result[80];
	create_medication(1, "ae", 1, result);
	create_medication(1, "ae", 1, result);
	create_medication(2, "ai", 10, result);
	create_medication(3, "c", 11, result);
	create_medication(3, "c", 11, result);
	create_medication(3, "c", 11, result);
	char c = 'z';
	Vector* v = filter_medications(filter_by_name, &c);
	assert(size(v) == 0);
	destroy(v);
	free(v);
	c = 'a';
	v = filter_medications(filter_by_name, &c);
	assert(size(v) == 2);
	assert(get_id((Medication*)element(v, 0)) == 1);
	assert(get_id((Medication*)element(v, 1)) == 2);
	destroy(v);
	free(v);
	int q = 1;
	v = filter_medications(filter_by_quantity, &q);
	assert(size(v) == 1);
	assert(get_id((Medication*)element(v, 0)) == 2);
	destroy(v);
	free(v);
	service_destroy();
}

void test_service() {
	test_create_medication();
	test_get_medications();
	test_update_medication_service();
	test_remove_medication_service();
	test_compare_by_name();
	test_compare_by_quantity();
	test_sort_medications();
	test_get_medications_name();
	test_get_medications_quantity();
	test_filter_medications();
}
