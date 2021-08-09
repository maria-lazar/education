#include "TableViewModel.h"



TableViewModel::TableViewModel(const vector<Movie>& m) : movies{ m }
{

}

int TableViewModel::rowCount(const QModelIndex & parent) const{
	return movies.size();
}

int TableViewModel::columnCount(const QModelIndex & parent) const {
	return 4;
}

QVariant TableViewModel::data(const QModelIndex& index, int role) const{
	if (role == Qt::DisplayRole) {
		Movie m = movies.at(index.row());
		if (index.column() == 0) {
			return QString::fromStdString(m.getTitle());
		}
		if (index.column() == 1) {
			return QString::fromStdString(m.getGenre());
		}
		if (index.column() == 2) {
			return QString::number(m.getYear());
		}
		if (index.column() == 3) {
			return QString::fromStdString(m.getActor());
		}
	}
	if (role == Qt::BackgroundColorRole) {
		int green = 255 - (index.row()*20);
		int blue = index.row() * 20;
		return QBrush(QColor(0, green, blue));
	}
	return QVariant{};
}


QVariant TableViewModel::headerData(int section, Qt::Orientation orientation, int role) const {
	if (role == Qt::DisplayRole && orientation == Qt::Horizontal) {
		if (section == 0) {
			return QString("Title");
		}
		if (section == 1) {
			return QString("Genre");
		}
		if (section == 2) {
			return QString("Year");
		}
		if (section == 3) {
			return QString("Actor");
		}
	}
	return QVariant();
}

void TableViewModel::setMovies(const vector<Movie> m) {
	movies = m;
	emit layoutChanged();
}

TableViewModel::~TableViewModel()
{
}
