import React, {useCallback, useContext, useEffect, useReducer} from 'react';
import PropTypes from 'prop-types';
import {getLogger} from "../core";
import {PetProps} from "./PetProps";
import {createPet, getPets, getPetsModifiedSince, newWebSocket, updatePet} from "./petApi";
import {AuthContext} from "../auth";
import {Storage} from "@capacitor/core";
import {v4 as uuidv4} from 'uuid';
import {NetworkContext} from "../core/NetworkProvider";
import {Photo, usePhotoGallery} from "../photo/usePhotoGallery";

const log = getLogger('PetProvider');

type SavePetFn = (pet: PetProps) => Promise<any>;
type NextPageFn = () => Promise<any>;
type ResetPageFn = () => void;
type TakePhotoFn = () => Promise<Photo>;

export interface PetsState {
    pets: PetProps[],
    fetching: boolean,
    fetchingError?: Error | null
    saving: boolean,
    savingError?: Error | null,
    savePet?: SavePetFn,
    page: number,
    nextPage?: NextPageFn,
    hasMorePets: boolean,
    resetProfilePage?: ResetPageFn,
    sendingUnsaved: boolean,
    sendAgain: number,
    modifiedSince?: number,
    socketClosed?: boolean,
    takePhoto?: TakePhotoFn;
}

interface ActionProps {
    type: string,
    payload?: any,
}

const initialState: PetsState = {
    fetching: false,
    saving: false,
    page: 0,
    hasMorePets: true,
    pets: [],
    sendingUnsaved: false,
    sendAgain: 0,
    socketClosed: true
};

const FETCH_PETS_STARTED = 'FETCH_PETS_STARTED';
const FETCH_PETS_SUCCEEDED = 'FETCH_PETS_SUCCEEDED';
const FETCH_PETS_NOT_MODIFIED = 'FETCH_PETS_NOT_MODIFIED';
const FETCH_PETS_FAILED = 'FETCH_PETS_FAILED';
const RESET_PETS = 'RESET_PETS';
const RESET_PROFILE_PAGE = 'RESET_PROFILE_PAGE';
const SAVE_PET_STARTED = 'SAVE_PET_STARTED';
const SAVE_PET_SUCCEEDED = 'SAVE_PET_SUCCEEDED';
const SAVE_PET_FAILED = 'SAVE_PET_FAILED';
const SAVE_NR_PAGE = 'SAVE_NR_PAGE';
const FETCH_NEXT_PAGE = 'FETCH_NEXT_PAGE';
const SEND_UNSAVED_STARTED = 'SEND_UNSAVED_STARTED';
const SEND_UNSAVED_SUCCESSFUL = 'SEND_UNSAVED_SUCCESSFUL';
const CONFLICT = 'CONFLICT';
const MODIFIED_SINCE = 'MODIFIED_SINCE';
const SOCKET_CLOSED = 'SOCKET_CLOSED';

function savePet(pets: PetProps[], pet: PetProps) {
    const index = pets.findIndex(p => (p._id === pet._id) || (p.frontendId && (p.frontendId === pet.frontendId)));
    if (index === -1) {
        pets.push(pet);
    } else {
        pets[index] = pet;
    }
    pets.sort((p1, p2) => {
        if (p1.lastModified && p2.lastModified && p1.lastModified > p2.lastModified) {
            return -1;
        } else if (p1.lastModified && p2.lastModified && p1.lastModified === p2.lastModified) {
            return 0;
        }
        return 1;
    });
    return pets;
}

