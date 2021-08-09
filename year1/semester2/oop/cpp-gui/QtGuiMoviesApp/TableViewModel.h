#pragma once
#include "Movie.h"
#include <vector>
#include <QAbstractTableModel>
#include <QBrush>
using std::vector;
using namespace domain;

class TableViewModel: public QAbstractTableModel
{
private:
	vector<Movie> movies;

public:
	TableViewModel(const vector<Movie>& m);

	int rowCount(const QModelIndex & parent = QModelIndex()) const override;

	int columnCount(const QModelIndex & parent = QModelIndex()) const override;

	QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override;

	QVariant headerData(int section, Qt::Orientation orientation, int role = Qt::DisplayRole) const override;

	void setMovies(const vector<Movie> m);

	~TableViewModel();
};

