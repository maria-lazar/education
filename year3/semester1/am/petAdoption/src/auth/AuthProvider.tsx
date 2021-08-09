import React, {useCallback, useEffect, useState} from 'react';
import PropTypes from 'prop-types';
import {getLogger} from '../core';
import {loginApi} from "./authApi";
import {Storage} from "@capacitor/core";

const log = getLogger('AuthProvider');

type LoginFn = (username?: string, password?: string) => void;
type LogoutFn = () => void;

export interface AuthState {
    authenticationError: Error | null;
    isAuthenticated: boolean;
    isAuthenticating: boolean;
    login?: LoginFn;
    logout?: LogoutFn;
    pendingAuthentication?: boolean;
    username?: string;
    password?: string;
    token: string;
}

let initialState: AuthState = {
    isAuthenticated: false,
    isAuthenticating: false,
    authenticationError: null,
    pendingAuthentication: false,
    token: ''
};

export const AuthContext = React.createContext<AuthState>(initialState);

interface AuthProviderProps {
    children: PropTypes.ReactNodeLike
}

export const AuthProvider: React.FC<AuthProviderProps> = ({children}) => {
    const [state, setState] = useState<AuthState>(initialState);
    const {isAuthenticated, isAuthenticating, authenticationError, pendingAuthentication, token} = state;
    const login = useCallback<LoginFn>(loginCallback, []);
    const logout = useCallback<LogoutFn>(logoutCallback, []);
    useEffect(authenticationEffect, [pendingAuthentication]);
    useEffect(setInitialState, []);
    const value = {isAuthenticated, login, isAuthenticating, authenticationError, token, logout};
    // log('render');
    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );

    function setInitialState() {
        (async () => {
            const res = await Storage.get({key: 'token'});
            if (res.value) {
                setState({
                    isAuthenticated: true,
                    isAuthenticating: false,
                    authenticationError: null,
                    pendingAuthentication: false,
                    token: res.value
                });
            }
        })()
    }

    function loginCallback(username?: string, password?: string): void {
        log('login');
        setState({
            ...state,
            pendingAuthentication: true,
            username,
            password
        });
    }

    async function logoutCallback(): Promise<void> {
        log('logout');
        await Storage.clear();
        setState({
            isAuthenticated: false,
            isAuthenticating: false,
            authenticationError: null,
            pendingAuthentication: false,
            token: ''
        });
    }

    function authenticationEffect() {
        let canceled = false;
        authenticate();
        return () => {
            canceled = true;
        }

        async function authenticate() {
            if (!pendingAuthentication) {
                log('authenticate, !pendingAuthentication, return');
                return;
            }
            try {
                log('authenticate...');
                setState({
                    ...state,
                    isAuthenticating: true,
                });
                const {username, password} = state;
                const {token} = await loginApi(username, password);
                if (canceled) {
                    return;
                }
                log('authenticate succeeded');
                await Storage.set({
                    key: 'token',
                    value: token
                });
                setState({
                    ...state,
                    token,
                    pendingAuthentication: false,
                    isAuthenticated: true,
                    isAuthenticating: false,
                });
            } catch (error) {
                if (canceled) {
                    return;
                }
                log('authenticate failed');
                setState({
                    ...state,
                    authenticationError: error,
                    pendingAuthentication: false,
                    isAuthenticating: false,
                });
            }
        }
    }
};
