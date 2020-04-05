#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include "cli.h"
#include "vector.h"

Vector menu_items;

void cli_init() {
	init(&menu_items);
}

/*
	Frees the memory occupied by a menu item
	m: pointer to MenuItem
*/
void menu_item_destroy(MenuItem* m) {
	free(m->cmd);
	free(m->description);
}

void cli_destroy() {
	for (int i = 0; i < size(&menu_items); i++) {
		MenuItem* m = element(&menu_items, i);
		menu_item_destroy(m);
		free(m);
	}
	destroy(&menu_items);
}

/*
	Initializes and allocates memory for a MenuItem
	m: pointer to MenuItem
	cmd: pointer to c string
	description: pointer to c string
	f: Executor
*/
void menu_item_init(MenuItem* m, char* cmd, char* description, const Executor f) {
	m->cmd = (char*)malloc((strlen(cmd) + 1) * sizeof(char));
	m->description = (char*)malloc((strlen(description) + 1) * sizeof(char));
	strcpy(m->cmd, cmd);
	strcpy(m->description, description);
	m->f = f;
}

void cli_add_menu_item(char* cmd, char* description, const Executor f) {
	MenuItem* menu_item = (MenuItem*)malloc(sizeof(MenuItem));
	menu_item_init(menu_item, cmd, description, f);
	append(&menu_items, menu_item);
}

/*
	Print menu
*/
void menu() {
	printf("\nexit - Exit\n");
	for (int i = 0; i < size(&menu_items); i++) {
		MenuItem* m = element(&menu_items, i);
		printf("%s - %s\n", m->cmd, m->description);
	}
}

/*
	Finds by command the MenuItem position in the menu
	command: pointer to c string
	return: int: > 0 if the MenuItem is in the menu,
			-1 otherwise
*/
int find_menu_item(char* command) {
	for (int i = 0; i < size(&menu_items); i++) {
		if (strcmp(((MenuItem*)element(&menu_items, i))->cmd, command) == 0) {
			return i;
		}
	}
	return -1;
}

void cli_run() {
	char cmd[20];
	while (1) {
		menu();
		scanf("%s", cmd);
		if (strcmp(cmd, "exit") == 0) {
			break;
		}
		int index = find_menu_item(cmd);
		if (index == -1) {
			printf("invalid command");
			continue;
		}
		((MenuItem*)element(&menu_items, index))->f();
	}
}

//TESTS

void test_menu_item_init() {
	MenuItem m;
	menu_item_init(&m, "a", "b", test_cli);
	assert(strcmp(m.cmd, "a") == 0);
	assert(strcmp(m.description, "b") == 0);
	assert(m.f == test_cli);
	menu_item_destroy(&m);
}

void test_cli_add_menu_item() {
	cli_init();
	cli_add_menu_item("a", "b", test_cli);
	assert(size(&menu_items) == 1);
	MenuItem* m = (MenuItem*)element(&menu_items, 0);
	assert(strcmp(m->cmd, "a") == 0);
	assert(strcmp(m->description, "b") == 0);
	assert(m->f == test_cli);
	cli_destroy();
}

void test_find_menu_item() {
	cli_init();
	int index;
	index = find_menu_item("add");
	assert(index == -1);
	cli_add_menu_item("add", "b", test_cli);
	index = find_menu_item("add");
	assert(index == 0);
	cli_add_menu_item("delete", "b", test_cli);
	cli_add_menu_item("update", "b", test_cli);
	index = find_menu_item("delete");
	assert(index == 1);
	index = find_menu_item("update");
	assert(index == 2);
	index = find_menu_item("add");
	assert(index == 0);
	cli_destroy();
}

void test_cli() {
	test_menu_item_init();
	test_cli_add_menu_item();
	test_find_menu_item();
}