#include "QtGuiMoviesInputStore.h"

void showMessageStore(std::string message) {
	QMessageBox msgBox;
	msgBox.setText(QString::fromStdString(message));
	msgBox.exec();
}

QtGuiMoviesInputStore::QtGuiMoviesInputStore(MovieService& mS, std::vector<std::unique_ptr<Change>>& prevChanges, QWidget *parent) :
	movieService(mS), prevChanges{ prevChanges }, QWidget(parent, Qt::Window) {

	QVBoxLayout* verticalLayout = new QVBoxLayout();
	
	//details
	QWidget* formDetails = new QWidget;
	QFormLayout* formLayout = new QFormLayout();
	QLabel* lblTitle = new QLabel("Title: ");
	QLabel* lblGenre = new QLabel("Genre: ");
	QLabel* lblYear = new QLabel("Year: ");
	QLabel* lblActor = new QLabel("Actor: ");
	title = new QLineEdit();
	genre = new QLineEdit();
	year = new QLineEdit();
	actor = new QLineEdit();
	formLayout->addRow(lblTitle, title);
	formLayout->addRow(lblGenre, genre);
	formLayout->addRow(lblYear, year);
	formLayout->addRow(lblActor, actor);
	formDetails->setLayout(formLayout);
	verticalLayout->addWidget(formDetails);

	//buttons
	QWidget* h = new QWidget();
	QHBoxLayout* hLayout = new QHBoxLayout();
	saveBtn = new QPushButton("Save");
	//editBtn = new QPushButton("Edit");
	QPushButton* closeBtn = new QPushButton("Close");
	//QObject::connect(saveBtn, SIGNAL(clicked()), this, SLOT(addMovie()));
	//QObject::connect(editBtn, SIGNAL(clicked()), this, SLOT(updateMovie()));
	QObject::connect(closeBtn, SIGNAL(clicked()), this, SLOT(close()));
	hLayout->addWidget(saveBtn);
	//hLayout->addWidget(editBtn);
	hLayout->addWidget(closeBtn);
	h->setLayout(hLayout);
	verticalLayout->addWidget(h);

	this->setLayout(verticalLayout);

};


// PUBLIC SLOTS

void QtGuiMoviesInputStore::addStoreSlot(QString title, QString genre, QString year, QString actor) {
	this->title->setText(title);
	this->genre->setText(genre);
	this->year->setText(year);
	this->actor->setText(actor);
	this->title->setReadOnly(false);
	this->year->setReadOnly(false);
	//this->editBtn->setDisabled(true);
	//this->saveBtn->setDisabled(false);
	saveBtn->setText("Save");
	QObject::disconnect(saveBtn, SIGNAL(clicked()), this, SLOT(updateMovie()));
	QObject::disconnect(saveBtn, SIGNAL(clicked()), this, SLOT(addMovie()));
	QObject::connect(saveBtn, SIGNAL(clicked()), this, SLOT(addMovie()));
	this->show();
}

void QtGuiMoviesInputStore::updateStoreSlot(QString title, QString genre, QString year, QString actor) {
	this->title->setText(title);
	this->title->setReadOnly(true);
	this->genre->setText(genre);
	this->year->setText(year);
	this->year->setReadOnly(true);
	this->actor->setText(actor);
	//this->editBtn->setDisabled(false);
	//this->saveBtn->setDisabled(true);
	if (title.isEmpty()) {
		showMessageStore("Select movie");
	}
	else {
		saveBtn->setText("Update");
		QObject::disconnect(saveBtn, SIGNAL(clicked()), this, SLOT(addMovie()));
		QObject::disconnect(saveBtn, SIGNAL(clicked()), this, SLOT(updateMovie()));
		QObject::connect(saveBtn, SIGNAL(clicked()), this, SLOT(updateMovie()));
		this->show();
	}
}

// PRIVATE SLOTS

void QtGuiMoviesInputStore::addMovie() {
	try {
		string title = this->title->text().toStdString();
		string genre = this->genre->text().toStdString();
		string actor = this->actor->text().toStdString();
		string sYear = this->year->text().toStdString();
		int year = stoi(sYear);
		std::unique_ptr<AddMovieChange> addMovieChange = std::make_unique<AddMovieChange>(movieService, title, genre, year, actor);
		addMovieChange->apply();
		prevChanges.push_back(std::make_unique<AddMovieChange>(movieService, title, genre, year, actor));
		/*for (size_t i = 0; i < nextChanges.size(); i++) {
			delete nextChanges.at(i);
		}
		nextChanges.clear();*/
		emit movieAdded();
		showMessageStore("Movie added\n");
	}
	catch (std::invalid_argument) {
		showMessageStore("Invalid year\n");
	}
	catch (const StorageException& se) {
		showMessageStore(se.what());
	}
	catch (const ValidationException& ve) {
		showMessageStore(ve.what());
	}
	catch (...) {
		showMessageStore("Unexpected error\n");
	}
}

void QtGuiMoviesInputStore::updateMovie() {
	try {
		string title = this->title->text().toStdString();
		string genre = this->genre->text().toStdString();
		string actor = this->actor->text().toStdString();
		string sYear = this->year->text().toStdString();
		int year = stoi(sYear);
		std::unique_ptr<UpdateMovieChange> updateMovieChange = std::make_unique<UpdateMovieChange>(movieService, title, genre, year, actor);
		updateMovieChange->apply();
		prevChanges.push_back(std::move(updateMovieChange));
		/*for (size_t i = 0; i < nextChanges.size(); i++) {
			delete nextChanges.at(i);
		}
		nextChanges.clear();*/
		emit movieUpdated();
		showMessageStore("Movie updated");
	}
	catch (std::invalid_argument) {
		showMessageStore("Invalid year");
	}
	catch (const StorageException& se) {
		showMessageStore(se.what());
	}
	catch (const ValidationException& ve) {
		showMessageStore(ve.what());
	}
	catch (...) {
		showMessageStore("Unexpected error\n");
	}
}
