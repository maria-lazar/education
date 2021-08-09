#include "QtGuiMoviesList.h"

void showMessageList(string message) {
	QMessageBox msgBox;
	msgBox.setText(QString::fromStdString(message));
	msgBox.exec();
}

QtGuiMoviesList::QtGuiMoviesList(MovieService& mS, std::vector<std::unique_ptr<Change>>& prevChanges, QWidget *parent)
	: movieService(mS), prevChanges(prevChanges), QWidget(parent)
{
	movieService.addObserver(this);
	sortCriteria = 0;
	filterCriteria = 0;
	viewMode = 0;
	bagSize = movieService.getSizeShoppingBag();
	year = 0;

	QVBoxLayout* verticalLayout = new QVBoxLayout();

	//sort and filter labels
	QWidget* labelWidget = new QWidget();
	QHBoxLayout* labelLayout = new QHBoxLayout();
	sortLabel = new QLabel();
	filterLabel = new QLabel();
	QString size = QString::number(bagSize);
	bagLabel = new QLabel("Bag items: " + size);
	labelLayout->addWidget(sortLabel);
	labelLayout->addWidget(filterLabel);
	labelLayout->addWidget(bagLabel);
	labelWidget->setLayout(labelLayout);
	verticalLayout->addWidget(labelWidget);

	//list widget
	list = new QListWidget();
	//verticalLayout->addWidget(list);
	list->setSelectionMode(QAbstractItemView::SingleSelection);
	QObject::connect(list, SIGNAL(itemSelectionChanged()), this, SLOT(onItemSelected()));
	table = new QTableWidget(1, 5);
	table->setSelectionBehavior(QAbstractItemView::SelectRows);
	table->setSelectionMode(QAbstractItemView::SingleSelection);
	//QObject::connect(table, SIGNAL(itemSelectionChanged()), this, SLOT(onItemSelected()));
	QTableWidgetItem* cellItem1 = new QTableWidgetItem("Title");
	QTableWidgetItem* cellItem2 = new QTableWidgetItem("Genre");
	QTableWidgetItem* cellItem3 = new QTableWidgetItem("Year");
	QTableWidgetItem* cellItem4 = new QTableWidgetItem("Actor");
	QTableWidgetItem* cellItem5 = new QTableWidgetItem("Stat");
	table->setHorizontalHeaderItem(0, cellItem1);
	table->setHorizontalHeaderItem(1, cellItem2);
	table->setHorizontalHeaderItem(2, cellItem3);
	table->setHorizontalHeaderItem(3, cellItem4);
	table->setHorizontalHeaderItem(4, cellItem5);
	//verticalLayout->addWidget(table);

	table2 = new QTableView();
	table2->setSelectionBehavior(QAbstractItemView::SelectRows);
	table2->setSelectionMode(QAbstractItemView::SingleSelection);
	model = new TableViewModel(movieService.getAll());
	table2->setModel(model);
	QObject::connect(table2->selectionModel(), &QItemSelectionModel::selectionChanged, [&]() {
		if (table2->selectionModel()->selectedIndexes().isEmpty()) {
			emit movieSelected(-1, -1);
		}
		int selRow = table2->selectionModel()->selectedIndexes().at(0).row();
		emit movieSelected(selRow, 0);
	});
	verticalLayout->addWidget(table2);

	//buttons
	QWidget* buttonsWidget = new QWidget();
	QHBoxLayout* horizontalLayout = new QHBoxLayout();
	removeBtn = new QPushButton("remove");
	sortTitleBtn = new QPushButton("sortByTitle");
	sortActorBtn = new QPushButton("sortByActor");
	sortYearGenreBtn = new QPushButton("sortByYearGenre");
	undoBtn = new QPushButton("undo");
	horizontalLayout->addWidget(removeBtn);
	horizontalLayout->addWidget(sortTitleBtn);
	horizontalLayout->addWidget(sortActorBtn);
	horizontalLayout->addWidget(sortYearGenreBtn);
	horizontalLayout->addWidget(undoBtn);
	QObject::connect(removeBtn, SIGNAL(clicked()), this, SLOT(removeMovie()));
	QObject::connect(sortTitleBtn, SIGNAL(clicked()), this, SLOT(sortByTitle()));
	QObject::connect(sortActorBtn, SIGNAL(clicked()), this, SLOT(sortByActor()));
	QObject::connect(sortYearGenreBtn, SIGNAL(clicked()), this, SLOT(sortByYearGenre()));
	QObject::connect(undoBtn, SIGNAL(clicked()), this, SLOT(undo()));
	buttonsWidget->setLayout(horizontalLayout);
	verticalLayout->addWidget(buttonsWidget);

	QWidget* buttonsWidget2 = new QWidget();
	QHBoxLayout* horizontalLayout2 = new QHBoxLayout();
	bagBtn = new QPushButton("Bag items");
	viewBagBtn = new QPushButton("View bag items");
	saveBagBtn = new QPushButton("Save bag");
	clearBagBtn = new QPushButton("Clear bag");
	generateBagBtn = new QPushButton("Generate bag");
	okBtn = new QPushButton("Ok");
	generateTxt = new QLineEdit();
	QObject::connect(bagBtn, &QPushButton::clicked, [&]() {
		CosCRUDGui* cos = new CosCRUDGui(movieService, [&]() {
			movieService.clearBag();
			modifyBagSize();
		}, [&](int n) {
				movieService.generateBag(n);
				modifyBagSize();
		});
		cos->show();
		movieService.addObserver(cos);
		/*cos->update();
		for (int i = 0; i < cosuri.size(); i++) {
			cosuri.at(i)->addObserver(cos);
			cos->addObserver(cosuri.at(i));
		}
		cosuri.push_back(cos);*/
	});

	QObject::connect(viewBagBtn, &QPushButton::clicked, [&]() {
		CosReadOnlyGui* cos = new CosReadOnlyGui(movieService);
		cos->show();
		movieService.addObserver(cos);
	});

	QObject::connect(saveBagBtn, &QPushButton::clicked, [&]() {
		movieService.saveBag();
		showMessageList("Shopping list saved");
	});

	QObject::connect(clearBagBtn, &QPushButton::clicked, [&]() {
		movieService.clearBag();
		modifyBagSize();
	});
	QObject::connect(generateBagBtn, &QPushButton::clicked, [&]() {
		generateTxt->show();
		okBtn->show();
	});

	QObject::connect(okBtn, &QPushButton::clicked, [&]() {
		int n = generateTxt->text().toInt();
		movieService.generateBag(n);
		generateTxt->hide();
		okBtn->hide();
		modifyBagSize();
	});

	horizontalLayout2->addWidget(bagBtn);
	horizontalLayout2->addWidget(viewBagBtn);
	horizontalLayout2->addWidget(saveBagBtn);
	horizontalLayout2->addWidget(clearBagBtn);
	horizontalLayout2->addWidget(generateBagBtn);
	horizontalLayout2->addWidget(generateTxt);
	horizontalLayout2->addWidget(okBtn);
	generateTxt->hide();
	okBtn->hide();
	horizontalLayout2->addStretch();
	buttonsWidget2->setLayout(horizontalLayout2);
	verticalLayout->addWidget(buttonsWidget2);

	this->setLayout(verticalLayout);
	this->fillMovieList(movieService.getAll(), sortCriteria, filterCriteria);
}


