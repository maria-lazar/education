import {PetProps} from "./PetProps";
import {createAnimation, IonChip, IonIcon, IonItem, IonLabel} from "@ionic/react";
import React, {useEffect} from "react";
import "./Pet.css"
import {alertCircleOutline, cloudOfflineOutline, image} from "ionicons/icons";

interface PetPropsExt extends PetProps {
    onEdit: (id?: string) => void;
}

const Pet: React.FC<PetPropsExt> = ({photo, saved, _id, name, type, breed, birthDate, onEdit, frontendId, conflict}) => {
    return (
        <IonItem onClick={() => onEdit(_id || frontendId)}>
            <div className="pet-div">
                <div className="pet-image-container">
                    {!photo ?
                        <IonIcon class="pet-icon-list" icon={image}/> :
                        // <IonImg class="pet-photo-list" src={photo?.webviewPath}/>}
                        <img className="pet-photo-list" src={photo?.webviewPath}/>}
                    <div>
                        <div className="pet-name">
                            <IonLabel>
                                {name}
                                {!saved && (<IonIcon class="not-saved-icon" icon={alertCircleOutline}/>)}
                                {conflict && (<IonIcon class="not-saved-icon" icon={cloudOfflineOutline}/>)}
                            </IonLabel>
                        </div>
                        <IonChip class="chip-type pet-chip ion-margin-top">
                            <IonLabel>{type}</IonLabel>
                        </IonChip>
                        <IonChip class="chip-breed pet-chip ion-margin-top">
                            <IonLabel>{breed}</IonLabel>
                        </IonChip>
                    </div>
                </div>

                <div className="pet-date"><IonLabel class="ion-text-wrap">Born
                    on:<br/> {new Date(birthDate || Date.now()).toLocaleDateString("en-US")}
                </IonLabel></div>
            </div>
        </IonItem>
    )

    function simpleAnimation() {
        const el = document.querySelector('.chip-type');
        if (el) {
            const animation = createAnimation()
                .addElement(el)
                .duration(1000)
                .direction('alternate')
                .iterations(Infinity)
                .keyframes([
                    {offset: 0, transform: 'scale(3)', opacity: '1'},
                    {
                        offset: 1, transform: 'scale(1.5)', opacity: '0.5'
                    }
                ]);
            animation.play();
        }
    }
}

export default Pet;