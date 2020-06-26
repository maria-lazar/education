import React from 'react';
import './App.css';
import Trip from "./Trip";

class TripList extends React.Component {
    render() {
        console.log('TripList - render');
        return (
            <div className="TripList">
                <table className="TripTable">
                    <thead>
                    <tr>
                        <th>Landmark</th>
                        <th>Company</th>
                        <th>Departure</th>
                        <th>Price</th>
                        <th>Available places</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>{
                        this.props.trips
                            ? this.props.trips.map(trip => <Trip key={trip.id}
                                                                 id={trip.id}
                                                                 landmark={trip.landmark}
                                                                 availablePlaces={trip.availablePlaces}
                                                                 companyName={trip.companyName}
                                                                 departureTime={trip.departureTime}
                                                                 price={trip.price}
                                                                 onDelete={this.props.onDelete}/>)
                            : 'Loading...'}
                    </tbody>
                </table>
                {this.props.fetchError && <div>{this.props.fetchError}</div>}
            </div>
        );
    }
}

export default TripList;
