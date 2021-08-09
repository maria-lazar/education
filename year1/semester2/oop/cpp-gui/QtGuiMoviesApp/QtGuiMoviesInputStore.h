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


class QtGuiMoviesInputStore : public QWidget {
	Q_OBJECT

public:
	QtGuiMoviesInputStore(MovieService& mS, std::vector<std::unique_ptr<Change>>& prevChanges, QWidget *parent = Q_NULLPTR);

public slots:
	void addStoreSlot(QString title, QString genre, QString year, QString actor);

	void updateStoreSlot(QString title, QString genre, QString year, QString actor);

private slots:
	void addMovie();

	void updateMovie();

signals:
	void movieAdded();

	void movieUpdated();


private:
	MovieService& movieService;
	QLineEdit* title;
	QLineEdit* genre;
	QLineEdit* year;
	QLineEdit* actor;
	std::vector<std::unique_ptr<Change>>& prevChanges;
	QPushButton* saveBtn;
	QPushButton* editBtn;
	QPushButton* filterBtn;
};
