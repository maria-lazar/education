import {getLogger} from "../core";
import {RouteComponentProps} from "react-router";
import React, {useContext, useState} from "react";
import {PetContext} from "./PetProvider";
import {
    IonButton,
    IonButtons,
    IonCard,
    IonCardContent,
    IonContent,
    IonHeader,
    IonIcon,
    IonInput,
    IonItem,
    IonLabel,
    IonLoading,
    IonPage,
    IonTitle,
    IonToolbar
} from '@ionic/react';
import {save} from 'ionicons/icons'

const log = getLogger('PetProfile');

export interface PetProfileProps extends RouteComponentProps<{
    id?: string;
}> {
}


const PetProfile: React.FC<PetProfileProps> = ({history, match}) => {
    const {saving, savingError, savePet, resetProfilePage} = useContext(PetContext);
    const [name, setName] = useState<string>('');

    const handleSave = () => {
        const editedPet: any = {
            name
        };
        savePet?.(editedPet).then((value) => {
            if (value.length === 0) {
                resetProfilePage?.();
                history.push("/pets");
            }
        });
    };

    log('render');
    return (
        <IonPage>
            <IonHeader>
                <IonToolbar color="primary">
                    <IonTitle id="title">Add
                    </IonTitle>
                    <IonButtons slot="end">
                        <IonButton id="saveBtn" onClick={handleSave}>
                            <IonIcon icon={save}/>
                        </IonButton>
                    </IonButtons>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <IonLoading isOpen={saving}/>
                <IonCard class="profile-card">
                    <IonCardContent>
                        <IonItem class="name-item">
                            <IonLabel color="medium">Name:</IonLabel>
                            <IonInput id="name" class="ion-text-end" value={name} color="dark" onIonChange={e => {
                                setName(e.detail.value || '')
                            }}/>
                        </IonItem>
                    </IonCardContent>
                </IonCard>
                {savingError && (
                    <IonLabel class="ion-padding-start">{savingError.message || 'Failed to save item'}</IonLabel>
                )}
            </IonContent>
        </IonPage>
    );
}
export default PetProfile;
