#include "QtGuiMoviesApp.h"
#include "QtWidgets/QHBoxLayout"
#include "QtWidgets/QVBoxLayout"
#include "QtWidgets/QLabel"
#include "QtWidgets/QGridLayout"
#include "QtWidgets/QFormLayout"
#include "QtWidgets/QPushButton"
#include "QtWidgets/QMessageBox"
#include "QtWidgets/QListWidgetItem"
#include "QtWidgets/QLineEdit"
#include <iostream>
#include <algorithm>

QtGuiMoviesFilter::QtGuiMoviesFilter(QWidget *parent) : QWidget(parent, Qt::Window) {
	QHBoxLayout* hLay = new QHBoxLayout();
	criteriaLabel = new QLabel();
	value = new QLineEdit();
	hLay->addWidget(criteriaLabel);
	hLay->addWidget(value);
	QPushButton* okBtn = new QPushButton("Ok");
	QPushButton* cancelBtn = new QPushButton("Cancel");
	hLay->addWidget(okBtn);
	hLay->addWidget(cancelBtn);
	QObject::connect(okBtn, SIGNAL(clicked()), this, SLOT(sendInfo()));
	QObject::connect(cancelBtn, SIGNAL(clicked()), this, SLOT(close()));
	this->setLayout(hLay);
}

QtGuiMoviesApp::QtGuiMoviesApp(MovieService& mS, QWidget *parent)
	: movieService(mS), QWidget(parent)
{
	storeWidget = new QtGuiMoviesInputStore(movieService, prevChanges, this);
	movieList = new QtGuiMoviesList(movieService, prevChanges, this);
	movieDetails = new QtGuiMoviesDetails(movieService, prevChanges, this);
	filterWidget = new QtGuiMoviesFilter(this);

	QObject::connect(storeWidget, SIGNAL(movieAdded()), movieList, SLOT(movieAddSetSelection()));
	QObject::connect(storeWidget, SIGNAL(movieUpdated()), movieList, SLOT(movieUpdate()));

	QObject::connect(movieDetails, SIGNAL(addSignal(QString, QString, QString, QString)), storeWidget, SLOT(addStoreSlot(QString, QString, QString, QString)));
	QObject::connect(movieDetails, SIGNAL(updateSignal(QString, QString, QString, QString)), storeWidget, SLOT(updateStoreSlot(QString, QString, QString, QString)));
	QObject::connect(movieDetails, SIGNAL(filteredByTitle()), filterWidget, SLOT(filterByTitle()));
	QObject::connect(movieDetails, SIGNAL(filteredByYear()), filterWidget, SLOT(filterByYear()));
	QObject::connect(movieDetails, SIGNAL(clearedFilter()), movieList, SLOT(clearFilterList()));
	QObject::connect(movieDetails, SIGNAL(addedToBag()), movieList, SLOT(modifyBagSize()));
	
	QObject::connect(movieList, SIGNAL(movieSelected(int, int)), movieDetails, SLOT(fillSelectedInfo(int, int)));
	QObject::connect(movieList, SIGNAL(bagShown()), movieDetails, SLOT(bagView()));
	QObject::connect(movieList, SIGNAL(bagHidden()), movieDetails, SLOT(bagHide()));
	
	QObject::connect(filterWidget, SIGNAL(infoSent(int, string)), movieList, SLOT(filter(int, string)));
	
	QHBoxLayout* layout = new QHBoxLayout();
	layout->addWidget(movieList);
	layout->addWidget(movieDetails);
	this->setLayout(layout);
}

void QtGuiMoviesFilter::sendInfo() {
	string s = value->text().toStdString();
	this->close();
	emit infoSent(criteria, s);
}

void showMessage(string message) {
	QMessageBox msgBox;
	msgBox.setText(QString::fromStdString(message));
	msgBox.exec();
}

void QtGuiMoviesFilter::filterByTitle() {
	criteriaLabel->setText("title: ");
	criteria = 0;
	value->clear();
	this->show();
}

void QtGuiMoviesFilter::filterByYear() {
	criteriaLabel->setText("year: ");
	criteria = 1;
	value->clear();
	this->show();
}