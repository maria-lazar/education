#pragma once
#include <QtWidgets/QWidget>
#include "MovieService.h"
#include <QtWidgets/qboxlayout.h>
#include <qpainter.h>
#include <vector>
#include "Movie.h"
#include "Observer.h"
using std::vector;

class CosReadOnlyGui: public QWidget, public Observer
{

private:
	MovieService& movieService;

	QPainter* p;

public:
	CosReadOnlyGui(MovieService& mS, QWidget *parent = Q_NULLPTR);
	
	~CosReadOnlyGui();

	void update();
	void update2(int n) {};
	
	void paintEvent(QPaintEvent* ev);
};

