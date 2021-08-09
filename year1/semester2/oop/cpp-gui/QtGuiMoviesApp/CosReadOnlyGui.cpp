#include "CosReadOnlyGui.h"



CosReadOnlyGui::CosReadOnlyGui(MovieService& mS, QWidget *parent): movieService(mS), QWidget(parent, Qt::Window) {

}

void CosReadOnlyGui::paintEvent(QPaintEvent* ev){
	p = new QPainter{ this };
	p->setPen(Qt::blue);
	const vector<Movie>& movies = movieService.getAllShopping();
	for (int i = 0; i < movies.size(); i++) {
		int x, y, z, w;
		x = rand() % 300;
		y = rand() % 300;
		z = rand() % 300;
		w = rand() % 300;
		QRect* rect = new QRect(x, y, z, w);
		int r = rand() % 255;
		int g = rand() % 255;
		int b = rand() % 255;
		p->fillRect(*rect, QColor(r, g, b));
		p->drawRect(*rect);
	}
}


void CosReadOnlyGui::update() {
	repaint();
}

CosReadOnlyGui::~CosReadOnlyGui()
{
}
