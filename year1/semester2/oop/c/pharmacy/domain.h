#ifndef DOMAIN_H_
#define DOMAIN_H_

typedef struct {
	int id;
	char* name;
	float concentration;
	int quantity;
} Medication;

/*
	Returns the Medication id
	m: pointer to a Medication
	return: int >= 0
*/
int get_id(Medication* m);

/*
	Returns the Medication name
	m: pointer to a Medication
	return: pointer to c string
*/
char* get_name(Medication* m);

/*
	Returns the Medication concentration
	m: pointer to a Medication
	return: float
*/
float get_concentration(Medication* m);

/*
	Returns the Medication quantity
	m: pointer to a Medication
	return: int >= 0
*/
int get_quantity(Medication* m);

/*
	Sets the Medication name
	m: pointer to a Medication
	n: pointer to c string
*/
void set_name(Medication* m, char* n);

/*
	Sets the Medication concentration
	m: pointer to a Medication
	c: float
*/
void set_concentration(Medication* m, float c);

/*
	Sets the Medication quantity
	m: pointer to a Medication
	q: int >= 0
*/
void set_quantity(Medication* m, int q);

/*
	Initializes a medication with the properties given
	m: pointer to medication
	id: int >= 0
	name: pointer to c string
	c: float
*/
void medication_init(Medication* m, int id, char* name, float c);

/*
	Frees the memory of a medication
	m: pointer to medication
*/
void medication_destroy(Medication* m);

/*
	Validates a medication, sends the error message through the parameter errors
	m: pointer to medication
	errors: pointer to c string
*/
void medication_is_valid(int id, char* name, float con, char* errors);

/*
	Validates id, sends the error messag through the parameter errors
	id: int
	errors: pointer to c string
*/
void id_is_valid(int id, char* errors);

void test_domain();

#endif /* DOMAIN_H_ */