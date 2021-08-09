#pragma once
#include <exception>
#include <string>

class ValidationException: public std::exception
{
	std::string message;
public:
	ValidationException(std::string m) {
		message = m;
	}
	const char * what() const throw() {
		return message.c_str();
	}
};

class StorageException : public std::exception
{
	std::string message;
public:
	StorageException(std::string m) {
		message = m;
	}
	const char * what() const throw(){
		return message.c_str();
	}
};