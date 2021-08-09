import axios from 'axios';
import {authConfig, authModifiedSinceConfig, baseUrl, getLogger} from '../core';
import {PetProps} from './PetProps';

const log = getLogger('petApi');

const petUrl = `http://${baseUrl}/api/pet`;

interface ResponseProps<T> {
    data: T;
}

interface ResponsePets {
    pets?: PetProps[];
    status: any;
    modifiedSince?: number;
}

function withLogs<T>(promise: Promise<ResponseProps<T>>, fnName: string): Promise<T> {
    log(`${fnName} - started`);
    return promise
        .then(res => {
            log(`${fnName} - succeeded`);
            return Promise.resolve(res.data);
        })
        .catch(err => {
            log(`${fnName} - failed`);
            return Promise.reject(err);
        });
}


export const getPetsModifiedSince: (token: string, modifiedSince: number, page?: number) => Promise<ResponsePets> = async (token, modifiedSince, page) => {
    log("getPetsModifiedSince");
    if (!page) {
        try {
            const response = await axios.get(petUrl, authModifiedSinceConfig(token, modifiedSince));
            return {pets: response.data, status: response.status, modifiedSince: response.headers["last-modified"]};
        } catch (err) {
            log(err.response);
            if (err?.response?.status === 304) {
                return {status: 304, modifiedSince: err.response.headers['last-modified']};
            }
            throw err;
        }
    } else {
        const url = petUrl + `?page=${page}`;
        try {
            const response = await axios.get(url, authModifiedSinceConfig(token, modifiedSince));
            return {pets: response.data, status: response.status, modifiedSince: response.headers["last-modified"]};
        } catch (err) {
            log(err.response);
            if (err?.response?.status === 304) {
                return {status: 304, modifiedSince: err.response.headers['last-modified']};
            }
            throw err;
        }
    }
}
export const getPets: (token: string, page?: number) => Promise<ResponsePets> = async (token, page) => {
    log("getPets");
    if (page === undefined) {
        try {
            const response = await axios.get(petUrl, authConfig(token));
            return {pets: response.data, status: response.status, modifiedSince: response.headers["last-modified"]};
        } catch (err) {
            log(err.response);
            if (err?.response?.status === 304) {
                return {status: 304, modifiedSince: err.response.headers['last-modified']};
            }
            throw err;
        }
    } else {
        const url = petUrl + `?page=${page}`;
        try {
            const response = await axios.get(url, authConfig(token));
            return {pets: response.data, status: response.status, modifiedSince: response.headers["last-modified"]};
        } catch (err) {
            log(err.response);
            if (err?.response?.status === 304) {
                return {status: 304, modifiedSince: err.response.headers['last-modified']};
            }
            throw err;
        }
    }
}

export const createPet: (token: string, pet: PetProps) => Promise<PetProps[]> = (token, pet) => {
    return withLogs(axios.post(petUrl, pet, authConfig(token)), 'createPet');
}

export const updatePet: (token: string, pet: PetProps) => Promise<PetProps[]> = (token, pet) => {
    return withLogs(axios.put(`${petUrl}/${pet._id}`, pet, authConfig(token)), 'updatePet');
}

export const getPet: (token: string, id?: string) => Promise<PetProps[]> = (token, id) => {
    return withLogs(axios.get(`${petUrl}/${id}`, authConfig(token)), 'getPet');
}

interface MessageData {
    event: string;
    // payload: {
    //     pet: PetProps;
    // };
    payload: PetProps;
}

export const newWebSocket = (token: string, onMessage: (data: MessageData) => void, onClose: () => void) => {
    const ws = new WebSocket(`ws://${baseUrl}`);
    ws.onopen = () => {
        log('web socket onopen');
        ws.send(JSON.stringify({type: 'authorization', payload: {token}}));
    };
    ws.onclose = () => {
        log('web socket onclose');
        onClose();
    };
    ws.onerror = error => {
        log('web socket onerror', error);
    };
    ws.onmessage = messageEvent => {
        log('web socket onmessage');
        onMessage(JSON.parse(messageEvent.data));
    };
    return () => {
        ws.close();
    }
}
