import {PetProps} from "./PetProps";
import {IonButton, IonIcon, IonItem, IonLabel} from "@ionic/react";
import React from "react";
import "./Pet.css"
import {closeSharp} from "ionicons/icons";

interface PetPropsExt extends PetProps {
    onEdit: (id?: string) => void;
}

const Pet: React.FC<PetPropsExt> = ({_id, name, onEdit}) => {
    return (
        <IonItem>
            <div className="pet-div">
                <div className="pet-image-container">
                    <div className="pet-name">
                        {name}
                    </div>
                </div>

                <IonButton class="deleteBtn" fill="clear" slot="end" onClick={() => onEdit(_id)}>
                    <IonIcon color="dark" icon={closeSharp}/>
                </IonButton>
            </div>
        </IonItem>
    )
}

export default Pet;