import {TRIPS_BASE_URL} from "./constants";

export function getTrips() {
    let headers = new Headers();
    headers.append("Accept", "application/json");
    let request = new Request(TRIPS_BASE_URL, {method: "GET", headers: headers});
    return fetch(request)
        .then(res => res.json());
}

export function addTrip(trip) {
    const myHeaders = new Headers();
    myHeaders.append("Accept", "application/json");
    myHeaders.append("Content-Type", "application/json");
    return fetch(TRIPS_BASE_URL, {
        method: 'POST',
        headers: myHeaders,
        mode: 'cors',
        body: JSON.stringify(trip)
    })
        .then(response => response.json())
}

export function deleteTrip(id) {
    const myHeaders = new Headers();
    myHeaders.append("Accept", "application/json");
    myHeaders.append("Content-Type", "application/json");
    return fetch(TRIPS_BASE_URL + "/" + id, {
        method: 'DELETE',
        headers: myHeaders,
        mode: 'cors'
    });
}