vector<Movie> QtGuiMoviesList::fillMovieList(std::vector<Movie> movies, int sort, int filter) {
	if (movies.size() == 0) {
		showMessageList("No movies");
		return movies;
	}
	if (sort == 1) {
		movies = movieService.sort(movies, [](const Movie& m1, const Movie& m2) noexcept {
			return m1.getTitle() < m2.getTitle();
		});
	}
	else if (sort == 2) {
		movies = movieService.sort(movies, [](const Movie& m1, const Movie& m2) noexcept {
			return m1.getActor() < m2.getActor();
		});
	}
	else if (sort == 3) {
		movies = movieService.sort(movies, [](const Movie& m1, const Movie& m2) noexcept {
			if (m1.getYear() == m2.getYear()) {
				return m1.getGenre() < m2.getGenre();
			}
			return m1.getYear() < m2.getYear();
		});
	}
	vector<Movie> filteredMovies = movies;
	if (filter == 1) {
		filteredMovies = movieService.filter(movies, [this](const Movie &m) noexcept {
			return m.getYear() == this->year;
		});
	}
	else if (filter == 2) {
		filteredMovies = movieService.filter(movies, [this](const Movie &m) noexcept {
			return m.getTitle() == title;
		});
	}
	model->setMovies(filteredMovies);
	
	/*model = new TableViewModel(filteredMovies);
	table2->setModel(model);
	QObject::connect(table2->selectionModel(), &QItemSelectionModel::selectionChanged, [&]() {
		if (table2->selectionModel()->selectedIndexes().isEmpty()) {
			emit movieSelected(-1, -1);
		}
		int selRow = table2->selectionModel()->selectedIndexes().at(0).row();
		emit movieSelected(selRow, 0);
	});*/
	/*
	int green = 40;
	int blue = 250;
	int line = 0;
	int count;


	vector<MovieDTO> v = movieService.getMap();
	table->setRowCount(filteredMovies.size());
	for (const Movie& m : filteredMovies) {
		QListWidgetItem* item = new QListWidgetItem(QString::fromStdString(m.toString()));
		//item->setBackgroundColor(QColor(0, green, blue));
		/*green += 255 / filteredMovies.size();
		blue -= 255 / filteredMovies.size();*/
		/*list->addItem(item);
		QTableWidgetItem* tableItem = new QTableWidgetItem();
		tableItem->setText(QString::fromStdString(m.getTitle()));
		tableItem->setBackgroundColor(QColor(0, green, blue));
		table->setItem(line, 0, tableItem);
		QTableWidgetItem* tableItem1 = new QTableWidgetItem();
		tableItem1->setText(QString::fromStdString(m.getGenre()));
		tableItem1->setBackgroundColor(QColor(0, green, blue));
		table->setItem(line, 1, tableItem1);
		QTableWidgetItem* tableItem2 = new QTableWidgetItem();
		tableItem2->setText(QString::number(m.getYear()));
		tableItem2->setBackgroundColor(QColor(0, green, blue));
		table->setItem(line, 2, tableItem2);
		QTableWidgetItem* tableItem3 = new QTableWidgetItem();
		tableItem3->setText(QString::fromStdString(m.getActor()));
		tableItem3->setBackgroundColor(QColor(0, green, blue));
		table->setItem(line, 3, tableItem3);
		for (int i = 0; i < v.size(); i++) {
			if (v.at(i).getGenre() == m.getGenre()) {
				count = v.at(i).getCount();
			}
		}
		QTableWidgetItem* tableItem4 = new QTableWidgetItem();
		tableItem4->setText(QString::number(count));
		tableItem4->setBackgroundColor(QColor(0, green, blue));
		table->setItem(line, 4, tableItem4);
		green += 255 / filteredMovies.size();
		blue -= 255 / filteredMovies.size();
		line++;
	}*/
	return filteredMovies;
}