const reducer: (state: PetsState, action: ActionProps) => PetsState =
    (state, {type, payload}) => {
        log(type, payload);
        switch (type) {
            case SAVE_NR_PAGE:
                return {...state, page: payload.page}
            case SOCKET_CLOSED:
                return {...state, socketClosed: payload.closed}
            case MODIFIED_SINCE:
                return {...state, modifiedSince: payload.modifiedSince}
            case RESET_PETS:
                return {...state, pets: []}
            case CONFLICT:
                let currentPets = state.pets || [];
                const index = currentPets.findIndex(p => (p._id === payload.petId));
                if (index !== -1) {
                    currentPets[index].conflict = true;
                    currentPets[index].saved = false;
                }
                return {...state, saving: false, pets: currentPets}
            case SEND_UNSAVED_STARTED:
                return {...state, sendingUnsaved: true}
            case SEND_UNSAVED_SUCCESSFUL:
                let resultPets = state.pets || [];
                payload.pets.forEach((pet: PetProps) => {
                    const index = state.pets?.findIndex(p => (p._id === pet._id) || (p.frontendId && (p.frontendId === pet.frontendId)));
                    if (index === -1) {
                        resultPets?.push(pet);
                    } else {
                        resultPets[index] = pet;
                    }
                })
                resultPets.forEach(pet => {
                    const index = payload.conflicts.findIndex((id: string) => id === pet._id);
                    if (index !== -1) {
                        pet.conflict = true;
                    }
                })
                resultPets.sort((p1, p2) => {
                    if (p1.lastModified && p2.lastModified && p1.lastModified > p2.lastModified) {
                        return -1;
                    } else if (p1.lastModified && p2.lastModified && p1.lastModified === p2.lastModified) {
                        return 0;
                    }
                    return 1;
                });
                let retry = state.sendAgain;
                if (!payload.sendAgain) {
                    retry = 0;
                } else {
                    retry += 1;
                }
                return {...state, sendingUnsaved: false, pets: resultPets, sendAgain: retry}
            case RESET_PROFILE_PAGE:
                return {...state, savingError: null}
            case FETCH_PETS_STARTED:
                return {...state, fetching: true, fetchingError: null};
            case FETCH_PETS_NOT_MODIFIED:
                return {...state, fetching: false, fetchingError: null};
            case FETCH_PETS_SUCCEEDED:
                let result = state.pets || [];
                payload.pets.forEach((pet: PetProps) => {
                    const index = state.pets?.findIndex(p => p._id === pet._id);
                    if (index === -1) {
                        result?.push(pet);
                    } else {
                        result[index] = pet;
                    }
                })
                result.sort((p1, p2) => {
                    if (p1.lastModified && p2.lastModified && p1.lastModified > p2.lastModified) {
                        return -1;
                    } else if (p1.lastModified && p2.lastModified && p1.lastModified === p2.lastModified) {
                        return 0;
                    }
                    return 1;
                });
                let more = true;
                if (state.page * 5 + 5 > result.length) {
                    more = false;
                }
                return {
                    ...state,
                    pets: result,
                    fetching: false,
                    hasMorePets: more,
                    modifiedSince: payload.modifiedSince
                };
            case FETCH_PETS_FAILED:
                return {...state, fetchingError: payload.error, fetching: false};
            case SAVE_PET_STARTED:
                return {...state, savingError: null, saving: true};
            case SAVE_PET_SUCCEEDED:
                let pets = [...(state.pets || [])];
                const pet = payload.pet;
                pets = savePet(pets, pet);
                return {...state, pets: pets, saving: false};
            case SAVE_PET_FAILED:
                if (payload.error.message === "All fields must be entered") {
                    return {...state, savingError: payload.error, saving: false};
                }
                if (payload.pet) {
                    let currentPets = [...(state.pets || [])];
                    const currentPet = payload.pet;
                    currentPets = savePet(currentPets, currentPet);
                    let retrySend = state.sendAgain;
                    if (!payload.sendAgain) {
                        retrySend = 0;
                    } else {
                        retrySend += 1;
                    }
                    return {
                        ...state,
                        savingError: payload.error,
                        saving: false,
                        pets: currentPets,
                        sendAgain: retrySend
                    };
                } else {
                    return {
                        ...state,
                        savingError: payload.error,
                        saving: false
                    };
                }
            case FETCH_NEXT_PAGE:
                let morePets = true;
                if (state.page * 5 + 5 > state.pets.length) {
                    morePets = false;
                }
                return {...state, page: state.page + 1, hasMorePets: morePets};
            default:
                return state;
        }
    };

