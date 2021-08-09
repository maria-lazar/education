#pragma once
#include <QtWidgets/QWidget>
#include <QtWidgets/qtablewidget.h>
#include <QtWidgets/qboxlayout.h>
#include <QtWidgets/qpushbutton.h>
#include <QtWidgets/qlineedit.h>
#include <QtWidgets/qtableview.h>
#include "MovieService.h"
#include <vector>
#include "Movie.h"
#include "Observer.h"
#include "TableViewModel.h"
using std::vector;

class CosCRUDGui: public QWidget, public Observer//, public Observable
{
	Q_OBJECT

private:
	QTableWidget* table;
	QTableView* table2;
	TableViewModel* model;
	MovieService& movieService;
	void reloadList();
	std::function<void()> funcClear;
	std::function<void(int n)> funcGenerate;
	QPushButton* okBtn;
	QLineEdit* generateTxt;

public:
	CosCRUDGui(MovieService& mS, std::function<void()> f, std::function<void(int n)> g, QWidget *parent = Q_NULLPTR);
	~CosCRUDGui();

	void update() override;
	void update2(int n) override;

};