void QtGuiMoviesList::update() {
	modifyBagSize();
	movieService.currentList = reloadList(movieService.getAll(), sortCriteria, filterCriteria);
}


// PUBLIC SLOTS

vector<Movie> QtGuiMoviesList::reloadList(const std::vector<Movie>& movies, int sort, int filter) {
	list->clear();
	return fillMovieList(movies, sort, filter);
}

void QtGuiMoviesList::movieAddSetSelection() {
	movieService.currentList = reloadList(movieService.getAll(), sortCriteria, filterCriteria);
	const vector<Movie>& movies = movieService.currentList;
	int index = movies.size() - 1;
	QListWidgetItem* e = list->item(index);
	list->setCurrentItem(e);
}

void QtGuiMoviesList::movieUpdate() {
	movieService.currentList = reloadList(movieService.getAll(), sortCriteria, filterCriteria);
}

void QtGuiMoviesList::filter(int c, string s) {
	if (c == 0) {
		filterByTitleList(s);
	}
	else {
		filterByYearList(s);
	}
}

void QtGuiMoviesList::filterByTitleList(string s) {
	if (s.size() == 0) {
		showMessageList("Title not introduced");
		return;
	}
	if (title != "" || year != 0) {
		showMessageList("Already filtered");
		return;
	}
	filterCriteria = 2;
	title = s;
	movieService.currentList = reloadList(movieService.getAll(), sortCriteria, filterCriteria);
	string str = "- filtered by title: " + title;
	if (movieService.currentList.size() != 0) {
		filterLabel->setText(QString::fromStdString(str));
	}
}

