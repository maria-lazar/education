#include "CosCRUDGui.h"




CosCRUDGui::CosCRUDGui(MovieService& mS, std::function<void()> f, std::function<void(int n)> g, QWidget *parent) : QWidget(parent, Qt::Window), movieService(mS) {
	QHBoxLayout* mainLay = new QHBoxLayout();
	this->setLayout(mainLay);
	table2 = new QTableView();
	

	model = new TableViewModel(movieService.getAllShopping());
	table2->setModel(model);
	table2->setSelectionBehavior(QAbstractItemView::SelectRows);
	table2->setSelectionMode(QAbstractItemView::SingleSelection);
	QObject::connect(table2->selectionModel(), &QItemSelectionModel::selectionChanged, [&]() {
		movieService.selectionChanged(table2->selectionModel()->selectedIndexes().at(0).row());
		//notify2(table2->selectionModel()->selectedIndexes().at(0).row());
	});
	mainLay->addWidget(table2);

	table = new QTableWidget(1, 4);
	table->setSelectionBehavior(QAbstractItemView::SelectRows);
	table->setSelectionMode(QAbstractItemView::SingleSelection);
	table->setHorizontalHeaderItem(0, new  QTableWidgetItem("Title"));
	table->setHorizontalHeaderItem(1, new  QTableWidgetItem("Genre"));
	table->setHorizontalHeaderItem(2, new  QTableWidgetItem("Year"));
	table->setHorizontalHeaderItem(3, new  QTableWidgetItem("Actor"));
//	mainLay->addWidget(table);
	/*
	QObject::connect(table, &QTableWidget::itemSelectionChanged, [&]() {
		notify2(table->currentRow());
	});*/

	QPushButton* clearBtn = new QPushButton("Clear");
	QPushButton* generateBtn = new QPushButton("Generate");
	generateTxt = new QLineEdit();
	okBtn = new QPushButton("Ok");
	mainLay->addWidget(clearBtn);
	mainLay->addWidget(generateBtn);
	mainLay->addWidget(generateTxt);
	mainLay->addWidget(okBtn);
	funcClear = f;
	funcGenerate = g;
	generateTxt->hide();
	okBtn->hide();
	QObject::connect(clearBtn, &QPushButton::clicked, [&]() {
		funcClear();
	});
	QObject::connect(generateBtn, &QPushButton::clicked, [&]() {
		generateTxt->show();
		okBtn->show();
	});
	QObject::connect(okBtn, &QPushButton::clicked, [&]() {
		int n = generateTxt->text().toInt();
		funcGenerate(n);
		generateTxt->hide();
		okBtn->hide();
	});
}


void CosCRUDGui::reloadList() {
	table->clear();
	model->setMovies(movieService.getAllShopping());
	/*
	const vector<Movie>& movies = movieService.getAllShopping();
	table->setRowCount(movies.size());
	int line = 0;
	for (int i = 0; i < movies.size(); i++) {
		Movie m = movies.at(i);
		QTableWidgetItem* title = new QTableWidgetItem(QString::fromStdString(m.getTitle()));
		table->setItem(line, 0, title);
		QTableWidgetItem* genre = new QTableWidgetItem(QString::fromStdString(m.getGenre()));
		table->setItem(line, 1, genre);
		QTableWidgetItem* year = new QTableWidgetItem(QString::number(m.getYear()));
		table->setItem(line, 2, year);
		QTableWidgetItem* actor = new QTableWidgetItem(QString::fromStdString(m.getActor()));
		table->setItem(line, 3, actor);
		line++;
	}*/
}


void CosCRUDGui::update() {
	reloadList();
}

void CosCRUDGui::update2(int n) {
	//table->selectRow(n);
	table2->selectRow(n);
}

CosCRUDGui::~CosCRUDGui()
{
}
