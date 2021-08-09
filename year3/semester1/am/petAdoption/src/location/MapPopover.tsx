import {IonButton, IonButtons, IonContent, IonHeader, IonIcon, IonPage, IonToolbar} from '@ionic/react';
import React, {useEffect, useState} from 'react';
import {MyMap} from "./MyMap";
import "./Map.css";
import {PetProfileProps} from "../petadoption/PetProfile";
import {Geolocation, GeolocationPosition} from "@capacitor/core";
import {arrowBack, save} from "ionicons/icons";
import {getLogger} from "../core";

const log = getLogger("MapPopover");

export interface MyLocation {
    position?: LocPosition;
    error?: Error;
}

export interface LocPosition {
    lat: number;
    lng: number;
}

export const MapPopover: React.FC<PetProfileProps> = ({match, history}) => {
    const [location, setLocation] = useState<MyLocation>({});
    useEffect(fetchMyLocation, [match.params.id]);
    const {lat, lng} = location.position || {};
    return (
        <IonPage>
            <IonHeader>
            </IonHeader>
            <IonContent class="map" fullscreen>
                <IonToolbar>
                    <IonButtons slot="start">
                        <IonButton onClick={() => {
                            history.push({pathname: `/pet/${match.params.id}`, state: history.location.state})
                        }}>
                            <IonIcon icon={arrowBack}/>
                        </IonButton>
                    </IonButtons>
                    <IonButtons slot="end">
                        <IonButton onClick={() => {
                            const pet = history.location.state;
                            // @ts-ignore
                            pet.location = {lat, lng};
                            history.push({pathname: `/pet/${match.params.id}`, state: pet})
                        }}>
                            <IonIcon icon={save}/>
                        </IonButton></IonButtons>
                </IonToolbar>
                {lat && lng &&
                <MyMap
                    class="map"
                    lat={lat}
                    lng={lng}
                    onMapClick={(e: any) => {
                        log("onMapClick");
                        setLocation({...location, position: {lat: e.latLng.lat(), lng: e.latLng.lng()}});
                    }}
                    onMarkerClick={log('onMarker')}
                />}
            </IonContent>
        </IonPage>
    );

    // function log(source: string) {
    //     return (e: any) => console.log(source, e.latLng.lat(), e.latLng.lng());
    // }

    function fetchMyLocation() {
        log("fetchMyLocation");
        let cancelled = false;
        // @ts-ignore
        if (history.location.state) {
            // @ts-ignore
            if (history.location.state.location) {
                // @ts-ignore
                const position = history.location.state.location;
                const myPosition = {lat: position.lat || 0, lng: position.lng || 0};
                setLocation({...location, position: myPosition});
                return;
            }
        }
        Geolocation.getCurrentPosition()
            .then(position => updateMyPosition('current', position))
            .catch(error => updateMyPosition('current', undefined, error));
        return () => {
            cancelled = true;
        };

        function updateMyPosition(source: string, position?: GeolocationPosition, error: any = undefined) {
            console.log(source, position, error);
            if (!cancelled) {
                const myPosition = {lat: position?.coords.latitude || 0, lng: position?.coords.longitude || 0};
                setLocation({position: myPosition, error});
            }
        }
    }
};

