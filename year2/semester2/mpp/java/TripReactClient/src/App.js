import React from 'react';
import './App.css';
import TripList from "./TripList";
import TripForm from "./TripForm";
import {addTrip, deleteTrip, getTrips} from "./utils/rest-calls";


class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {trips: null, saveError: null, fetchError: null};
        console.log('App - constructor');
    }

    componentDidMount() {
        console.log('App - componentDidMount');
        getTrips()
            .then((trips) => {
                this.setState({trips});
            })
            .catch(() => {
                this.setState({fetchError: 'Error loading list'});
            });
    }

    handleAddTrip = (trip) => {
        this.setState({saveError: null});
        addTrip(trip)
            .then(t => this.setState({trips: (this.state.trips || []).concat(t)}))
            .catch(error => {
                console.log('Request failed', error);
                this.setState({saveError: error});
            });
    };

    handleDelete = (id) => {
        this.setState({fetchError: null});
        deleteTrip(id)
            .then(t => this.setState({
                trips: this.state.trips.filter(t => t.id !== id)
            }))
            .catch(error => {
                console.log('Request failed', error);
                this.setState({fetchError: 'Error delete trip'});
            });
    };

    render() {
        return (
            <div className="App">
                <h1>Trips Services</h1>
                <div className="content">
                    <TripList trips={this.state.trips} onDelete={this.handleDelete} fetchError={this.state.fetchError}/>
                    <TripForm onAddTrip={this.handleAddTrip} saveError={this.state.saveError}/>
                </div>
            </div>
        );
    }
}

export default App;
