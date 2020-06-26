import React from 'react';
import './App.css';
import Trip from "./Trip";
import {getTrips} from "./utils/rest-calls";

class TripForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {landmark: '', companyName: '', departureTime: '', price: '', availablePlaces: ''};
    }

    render() {
        return (
            <form className="TripForm">
                <div className="formInput">
                    <label>Landmark</label>
                    <input id="landmark" type="text" onChange={this.handleChangeLandmark} value={this.state.landmark}/>
                </div>
                <div className="formInput">
                    <label>Company</label>
                    <input id="company" type="text" onChange={this.handleChangeCompany} value={this.state.companyName}/>
                </div>
                <div className="formInput">
                    <label>Departure time</label>
                    <input id="departure" type="datetime-local" onChange={this.handleChangeDepartureTime}
                           value={this.state.departureTime}/>
                </div>
                <div className="formInput">
                    <label>Price</label>
                    <input id="price" type="text" onChange={this.handleChangePrice} value={this.state.price}/>
                </div>
                <div className="formInput">
                    <label>Available places</label>
                    <input id="availablePlaces" type="text" onChange={this.handleChangeAvailablePlaces}
                           value={this.state.availablePlaces}/>
                </div>
                <button onClick={this.handleClick}>Add</button>
                {this.props.saveError && <div>Save error</div>}
            </form>
        );
    }

    handleChangeLandmark = (event) => {
        this.setState({landmark: event.target.value});
    };

    handleChangeCompany = (event) => {
        this.setState({companyName: event.target.value});
    };

    handleChangeDepartureTime = (event) => {
        this.setState({departureTime: event.target.value});
    };

    handleChangePrice = (event) => {
        this.setState({price: event.target.value});
    };

    handleChangeAvailablePlaces = (event) => {
        this.setState({availablePlaces: event.target.value});
    };

    handleClick = (event) => {
        let {landmark, companyName, departureTime, price, availablePlaces} = this.state;
        this.props.onAddTrip({landmark, companyName, departureTime, price, availablePlaces});
        event.preventDefault();
    }
}

export default TripForm;