export const PetContext = React.createContext<PetsState>(initialState);

interface ItemProviderProps {
    children: PropTypes.ReactNodeLike,
}

export const PetProvider: React.FC<ItemProviderProps> = ({children}) => {
    const {token} = useContext(AuthContext);
    const {connected} = useContext(NetworkContext);
    const {loadPhoto, takePhoto} = usePhotoGallery();
    const [state, dispatch] = useReducer(reducer, initialState);
    const {pets, fetching, fetchingError, saving, savingError, page, hasMorePets, sendingUnsaved, sendAgain, modifiedSince, socketClosed} = state;
    useEffect(getPetsLocal, [token]);
    useEffect(fetchModifiedSinceEffect, [token]);
    useEffect(fetchPetsEffect, [token]);
    useEffect(wsEffect, [token, socketClosed]);
    useEffect(sendUnsavedData, [connected, token, sendAgain]);
    const savePet = useCallback<SavePetFn>(savePetCallback, [token]);
    const nextPage = useCallback<NextPageFn>(getNextPage, [token]);
    const resetProfilePage = useCallback<ResetPageFn>(resetProfilePageFn, []);
    const value = {
        pets,
        fetching,
        fetchingError,
        saving,
        savingError,
        savePet,
        page,
        nextPage,
        hasMorePets,
        resetProfilePage,
        sendingUnsaved,
        sendAgain,
        modifiedSince,
        socketClosed,
        takePhoto
    };
    return (
        <PetContext.Provider value={value}>
            {children}
        </PetContext.Provider>
    );

    function sendUnsavedData() {
        if (sendAgain !== 0) {
            setTimeout(() => {
                sendUnsaved();
            }, 1500);
        }
        return () => {
            log('sendUnsaved - disconnecting');
        }

        async function sendUnsaved() {
            log('sendUnsaved ' + connected + " " + sendingUnsaved);
            if (connected && !sendingUnsaved) {
                dispatch({type: SEND_UNSAVED_STARTED});
                let pets = await (await Promise
                    .all((await Storage.keys()).keys
                        .filter(key => key.startsWith("pet"))
                        .map(k => Storage.get({key: k}))))
                    .map(res => JSON.parse(res.value))
                    .filter(pet => pet.saved === false)
                let result = [];
                let conflicts = [];
                let retry = false;
                for (let pet of pets) {
                    let oldPhoto;
                    try {
                        let content;
                        if (pet.photo) {
                            oldPhoto = pet.photo;
                            if (pet.photo.webviewPath?.includes("data:image/jpeg;base64,")) {
                                content = pet.photo.webviewPath.substring(23, pet.photo.webviewPath.length);
                            } else {
                                content = (await loadPhoto(pet.photo)).data;
                            }
                            pet.photo = {filepath: pet.photo?.filepath || "", content};
                        }
                        const savedPet = await (pet._id ? updatePet(token, pet) : createPet(token, pet));
                        log('savePetBackground succeeded');
                        // @ts-ignore
                        savedPet.saved = true;
                        // @ts-ignore
                        if (savedPet.photo) {
                            // @ts-ignore
                            savedPet.photo = {
                                // @ts-ignore
                                filepath: savedPet.photo.filepath,
                                webviewPath: `data:image/jpeg;base64,${content}`
                            };
                        }
                        // @ts-ignore
                        savedPet.frontendId = pet.frontendId;
                        result.push(savedPet);
                        // @ts-ignore
                        await Storage.set({key: `pet${savedPet._id}`, value: JSON.stringify(savedPet)});
                        // @ts-ignore
                        await Storage.remove({key: `pet${pet.frontendId}`});
                    } catch (error) {
                        log('savePetBackground failed');
                        if (error.response) {
                            if (error.response.status === 409) {
                                pet.conflict = true;
                                pet.photo = oldPhoto;
                                await Storage.set({key: `pet${pet._id}`, value: JSON.stringify(pet)});
                                conflicts.push(pet._id);
                            }
                        } else {
                            pet.photo = oldPhoto;
                            retry = true;
                        }
                    }
                }
                dispatch({type: SEND_UNSAVED_SUCCESSFUL, payload: {pets: result, sendAgain: retry, conflicts}});
            }
        }
    }

    function addPetsToLocalStorage(pets: PetProps[]) {
        (async () => {
            for (const pet of pets) {
                await Storage.set({key: `pet${pet._id}`, value: JSON.stringify(pet)});
            }
        })()
    }

    function fetchModifiedSinceEffect() {
        // let canceled = false;
        fetchModifiedSince();
        return () => {
            // canceled = true;
        }

        async function fetchModifiedSince() {
            const mod = await Storage.get({key: "modifiedSince"});
            if (mod) {
                dispatch({type: MODIFIED_SINCE, payload: {modifiedSince: JSON.parse(mod.value)}});
            }
        }
    }

    function fetchPetsEffect() {
        let canceled = false;
        fetchPets();
        return () => {
            canceled = true;
        }

        async function fetchPets() {
            if (!token?.trim()) {
                return;
            }
            try {
                log(`fetchPets started`);
                dispatch({type: FETCH_PETS_STARTED});
                let pets: PetProps[];
                let modSince;
                let responsePets;
                if (modifiedSince) {
                    log('fetchPets with modifySince header');
                    responsePets = await getPetsModifiedSince(token, modifiedSince);
                } else {
                    log('fetchPets without modifySince header');
                    responsePets = await getPets(token);
                }
                if (responsePets.status === 304) {
                    log('fetchPets not modified status = 304');
                    dispatch({type: FETCH_PETS_NOT_MODIFIED});
                    return;
                }
                modSince = responsePets.modifiedSince;
                pets = responsePets.pets || [];
                pets.forEach(pet => {
                    pet.saved = true;
                })
                log('fetchPets succeeded');
                if (!canceled) {
                    pets.forEach(pet => {
                        if (pet.photo) {
                            pet.photo = {
                                filepath: pet.photo?.filepath || "",
                                webviewPath: `data:image/jpeg;base64,${pet.photo?.content}`
                            };
                        }
                    })
                    addPetsToLocalStorage(pets);
                    await Storage.set({key: `modifiedSince`, value: JSON.stringify(modSince)})
                    dispatch({type: FETCH_PETS_SUCCEEDED, payload: {pets: pets, modifiedSince: modSince}});
                }
            } catch (error) {
                log('fetchPets failed');
                dispatch({type: FETCH_PETS_FAILED, payload: {error}});
            }
        }
    }

    async function getNextPage() {
        dispatch({type: FETCH_NEXT_PAGE});
    }

    function resetProfilePageFn() {
        dispatch({type: RESET_PROFILE_PAGE});
    }

    function getPetsLocal() {
        let canceled = false;
        fetchPetsLocal();
        return () => {
            canceled = true;
        }

        async function fetchPetsLocal() {
            if (!token?.trim()) {
                dispatch({type: RESET_PETS});
                return;
            }
            try {
                log('fetchPetsLocal started');
                dispatch({type: FETCH_PETS_STARTED});
                let pets = await (await Promise
                    .all((await Storage.keys()).keys
                        .filter(key => key.startsWith("pet"))
                        .map(k => Storage.get({key: k}))))
                    .map(res => JSON.parse(res.value))
                pets.sort((p1, p2) => {
                    if (p1.name < p2.name) {
                        return -1;
                    } else if (p1.name === p2.name) {
                        return 0;
                    }
                    return 1;
                });
                log('fetchPetsLocal succeeded');
                if (!canceled) {
                    dispatch({type: FETCH_PETS_SUCCEEDED, payload: {pets: pets}});
                }
            } catch (error) {
                log('fetchPetsLocal failed');
                dispatch({type: FETCH_PETS_FAILED, payload: {error}});
            }
        }
    }

    async function savePetCallback(pet: PetProps) {
        let oldPhoto;

        try {
            log('savePet started');
            dispatch({type: SAVE_PET_STARTED});
            const empty = getEmptyFields(pet);
            if (empty.length > 0) {
                log('savePet failed');
                dispatch({type: SAVE_PET_FAILED, payload: {error: {message: "All fields must be entered"}}});
                return empty;
            }
            let content = "";
            if (pet.photo) {
                oldPhoto = pet.photo;
                if (pet.photo.webviewPath?.includes("data:image/jpeg;base64,")) {
                    content = pet.photo.webviewPath.substring(23, pet.photo.webviewPath.length);
                } else {
                    content = (await loadPhoto(pet.photo)).data;
                }
                pet.photo = {filepath: pet.photo?.filepath || "", content};
            }
            const savedPet = await (pet._id ? updatePet(token, pet) : createPet(token, pet));
            log('savePet succeeded');
            // @ts-ignore
            savedPet.saved = true;
            // @ts-ignore
            if (savedPet.photo) {
                // @ts-ignore
                savedPet.photo = {filepath: savedPet.photo.filepath, webviewPath: `data:image/jpeg;base64,${content}`};
            }
            // @ts-ignore
            await Storage.set({key: `pet${savedPet._id}`, value: JSON.stringify(savedPet)});
            dispatch({type: SAVE_PET_SUCCEEDED, payload: {pet: savedPet}});
            return [];
        } catch (error) {
            log('savePet failed');
            if (error.response) {
                if (error.response.status === 409) {
                    pet.conflict = true;
                    pet.saved = false;
                    pet.photo = oldPhoto;
                    await Storage.set({key: `pet${pet._id}`, value: JSON.stringify(pet)});
                    dispatch({type: CONFLICT, payload: {petId: pet._id}});
                    return true;
                } else {
                    dispatch({type: SAVE_PET_FAILED, payload: {error}});
                    return false;
                }
            } else {
                pet.saved = false;
                pet.photo = oldPhoto;
                if (pet._id) {
                    await Storage.set({key: `pet${pet._id}`, value: JSON.stringify(pet)});
                } else if (pet.frontendId) {
                    await Storage.set({key: `pet${pet.frontendId}`, value: JSON.stringify(pet)});
                } else {
                    pet.frontendId = uuidv4();
                    await Storage.set({key: `pet${pet.frontendId}`, value: JSON.stringify(pet)});
                }
                dispatch({type: SAVE_PET_FAILED, payload: {error, pet, sendAgain: true}});
            }
            return true;
        }
    }

    function wsEffect() {
        let closeWebSocket: () => void;
        if (token?.trim() && socketClosed) {
            log('wsEffect - connecting');
            closeWebSocket = newWebSocket(token, async message => {
                const {event, payload} = message;
                let pet = payload;
                log(`ws message, pet ${event}`);
                if (event === 'created' || event === 'updated') {
                    pet.saved = true;
                    pet.photo = {
                        filepath: pet.photo?.filepath || "",
                        webviewPath: `data:image/jpeg;base64,${pet.photo?.content}`
                    };
                    await Storage.set({key: `pet${pet._id}`, value: JSON.stringify(pet)});
                    dispatch({type: SAVE_PET_SUCCEEDED, payload: {pet}});
                }
            }, () => {
                log("ws onClose");
                dispatch({type: SOCKET_CLOSED, payload: {closed: true}});
            });
            log("ws connected");
            dispatch({type: SOCKET_CLOSED, payload: {closed: false}});
        }
        return () => {
            log('wsEffect - disconnecting');
            // closeWebSocket?.();
        }
    }

    function getEmptyFields(pet: PetProps) {
        let empty = [];
        if (pet.name === "") {
            empty.push(".name-item");
        }
        if (pet.description === "") {
            empty.push(".descr-item");
        }
        if (pet.breed === "") {
            empty.push(".breed-item");
        }
        if (pet.type === "") {
            empty.push(".type-item");
        }
        return empty;
    }
};
