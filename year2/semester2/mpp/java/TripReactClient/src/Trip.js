import React from 'react';

function Trip({id, landmark, companyName, departureTime, price, availablePlaces, onDelete}) {
    return (
        <tr>
            <td>{landmark} </td>
            <td>{companyName} </td>
            <td>{departureTime} </td>
            <td>{price} </td>
            <td>{availablePlaces}</td>
            <td>
                <button onClick={() => onDelete(id)}>Delete</button>
            </td>
        </tr>
    )
}

export default Trip;
