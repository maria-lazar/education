import React from 'react';
import PropTypes from 'prop-types';
import {initialState, NetworkState, useNetwork} from "./useNetwork";

export const NetworkContext = React.createContext<NetworkState>(initialState);

interface NetworkProviderProps {
    children: PropTypes.ReactNodeLike
}

export const NetworkProvider: React.FC<NetworkProviderProps> = ({children}) => {
    const {networkStatus} = useNetwork();
    const value = networkStatus;
    // console.log(networkStatus);
    // log('render');
    return (
        <NetworkContext.Provider value={value}>
            {children}
        </NetworkContext.Provider>
    );
};
