import {getLogger} from "../core";
import {RouteComponentProps} from "react-router";
import React, {useContext, useEffect, useState} from "react";
import {PetContext} from "./PetProvider";
import {PetProps} from "./PetProps";
import {
    createAnimation,
    IonButton,
    IonButtons,
    IonCard,
    IonCardContent,
    IonCardTitle,
    IonContent,
    IonDatetime,
    IonHeader,
    IonIcon,
    IonImg,
    IonInput,
    IonItem,
    IonLabel,
    IonLoading,
    IonModal,
    IonPage,
    IonSelect,
    IonSelectOption,
    IonTextarea,
    IonTitle,
    IonToolbar
} from '@ionic/react';
import "../location/Map.css"
import {arrowBack, ellipse, image, locationOutline, save, trashBin} from 'ionicons/icons'
import {NetworkContext} from "../core/NetworkProvider";
import {getPet} from "./petApi";
import {AuthContext} from "../auth";
import {Photo} from "../photo/usePhotoGallery";
import {LocPosition} from "../location/MapPopover";

const log = getLogger('PetProfile');

const mandatoryFields = [".name-item", ".type-item", ".breed-item", ".descr-item"];
const fields = [".name-item", ".type-item", "breed-item", "descr-item", "weight-item", "vaccinated-item", "birth-item", "location-item", "owner-item"];

export interface PetProfileProps extends RouteComponentProps<{
    id?: string;
}> {
}

let petVaccinated = (vaccinated?: boolean) => {
    if (vaccinated) {
        return "true";
    } else {
        return "false";
    }
}

let getBirthDate = (birthDate?: number) => {
    return new Date(birthDate || Date.now()).toLocaleDateString("en-US")
}

let extractDate = (value: any) => {
    return new Date(value).getTime();
}
let convertToBoolean = (value: any) => {
    if (typeof value != "boolean") {
        return value === "true";
    }
    return value;
}

