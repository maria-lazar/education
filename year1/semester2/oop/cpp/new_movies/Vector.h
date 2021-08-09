#pragma once
#include <exception>
#include <iostream>

template <typename T> class myvector;

//represents a SLL node
template <typename T>
class Nod {
	friend class myvector<T>;
private:
	T val;
	Nod* next;
public:

	//constructor
	Nod() : next{ nullptr } {}

	//destructor
	~Nod() {}

};

template <typename T>
class myvector
{
private:
	size_t _size;
	Nod<T>* head;
	Nod<T>* tail;

	/**
	* Destroys the current SLL representation
	*/
	void destroyList();

	/**
	* Copies v's SLL representation to this
	*/
	void copyList(const myvector<T> & v);
public:
	/**
	* Creates an empty vector
	*/
	myvector() : _size{ 0 }, head{ nullptr }, tail{ nullptr } { std::cout << "myvector()\n"; }
	
	/**
	* Initializes this by copying v's representation
	*/
	myvector(const myvector<T>& v);

	/**
	* Initializes this by moving v's representation
	*/
	myvector(myvector<T>&& v);

	/**
	* Copies v's representation into this
	*/
	myvector<T>& operator=(const myvector<T>& v);

	/**
	* Moves v's representation into this
	*/
	myvector<T>& operator=(myvector<T>&& v);

	/**
	* Deallocates this SLL representation
	*/
	~myvector();

	/**
	* Returns the element at a given index
	* index >= 0 and index < _size
	* return: the reference of "this[index]"
	* Throws out_of_range exception if index is invalid
	*/
	T& at(const size_t index) const;

	/**
	* Returns the last element
	* return: the reference of "this[_size - 1]"
	* Throws out_of_range exception if the vector is empty
	*/
	const T& back() const;

	/**
	* Erases the element of the given index
	* index >= 0 and index < _size
	* Throws out_of_range exception if index is invalid
	*/
	void erase(const size_t index);

	/**
	* Adds e to this SLL representation
	*/
	void push_back(T e);

	/**
	* Returns the number of elements of this vector
	* return: size_t
	*/
	const size_t size() const { return _size; }
};

template<typename T>
class IteratorVector {
private:
	const myvector<T>& v;
	int poz;
public:
	IteratorVector(const myvector<T>& v) noexcept;
	
	IteratorVector(const myvector<T>& v, int poz) noexcept;

	bool valid() const;

	T& element() const;

	void next();
};

template<typename T>
IteratorVector<T>::IteratorVector(const myvector<T>& v) noexcept : v{ v } {}

template<typename T>
IteratorVector<T>::IteratorVector(const myvector<T>& v, int poz) noexcept : v{ v }, poz{ poz } {}

template<typename T>
bool IteratorVector<T>::valid() const {
	return poz < v._size;
}

template<typename T>
T& IteratorVector<T>::element() const {
	return v.at(poz);
}

template<typename T>
void IteratorVector<T>::next() {
	poz++;
}

template<typename T>
void myvector<T>::copyList(const myvector<T> & v)
{
	_size = 0;
	head = nullptr;
	tail = nullptr;
	Nod<T>* p = v.head;
	while (p != nullptr) {
		push_back(p->val);
		p = p->next;
	}
}

template <typename T>
myvector<T>::myvector(const myvector<T>& v) {
	copyList(v);
	std::cout << "myvector(const myvector<T>& v)\n";
}

template <typename T>
myvector<T>::myvector(myvector<T>&& v) {
	head = v.head;
	tail = v.tail;
	_size = v._size;
	v.head = nullptr;
	v.tail = nullptr;
	std::cout << "myvector(const myvector<T>&& v)\n";
}

template <typename T>
myvector<T>& myvector<T>::operator=(const myvector<T>& v) {
	destroyList();
	copyList(v);
	std::cout << "operator=(const myvector<T>& v)\n";
	return *this;
}

template <typename T>
myvector<T>& myvector<T>::operator=(myvector<T>&& v) {
	destroyList();
	head = v.head;
	tail = v.tail;
	_size = v._size;
	v.head = nullptr;
	v.tail = nullptr;
	v._size = 0;
	std::cout << "operator=(const myvector<T>&& v)\n";
	return *this;
}

template <typename T>
void myvector<T>::push_back(T e) {
	Nod<T>* n = new Nod<T>;
	n->val = e;
	n->next = nullptr;
	if (_size == 0) {
		head = tail = n;
	}
	else {
		tail->next = n;
		tail = n;
	}
	_size++;
}

template <typename T>
const T& myvector<T>::back() const {
	if (tail == nullptr) {
		throw std::out_of_range("No elements");
	}
	return tail->val;
}

template <typename T>
T& myvector<T>::at(const size_t index) const {
	if (index < 0 || index >= _size) {
		throw std::out_of_range("Index out of bounds");
	}
	Nod<T>* p = head;
	size_t i = 0;
	while (i != index) {
		p = p->next;
		i++;
	}
	return p->val;
}

template <typename T>
void myvector<T>::erase(const size_t index) {
	if (index < 0 || index >= _size) {
		throw std::out_of_range("Index out of bounds");
	}
	if (index == 0) {
		Nod<T>* p = head;
		head = p->next;
		if (head == tail) {
			tail = nullptr;
		}
		delete p;
	}
	else {
		Nod<T>* p = head;
		size_t i = 0;
		while (i != index - 1) {
			p = p->next;
			i++;
		}
		Nod<T>* q = p->next;
		p->next = q->next;
		if (tail == q) {
			tail = p;
		}
		delete q;
	}
	_size--;
}

template <typename T>
myvector<T>::~myvector()
{
	destroyList();
	std::cout << "~myvector()\n";
}

template<typename T>
void myvector<T>::destroyList()
{
	Nod<T>* p = head;
	while (p != nullptr) {
		Nod<T>* next = p->next;
		delete p;
		p = next;
	}
}

