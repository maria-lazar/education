#pragma once
#include <vector>
#include <algorithm>
using std::vector;

class Observer {
public:
	virtual void update() = 0;
	virtual void update2(int i) = 0;
};

class Observable {
private:
	vector<Observer*> observers;

public:
	void addObserver(Observer* o) {
		observers.push_back(o);
	}

	void removeObserver(Observer* o) {
		vector<Observer*>::iterator position = std::find_if(observers.begin(), observers.end(), [&](Observer* o2) {
			return o2 == o;
		});
		if (position != observers.end()) {
			observers.erase(position);
		}
	}
	void notify2(int n) {
		for (int i = 0; i < observers.size(); i++) {
			observers.at(i)->update2(n);
		}
	}

	void notify() {
		for (int i = 0; i < observers.size(); i++) {
			observers.at(i)->update();
		}
	}
};