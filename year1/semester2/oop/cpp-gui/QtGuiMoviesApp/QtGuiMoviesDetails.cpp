#include "QtGuiMoviesDetails.h"


void showMessageDetails(std::string message) {
	QMessageBox msgBox;
	msgBox.setText(QString::fromStdString(message));
	msgBox.exec();
}

QtGuiMoviesDetails::QtGuiMoviesDetails(MovieService& mS, std::vector<std::unique_ptr<Change>>& prevChanges, QWidget *parent)
	: movieService(mS), prevChanges(prevChanges), QWidget(parent)
{
	QVBoxLayout* verticalLayout = new QVBoxLayout();
	
	//details
	QWidget* formDetails = new QWidget;
	QFormLayout* formLayout = new QFormLayout();
	QLabel* lblTitle = new QLabel("Title: ");
	QLabel* lblGenre = new QLabel("Genre: ");
	QLabel* lblYear = new QLabel("Year: ");
	QLabel* lblActor = new QLabel("Actor: ");
	txtTitle = new QLineEdit();
	txtGenre = new QLineEdit();
	txtYear = new QLineEdit();
	txtActor = new QLineEdit();
	txtTitle->setReadOnly(true);
	txtGenre->setReadOnly(true);
	txtYear->setReadOnly(true);
	txtActor->setReadOnly(true);
	formLayout->addRow(lblTitle, txtTitle);
	formLayout->addRow(lblGenre, txtGenre);
	formLayout->addRow(lblYear, txtYear);
	formLayout->addRow(lblActor, txtActor);
	formDetails->setLayout(formLayout);
	verticalLayout->addWidget(formDetails);

	//buttons
	QWidget* gridWidget = new QWidget();
	QGridLayout* gridLayout = new QGridLayout();
	addBtn = new QPushButton("add");
	updateBtn = new QPushButton("edit");
	filterTitleBtn = new QPushButton("filter by title");
	filterYearBtn = new QPushButton("filter by year");
	clearFilterBtn = new QPushButton("clear filter option");
	addToBagBtn = new QPushButton("add to bag");
	QObject::connect(addBtn, SIGNAL(clicked()), this, SLOT(add()));
	QObject::connect(updateBtn, SIGNAL(clicked()), this, SLOT(update()));
	QObject::connect(filterTitleBtn, SIGNAL(clicked()), this, SLOT(filterTitle()));
	QObject::connect(filterYearBtn, SIGNAL(clicked()), this, SLOT(filterYear()));
	QObject::connect(clearFilterBtn, SIGNAL(clicked()), this, SLOT(clearFilter()));
	QObject::connect(addToBagBtn, SIGNAL(clicked()), this, SLOT(addToBag()));
	gridLayout->addWidget(addBtn, 0, 0);
	gridLayout->addWidget(updateBtn, 0, 1);
	gridLayout->addWidget(filterTitleBtn, 1, 0);
	gridLayout->addWidget(filterYearBtn, 1, 1);
	gridLayout->addWidget(clearFilterBtn, 2, 0);
	gridLayout->addWidget(addToBagBtn, 2, 1);
	gridWidget->setLayout(gridLayout);
	verticalLayout->addWidget(gridWidget);

	this->setLayout(verticalLayout);
}


// PUBLIC SLOTS

void QtGuiMoviesDetails::fillSelectedInfo(int index, int list) {
	if (index == -1) {
		txtTitle->setText("");
		txtGenre->setText("");
		txtYear->setText("");
		txtActor->setText("");
		return;
	}
	if (list == 0) {
		const vector<Movie>& movies = movieService.currentList;
		Movie m = movies.at(index);
		txtTitle->setText(QString::fromStdString(m.getTitle()));
		txtGenre->setText(QString::fromStdString(m.getGenre()));
		txtYear->setText(QString::number(m.getYear()));
		txtActor->setText(QString::fromStdString(m.getActor()));
	}
	else {
		const vector<Movie>& movies = movieService.getAllShopping();
		Movie m = movies.at(index);
		txtTitle->setText(QString::fromStdString(m.getTitle()));
		txtGenre->setText(QString::fromStdString(m.getGenre()));
		txtYear->setText(QString::number(m.getYear()));
		txtActor->setText(QString::fromStdString(m.getActor()));
	}
}

// PRIVATE SLOTS

void QtGuiMoviesDetails::add() {
	emit addSignal(txtTitle->text(), txtGenre->text(), txtYear->text(), txtActor->text());
}

void QtGuiMoviesDetails::update() {
	emit updateSignal(txtTitle->text(), txtGenre->text(), txtYear->text(), txtActor->text());
}

void QtGuiMoviesDetails::filterTitle() {
	emit filteredByTitle();
}

void QtGuiMoviesDetails::filterYear() {
	emit filteredByYear();
}

void QtGuiMoviesDetails::clearFilter() {
	emit clearedFilter();
}

void QtGuiMoviesDetails::addToBag() {
	if (txtTitle->text().isEmpty()) {
		showMessageDetails("Select item to add");
		return;
	}
	try {
		string title = txtTitle->text().toStdString();
		movieService.addMovieBag(title);
		emit addedToBag();
	}
	catch (const StorageException& se) {
		showMessageDetails(se.what());
	}
	catch (const ValidationException& ve) {
		showMessageDetails(ve.what());
	}

}

void QtGuiMoviesDetails::bagView() {
	addBtn->setDisabled(true);
	updateBtn->setDisabled(true);
	filterTitleBtn->setDisabled(true);
	filterYearBtn->setDisabled(true);
	clearFilterBtn->setDisabled(true);
	addToBagBtn->setDisabled(true);
}

void QtGuiMoviesDetails::bagHide() {
	addBtn->setDisabled(false);
	updateBtn->setDisabled(false);
	filterTitleBtn->setDisabled(false);
	filterYearBtn->setDisabled(false);
	clearFilterBtn->setDisabled(false);
	addToBagBtn->setDisabled(false);
}