const PetProfile: React.FC<PetProfileProps> = ({history, match}) => {
    const {pets, saving, savingError, savePet, resetProfilePage, takePhoto} = useContext(PetContext);
    const {token} = useContext(AuthContext);
    const {connected} = useContext(NetworkContext);
    const [pet, setPet] = useState<PetProps>();
    const [name, setName] = useState<string>('');
    const [type, setType] = useState<string>('');
    const [owner, setOwner] = useState<string>('');
    const [ownerName, setOwnerName] = useState<string>('');
    const [description, setDescription] = useState<string>('');
    const [breed, setBreed] = useState<string>('');
    const [weight, setWeight] = useState<number>(0);
    const [vaccinated, setVaccinated] = useState<boolean>(false);
    const [birthDate, setBirthDate] = useState<number>(Date.now());
    const [conflictPet, setConflictPet] = useState<PetProps>();
    const [lastModified, setLastModified] = useState<number>(0);
    const [photo, setPhoto] = useState<Photo>();
    const [location, setLocation] = useState<LocPosition>();
    const [showModal, setShowModal] = useState(false);
    useEffect(setEditedPet, [match.params.id, pets]);

    const handleSave = () => {
        const editedPet: any = {
            name,
            owner,
            description,
            breed,
            weight,
            vaccinated,
            birthDate,
            type,
            ownerName,
            lastModified,
            photo,
            location
        };
        if (pet) {
            editedPet._id = pet._id;
            editedPet.frontendId = pet.frontendId;
            editedPet.version = pet.version;
        }
        if (conflictPet) {
            // resolving conflict
            editedPet.version = conflictPet.version;
        } else {
            editedPet.lastModified = Date.now();
        }
        savePet?.(editedPet).then((value) => {
            if (value.length === 0) {
                resetProfilePage?.();
                history.push("/pets");
            } else {
                emptyFieldsGroupAnimation(value);
            }
        });
    };
    const handleDiscard = () => {
        const editedPet = conflictPet;
        if (editedPet) {
            savePet?.(editedPet).then((value) => {
                if (value) {
                    resetProfilePage?.();
                    history.push("/pets");
                }
            });
        }
    };

    // log('render');
    return (
        <IonPage>
            <IonHeader>
                <IonToolbar color="primary">
                    {!conflictPet && (<IonButtons slot="start">
                        <IonButton onClick={() => {
                            resetProfilePage?.();
                            setShowModal(true);
                        }}>
                            <IonIcon icon={arrowBack}/>
                        </IonButton>
                    </IonButtons>)}
                    {!pet ? (
                            <IonTitle>Add
                                {connected ? (<IonIcon class="network-icon" color="success" icon={ellipse}/>) :
                                    (<IonIcon class="network-icon" color="danger" icon={ellipse}/>)}
                            </IonTitle>) :
                        <IonTitle>{name}
                            {connected ? (<IonIcon class="network-icon" color="success" icon={ellipse}/>) :
                                (<IonIcon class="network-icon" color="danger" icon={ellipse}/>)}
                        </IonTitle>}
                    <IonButtons slot="end">
                        {conflictPet && (<IonButton onClick={handleDiscard}>
                            <IonIcon icon={trashBin}/>
                        </IonButton>)}
                        <IonButton onClick={handleSave}>
                            <IonIcon icon={save}/>
                        </IonButton>
                    </IonButtons>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <IonLoading isOpen={saving}/>
                <IonCard class="profile-card">
                    <IonCardContent>
                        <IonItem>
                            <div className="photo-container">
                                {!photo ?
                                    <IonIcon class="pet-icon" icon={image}/> :
                                    <IonImg class="pet-photo" src={photo?.webviewPath}/>}
                                <IonButton onClick={() => openCamera()} color="light"
                                           class="change-photo">Change
                                    photo</IonButton>
                            </div>
                        </IonItem>
                        <IonItem class="name-item transparent">
                            <IonLabel color="medium">Name:</IonLabel>
                            <IonInput class="ion-text-end" value={name} color="dark" onIonChange={e => {
                                setName(e.detail.value || '')
                            }}/>
                        </IonItem>
                        <IonItem class="descr-item transparent">
                            <IonLabel color="medium">Description:</IonLabel>
                            <IonTextarea autoGrow value={description} className="ion-text-wrap"
                                         onIonChange={e => {
                                             setDescription(e.detail.value || '')
                                         }}/>
                        </IonItem>
                        <IonItem class="type-item transparent">
                            <IonLabel color="medium">Type:</IonLabel>
                            <IonSelect value={type}
                                       onIonChange={e => setType(e.detail.value || '')}>
                                <IonSelectOption value="cat">Cat</IonSelectOption>
                                <IonSelectOption value="dog">Dog</IonSelectOption>
                            </IonSelect>
                        </IonItem>
                        <IonItem class="breed-item transparent">
                            <IonLabel color="medium">Breed:</IonLabel>
                            <IonInput class="ion-text-end" value={breed} color="dark" onIonChange={e => {
                                setBreed(e.detail.value || '')
                            }}/>
                        </IonItem>
                        <IonItem class="weight-item transparent">
                            <IonLabel color="medium">Weight:</IonLabel>
                            <IonInput class="ion-text-end" color="dark" value={weight} onIonChange={e => {
                                setWeight(parseFloat(e.detail.value || "0"))
                            }}/>
                        </IonItem>
                        <IonItem class="vaccinated-item transparent">
                            <IonLabel color="medium">Vaccinated:</IonLabel>
                            <IonSelect value={petVaccinated(vaccinated)}
                                       onIonChange={e => setVaccinated(convertToBoolean(e.detail.value || ''))}>
                                <IonSelectOption value="true">True</IonSelectOption>
                                <IonSelectOption value="false">False</IonSelectOption>
                            </IonSelect>
                        </IonItem>
                        <IonItem class="birth-item transparent">
                            <IonLabel color="medium">Born on:</IonLabel>
                            <IonDatetime value={getBirthDate(birthDate)}
                                         placeholder="Select Date"
                                         onIonChange={e => setBirthDate(extractDate(e.detail.value))}/>
                        </IonItem>
                        <IonItem class="location-item transparent">
                            <IonIcon icon={locationOutline}/>
                            <IonButton slot={location ? "" : "end"}
                                       fill="clear"
                                       class="location-btn"
                                       onClick={() => {
                                           const state = {
                                               name,
                                               owner,
                                               description,
                                               breed,
                                               weight,
                                               vaccinated,
                                               birthDate,
                                               type,
                                               ownerName,
                                               lastModified,
                                               photo,
                                               location
                                           };
                                           history.push({pathname: `/pet/${match.params.id}/location`, state: state});
                                       }}>
                                {location ?
                                    (location.lat + "," + location.lng) :
                                    "Open map"
                                }</IonButton>
                        </IonItem>
                        {pet && <IonItem class="owner-item transparent">
                            <IonLabel color="medium">Owner:</IonLabel>
                            <IonInput disabled class="ion-text-end" value={ownerName}
                                      color="dark"/>
                        </IonItem>}
                    </IonCardContent>
                </IonCard>
                {conflictPet && (<IonCard>
                    <IonCardContent><IonCardTitle>Conflict current pet:</IonCardTitle>
                        <div>Name: {conflictPet?.name}</div>
                        <div>Description: {conflictPet?.description}</div>
                        <div>Type: {conflictPet?.type}</div>
                        <div>Breed: {conflictPet?.breed}</div>
                        <div>Date of birth: {conflictPet?.birthDate}</div>
                        <div>Weight: {conflictPet?.weight}</div>
                        <div>Vaccinated: {conflictPet?.vaccinated}</div>
                    </IonCardContent>
                </IonCard>)}
                {savingError && (
                    <IonLabel class="ion-padding-start">{savingError.message || 'Failed to save item'}</IonLabel>
                )}
                <IonModal cssClass="back-modal" isOpen={showModal} enterAnimation={enterAnimation}
                          leaveAnimation={leaveAnimation}>
                    <IonContent class="page">
                        <div className="modal-container">
                            <p>If you continue your changes will be lost.</p>
                            <p>Continue?</p>
                            <IonButton onClick={() => {
                                setShowModal(false);
                                history.push("/pets");
                            }}>Yes</IonButton>
                            <IonButton onClick={() => setShowModal(false)}>No</IonButton>
                        </div>
                    </IonContent>
                </IonModal>
            </IonContent>
        </IonPage>
    );

    function setEditedPet() {
        setEdited();
        return () => {
            log("setEditedPet disconnect")
        }

        async function setEdited() {
            log('useEffect - setEditedPet');
            const routeId = match.params.id || '';
            let pet = pets?.find(p => (p._id === routeId) || (p.frontendId === routeId));
            setPet(pet);
            // @ts-ignore
            if (history.location.state) {
                // @ts-ignore
                pet = history.location.state;
            }
            if (pet) {
                setName(pet.name || '');
                setDescription(pet.description || '');
                setBreed(pet.breed || '');
                setWeight(pet.weight || 0);
                setVaccinated(pet.vaccinated || false);
                setBirthDate(pet.birthDate || Date.now());
                setOwner(pet.owner || '');
                setType(pet.type || '');
                setOwnerName(pet.ownerName || '');
                setLastModified(pet.lastModified || 0);
                setPhoto(pet.photo);
                setLocation(pet.location);
                if (pet.conflict) {
                    const currentPet = await getPet(token, pet._id);
                    // @ts-ignore
                    setConflictPet(currentPet);
                }
            }
            simpleChainedAnimation();
        }
    }

    async function openCamera() {
        const p = await takePhoto?.();
        setPhoto(p);
    }


    function simpleAnimation(field: string) {
        const el = document.querySelector(field);
        if (el) {
            return createAnimation()
                .addElement(el)
                .duration(200)
                .keyframes([
                    {offset: 0, opacity: 0},
                    {
                        offset: 1, opacity: 1
                    }
                ]);
        }
    }

    function simpleChainedAnimation() {
        const animationName = simpleAnimation(".name-item");
        const animationDescr = simpleAnimation(".descr-item");
        const animationType = simpleAnimation(".type-item");
        const animationBreed = simpleAnimation(".breed-item");
        const animationWeight = simpleAnimation(".weight-item");
        const animationVaccinated = simpleAnimation(".vaccinated-item");
        const animationBirth = simpleAnimation(".birth-item");
        const animationLocation = simpleAnimation(".location-item");
        const animationOwner = simpleAnimation(".owner-item");
        (async () => {
            await animationName?.play();
            await animationDescr?.play();
            await animationType?.play();
            await animationBreed?.play();
            await animationWeight?.play();
            await animationVaccinated?.play();
            await animationBirth?.play();
            await animationLocation?.play();
            await animationOwner?.play();
        })()
    }


    function shakeAnimation(item: string) {
        const el = document.querySelector(item);
        if (el) {
            return createAnimation()
                .addElement(el)
                .duration(2000)
                .beforeStyles({
                    '--border-color': 'red'
                })
                .keyframes([
                    {offset: 0, transform: "translateX(0)"},
                    {offset: 0.15, transform: "translateX(-3px) rotateY(-5deg)"},
                    {offset: 0.20, transform: "translateX(2px) rotateY(4deg)"},
                    {offset: 0.30, transform: "translateX(-2px) rotateY(4deg)"},
                    {offset: 0.40, transform: "translateX(2px) rotateY(4deg)"},
                    {offset: 0.50, transform: "translateX(-1px) rotateY(-2deg)"},
                    {offset: 0.60, transform: "translateX(3px) rotateY(1deg)"},
                    {offset: 0.70, transform: "translateX(-1px) rotateY(-2deg)"},
                    {offset: 0.80, transform: "translateX(2px) rotateY(-2deg)"},
                    {offset: 0.90, transform: "translateX(-3px) rotateY(1deg)"},
                    {offset: 1, transform: "translateX(0)"}
                ]);
        }
    }

    function normalBorder(item: string) {
        const el = document.querySelector(item);
        if (el) {
            return createAnimation()
                .addElement(el)
                .beforeStyles({
                    '--border-color': '#d7d8da'
                });
        }
    }

    function emptyFieldsGroupAnimation(value: any) {
        const clearAnimation = createAnimation();
        mandatoryFields.forEach((field: string) => {
            const a = normalBorder(field);
            if (a) {
                clearAnimation.addAnimation(a);
            }
        });
        const parentAnimation = createAnimation();
        value.forEach((field: string) => {
            const a = shakeAnimation(field);
            if (a) {
                parentAnimation.addAnimation(a);
            }
        });
        (async () => {
            await clearAnimation.play();
            await parentAnimation.play();
        })()
    }

    function enterAnimation(baseEl: any) {
        const backdropAnimation = createAnimation()
            .addElement(baseEl.querySelector('ion-backdrop')!)
            .fromTo('opacity', '0.01', 'var(--backdrop-opacity)');

        const wrapperAnimation = createAnimation()
            .addElement(baseEl.querySelector('.modal-wrapper')!)
            .keyframes([
                {offset: 0, opacity: '0', transform: 'scale(0)'},
                {offset: 1, opacity: '0.99', transform: 'scale(1)'}
            ]);

        return createAnimation()
            .addElement(baseEl)
            .easing('ease-out')
            .duration(300)
            .addAnimation([backdropAnimation, wrapperAnimation]);
    }

    function leaveAnimation(baseEl: any) {
        return enterAnimation(baseEl).direction('reverse');
    }
}
export default PetProfile;
