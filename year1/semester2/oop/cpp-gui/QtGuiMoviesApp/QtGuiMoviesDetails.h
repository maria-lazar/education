#pragma once
#include "MovieService.h"
#include "QtWidgets/QHBoxLayout"
#include "QtWidgets/QVBoxLayout"
#include "QtWidgets/QLabel"
#include "QtWidgets/QGridLayout"
#include "QtWidgets/QFormLayout"
#include "QtWidgets/QPushButton"
#include "QtWidgets/QMessageBox"
#include "QtWidgets/QListWidgetItem"
#include "QtWidgets/QLineEdit"
#include "Change.h"
#include "Observer.h"
#include <vector>

class QtGuiMoviesDetails : public QWidget {
	Q_OBJECT

public:
	QtGuiMoviesDetails(MovieService& mS, std::vector<std::unique_ptr<Change>>& prevChanges, QWidget *parent = Q_NULLPTR);

private slots:
	void add();

	void update();

	void filterTitle();

	void filterYear();

	void clearFilter();
	
	void addToBag();

public slots:
	void fillSelectedInfo(int index, int list);

	void bagView();

	void bagHide();

signals:
	void addSignal(QString title, QString genre, QString year, QString actor);

	void updateSignal(QString title, QString genre, QString year, QString actor);

	void filteredByTitle();

	void filteredByYear();

	void clearedFilter();
	
	void addedToBag();

private:
	std::vector<std::unique_ptr<Change>>& prevChanges;
	QLineEdit* txtTitle;
	QLineEdit* txtGenre;
	QLineEdit* txtYear;
	QLineEdit* txtActor;
	MovieService& movieService;
	QPushButton* addBtn;
	QPushButton* updateBtn;
	QPushButton* filterTitleBtn;
	QPushButton* filterYearBtn;
	QPushButton* clearFilterBtn;
	QPushButton* addToBagBtn;
};

