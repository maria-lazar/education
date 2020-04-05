#ifndef CLI_H_
#define CLI_H_

typedef void(*Executor)();

typedef struct {
	char* cmd;
	char* description;
	Executor f;
} MenuItem;

/*
	Run the application
*/
void cli_run();

/*
	Creates the menu list
*/
void cli_init();

/*
	Frees the memory occupied by the menu
*/
void cli_destroy();

/*
	Adds a menuItem to the menu
	cmd: pointer to c string
	description: pointer to c string
	f: Executor
*/
void cli_add_menu_item(char* cmd, char* description, const Executor f);

void test_cli();

#endif