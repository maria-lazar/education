#define _CRTDBG_MAP_ALLOC
#include <stdlib.h>
#include <crtdbg.h>
#include <stdio.h>
#include <string.h>
#include "domain.h"
#include "cli.h"
#include "service.h"
#include "vector.h"
#include "repository.h"

void print_medications(Vector* medications) {
	if (size(medications) == 0) {
		printf("No medications found");
		return;
	}
	for (int i = 0; i < size(medications); i++) {
		Medication* m = element(medications, i);
		printf("id: %d name: %s concentration: %f quantity: %d\n", get_id(m),
			get_name(m), get_concentration(m), get_quantity(m));
	}
}

void add_medication() {
	int id;
	char name[50];
	float concentration;
	printf("id: ");
	scanf("%d", &id);
	printf("name: ");
	scanf("%s", name);
	printf("concentration: ");
	scanf("%f", &concentration);
	char result[80];
	strcpy(result, "");
	create_medication(id, name, concentration, result);
	printf("%s", result);
}

void update_medication() {
	int id;
	char name[50];
	float concentration;
	printf("id: ");
	scanf("%d", &id);
	printf("name: ");
	scanf("%s", name);
	printf("concentration: ");
	scanf("%f", &concentration);
	char result[80];
	strcpy(result, "");
	update_medication_service(id, name, concentration, result);
	printf("\n%s", result);
}

void remove_medication() {
	int id;
	printf("id: ");
	scanf("%d", &id);
	char result[80];
	strcpy(result, "");
	remove_medication_service(id, result);
	printf("\n%s", result);
}

void show_medications() {
	Vector* medications = get_medications();
	print_medications(medications);
}

void show_medications_name() {
	printf("	1 - ascending order\n");
	printf("	0 - descending order\n");
	int flag;
	scanf("%d", &flag);
	if (flag == 1 || flag == 0) {
		Vector* medications = get_medications_name(flag);
		print_medications(medications);
		service_destroy_vector(medications);
	}
	else {
		printf("invalid value\n");
	}
}

void show_medications_quantity() {
	printf("	1 - ascending order\n");
	printf("	0 - descending order\n");
	int flag;
	scanf("%d", &flag);
	if (flag == 1 || flag == 0) {
		Vector* medications = get_medications_quantity(flag);
		print_medications(medications);
		service_destroy_vector(medications);
	}
	else {
		printf("invalid value\n");
	}
}

void filter_medications_quantity() {
	int q;
	printf("quantity: ");
	scanf("%d", &q);
	char errors[30];
	id_is_valid(q, errors);
	if (strlen(errors) > 0) {
		printf("invalid value\n");
	}
	else {
		Vector* meds = filter_medications(filter_by_quantity, &q);
		print_medications(meds);
		destroy(meds);
		free(meds);
	}
}

void filter_medications_name() {
	char c;
	printf("character: ");
	scanf(" %c", &c);
	if (c == '\0') {
		printf("invalid value\n");
	}
	else{
		Vector* meds = filter_medications(filter_by_name, &c);
		print_medications(meds);
		destroy(meds);
		free(meds);
	}
}

void tests() {
	test_vector();
	test_cli();
	test_repository();
	test_service();
	test_domain();
}

int main() {
	_CrtSetDbgFlag(_CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF);
	tests();
	service_init();
	cli_init();
	cli_add_menu_item("add", "Add a medication", add_medication);
	cli_add_menu_item("show", "Show medications", show_medications);
	cli_add_menu_item("update", "Update medication", update_medication);
	cli_add_menu_item("remove", "Remove medication", remove_medication);
	cli_add_menu_item("show-name", "Show medications ordered by name", show_medications_name);
	cli_add_menu_item("show-quantity", "Show medications ordered by quantity", show_medications_quantity);
	cli_add_menu_item("filter-quantity", "Filter medications by quantity", filter_medications_quantity);
	cli_add_menu_item("filter-name", "Filter medications by the first letter of the name", filter_medications_name);
	cli_run();
	cli_destroy();
	service_destroy();
	_CrtDumpMemoryLeaks();
	return 0;
}