void QtGuiMoviesList::filterByYearList(string s) {
	try {
		int y = stoi(s);
		MovieValidator v;
		v.validateYear(y);
		if (year != 0 || title != "") {
			showMessageList("Already filtered");
			return;
		}
		filterCriteria = 1;
		year = y;
		movieService.currentList = reloadList(movieService.getAll(), sortCriteria, filterCriteria);
		string str = "- filtered by year: " + std::to_string(year);
		if (movieService.currentList.size() != 0) {
			filterLabel->setText(QString::fromStdString(str));
		}
	}
	catch (std::invalid_argument) {
		showMessageList("Invalid year");
	}
	catch (ValidationException& ve) {
		showMessageList(ve.what());
	}
}

void QtGuiMoviesList::clearFilterList() {
	filterCriteria = 0;
	title = "";
	year = 0;
	movieService.currentList = reloadList(movieService.getAll(), sortCriteria, filterCriteria);
	filterLabel->setText("");
}


// PRIVATE SLOTS

void QtGuiMoviesList::onItemSelected() {
	/*if (list->selectedItems().isEmpty()) {
		emit movieSelected(-1, -1);
	}
	else {
		int index = list->currentRow();
		emit movieSelected(index, viewMode);
	}*/
	if (table->selectedItems().isEmpty()) {
		emit movieSelected(-1, -1);
	}
	else {
		int index = table->currentRow();
		emit movieSelected(index, viewMode);
	}

}

void QtGuiMoviesList::removeMovie() {
	try {/*
		if (list->selectedItems().isEmpty()) {
			showMessageList("Select item to remove");
			return;
		}*/
		//int index = list->currentRow();
		//int index = table->currentRow();
		if (table2->selectionModel()->selectedIndexes().isEmpty()) {
			showMessageList("Select item to remove");
			return;
		}
		int index = table2->selectionModel()->selectedIndexes().at(0).row();
		const vector<Movie>& movies = movieService.currentList;
		Movie m = movies.at(index);
		std::unique_ptr<RemoveMovieChange> removeMovieChange = std::make_unique<RemoveMovieChange>(movieService, m.getTitle(), m.getYear());
		removeMovieChange->apply();
		prevChanges.push_back(std::move(removeMovieChange));
		/*for (size_t i = 0; i < nextChanges.size(); i++) {
			delete nextChanges.at(i);
		}
		nextChanges.clear();*/

		movieService.currentList = reloadList(movieService.getAll(), sortCriteria, filterCriteria);
		modifyBagSize();
		showMessageList("Movie deleted");
	}
	catch (std::invalid_argument) {
		showMessageList("Invalid year");
	}
	catch (const StorageException& se) {
		showMessageList(se.what());
	}
	catch (const ValidationException& ve) {
		showMessageList(ve.what());
	}
	catch (...) {
		showMessageList("Unexpected error\n");
	}
}

void QtGuiMoviesList::sortByTitle() {
	sortCriteria = 1;
	movieService.currentList = reloadList(movieService.getAll(), sortCriteria, filterCriteria);
	if (movieService.currentList.size() != 0) {
		sortLabel->setText("- sorted by title");
	}
}

void QtGuiMoviesList::sortByActor() {
	sortCriteria = 2;
	movieService.currentList = reloadList(movieService.getAll(), sortCriteria, filterCriteria);
	if (movieService.currentList.size() != 0) {
		sortLabel->setText("- sorted by actor");
	}
}

void QtGuiMoviesList::sortByYearGenre() {
	sortCriteria = 3;
	movieService.currentList = reloadList(movieService.getAll(), sortCriteria, filterCriteria);
	if (movieService.currentList.size() != 0) {
		sortLabel->setText("- sorted by year and genre");
	}
}

void QtGuiMoviesList::undo() {
	if (prevChanges.size() == 0) {
		showMessageList("No action to undo\n");
		return;
	}
	prevChanges.back()->undo();
	prevChanges.pop_back();
	movieService.currentList = reloadList(movieService.getAll(), sortCriteria, filterCriteria);
}

void QtGuiMoviesList::modifyBagSize() {
	bagSize = movieService.getSizeShoppingBag();
	QString size = QString::number(bagSize);
	bagLabel->setText("Bag items: " + size);
	//notify();
}

void QtGuiMoviesList::clearBag() {
	movieService.clearBag();
	modifyBagSize();
}
