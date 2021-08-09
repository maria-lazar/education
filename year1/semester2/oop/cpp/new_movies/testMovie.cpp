#pragma once
#include <assert.h>
#include "testMovie.h"

void testConstructor() {
	Movie m;
	assert(m.getTitle() == "");
	assert(m.getGenre() == "");
	assert(m.getYear() == 0);
	assert(m.getActor() == "");
	Movie m1("a", "b", 1900, "c");
	assert(m1.getTitle() == "a");
	assert(m1.getGenre() == "b");
	assert(m1.getYear() == 1900);
	assert(m1.getActor() == "c");
}

void testGetters() {
	Movie m1("a", "b", 1900, "c");
	assert(m1.getTitle() == "a");
	assert(m1.getGenre() == "b");
	assert(m1.getYear() == 1900);
	assert(m1.getActor() == "c");
}

void testSetters() {
	Movie m1("a", "b", 1900, "c");
	assert(m1.getTitle() == "a");
	assert(m1.getGenre() == "b");
	assert(m1.getYear() == 1900);
	assert(m1.getActor() == "c");
	m1.setTitle("z");
	assert(m1.getTitle() == "z");
	m1.setGenre("x");
	assert(m1.getGenre() == "x");
	m1.setActor("w");
	assert(m1.getActor() == "w");
	m1.setYear(2000);
	assert(m1.getYear() == 2000);
	Movie m("a", "a", 1900, "a");
	string s = m.toString();
}

void testMovieValidator() {
	Movie m1("a", "b", 1900, "c");
	MovieValidator mV;
	mV.validateActor(m1.getActor());
	mV.validateTitle(m1.getTitle());
	mV.validateGenre(m1.getGenre());
	mV.validateYear(m1.getYear());
	Movie m2("", "b", 1900, "c");
	try {
		mV.validateTitle(m2.getTitle());
		assert(false);
	}
	catch (ValidationException) {
		assert(true);
	}
	Movie m3("a", "b1", 1900, "c");
	try {
		mV.validateGenre(m3.getGenre());
		assert(false);
	}
	catch (ValidationException) {
		assert(true);
	}
	Movie m4("a", "b", 1, "c");
	try {
		mV.validateYear(m4.getYear());
		assert(false);
	}
	catch (ValidationException) {
		assert(true);
	}
	Movie m5("a", "b", 1900, "c2");
	try {
		mV.validateActor(m5.getActor());
		assert(false);
	}
	catch (ValidationException) {
		assert(true);
	}
	Movie m6("a", "", 1900, "c2");
	try {
		mV.validateGenre(m6.getGenre());
		assert(false);
	}
	catch (ValidationException) {
		assert(true);
	}
	Movie m7("a", "b", 1900, "");
	try {
		mV.validateActor(m7.getActor());
		assert(false);
	}
	catch (ValidationException) {
		assert(true);
	}
}

void testMovie() {
	testConstructor();
	testGetters();
	testSetters();
	testMovieValidator();
}
