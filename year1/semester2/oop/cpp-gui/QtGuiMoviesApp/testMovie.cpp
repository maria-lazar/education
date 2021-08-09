#pragma once
#include <assert.h>
#include "testMovie.h"

void testConstructor() {
	Movie m;
	assert(m.getTitle() == "");
	assert(m.getGenre() == "");
	assert(m.getYear() == 0);
	assert(m.getActor() == "");
	Movie m1("a", "b", 1, "c");
	assert(m1.getTitle() == "a");
	assert(m1.getGenre() == "b");
	assert(m1.getYear() == 1);
	assert(m1.getActor() == "c");
}

void testGetters() {
	Movie m1("a", "b", 1, "c");
	assert(m1.getTitle() == "a");
	assert(m1.getGenre() == "b");
	assert(m1.getYear() == 1);
	assert(m1.getActor() == "c");
}

void testSetters() {
	Movie m1("a", "b", 1, "c");
	assert(m1.getTitle() == "a");
	assert(m1.getGenre() == "b");
	assert(m1.getYear() == 1);
	assert(m1.getActor() == "c");
	m1.setTitle("z");
	assert(m1.getTitle() == "z");
	m1.setGenre("x");
	assert(m1.getGenre() == "x");
	m1.setActor("w");
	assert(m1.getActor() == "w");
	m1.setYear(100);
	assert(m1.getYear() == 100);
	Movie m("a", "a", 1, "a");
	string s = m.toString();
}

void testMovie() {
	testConstructor();
	testGetters();
	testSetters();
}
