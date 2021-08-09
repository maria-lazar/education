#pragma once

#include <QtWidgets/QWidget>
#include <QtWidgets/QListWidget>
#include <QtWidgets/QLabel>
#include <QtWidgets/QHBoxLayout>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QMainWindow>
#include <vector>
#include "MovieService.h"
#include "QtGuiMoviesList.h"
#include "Change.h"
#include "QtGuiMoviesDetails.h"
#include "QtGuiMoviesInputStore.h"
using std::string;


class QtGuiMoviesFilter : public QWidget {

	Q_OBJECT

public:
	QtGuiMoviesFilter(QWidget *parent = Q_NULLPTR);

public slots:
	void filterByTitle();
	
	void filterByYear();

private slots:
	void sendInfo();

signals:
	void infoSent(int c, string title);

private:
	QLabel* criteriaLabel;

	int criteria;

	QLineEdit* value;
};

class QtGuiMoviesApp : public QWidget
{
	Q_OBJECT

public:
	QtGuiMoviesApp(MovieService& mS, QWidget *parent = Q_NULLPTR);

private:
	QtGuiMoviesList* movieList;
	QtGuiMoviesDetails* movieDetails;
	QtGuiMoviesInputStore* storeWidget;
	QtGuiMoviesFilter* filterWidget;
	MovieService& movieService;
	std::vector<std::unique_ptr<Change>> prevChanges;
};
