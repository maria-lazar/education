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
#include "QtWidgets/QTableWidget"
#include "QtWidgets/QLineEdit"
#include "QtWidgets/QHeaderView"
#include "Change.h"
#include "CosCRUDGui.h"
#include "CosReadOnlyGui.h"
#include "TableViewModel.h"
#include "Observer.h"
#include <vector>
using std::string;

class QtGuiMoviesList : public QWidget, public Observer, public Observable {
	Q_OBJECT

public:
	QtGuiMoviesList(MovieService& mS, std::vector<std::unique_ptr<Change>>& prevChanges, QWidget *parent = Q_NULLPTR);

	void filterByTitleList(string s);

	void filterByYearList(string s);

	void clearBag();

	void update();
	
	void update2(int n) {};
	
private slots:
	void onItemSelected();

	void removeMovie();

	void sortByTitle();

	void sortByActor();

	void sortByYearGenre();

	void undo();

public slots:
	vector<Movie> reloadList(const std::vector<Movie>& movies, int sort, int filter);

	void movieAddSetSelection();

	void movieUpdate();

	void filter(int c, string s);

	void clearFilterList();

	void modifyBagSize();

signals:
	void movieSelected(int index, int list);

private:
	vector<Movie> fillMovieList(std::vector<Movie> movies, int sort, int filter);

	MovieService& movieService;

	QListWidget* list;
	
	QTableWidget* table;

	QTableView* table2;
	TableViewModel* model;

	std::vector<std::unique_ptr<Change>>& prevChanges;

	int sortCriteria;

	int filterCriteria;

	int viewMode;

	int bagSize;

	int year;

	string title;

	QLabel* sortLabel;

	QLabel* filterLabel;

	QLabel* bagLabel;
	QLineEdit* generateTxt;

	QPushButton* removeBtn;
	QPushButton* sortTitleBtn;
	QPushButton* sortActorBtn;
	QPushButton* sortYearGenreBtn;
	QPushButton* undoBtn;
	QPushButton* bagBtn;
	QPushButton* viewBagBtn;
	QPushButton* saveBagBtn;
	QPushButton* clearBagBtn;
	QPushButton* generateBagBtn;
	QPushButton* okBtn;

	vector<CosCRUDGui*> cosuri;
};
