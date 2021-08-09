import {
    createAnimation,
    IonButton,
    IonButtons,
    IonContent,
    IonFab,
    IonFabButton,
    IonHeader,
    IonIcon,
    IonInfiniteScroll,
    IonInfiniteScrollContent,
    IonList,
    IonLoading,
    IonPage,
    IonSearchbar,
    IonSelect,
    IonSelectOption,
    IonTitle,
    IonToolbar
} from '@ionic/react';
import React, {useContext, useEffect, useState} from 'react';
import {PetContext} from "./PetProvider";
import Pet from "./Pet";
import {getLogger} from "../core";
import {add, ellipse, logOut} from "ionicons/icons";
import {RouteComponentProps} from "react-router";
import {AuthContext} from "../auth";
import "../core/Network.css"
import {NetworkContext} from "../core/NetworkProvider";

const log = getLogger('PetList');

const PetList: React.FC<RouteComponentProps> = ({history}) => {
    const {pets, fetching, fetchingError, nextPage, hasMorePets, page} = useContext(PetContext);
    const {connected} = useContext(NetworkContext);
    const [disableInfiniteScroll, setDisableInfiniteScroll] = useState<boolean>(false);
    const {logout} = useContext(AuthContext);
    const [type, setType] = useState("all");
    const [searchBreed, setSearchBreed] = useState("");
    useEffect(() => {
        setDisableInfiniteScroll(!hasMorePets);
    }, [hasMorePets]);
    useEffect(addButtonAnimation, []);


    async function searchNext(e: CustomEvent<void>) {
        log("searchNext");
        await nextPage?.();
        (e.target as HTMLIonInfiniteScrollElement).complete();
    }

    // log('render');
    return (
        <IonPage>
            <IonHeader>
                <IonToolbar color="primary">
                    <IonButtons slot="start">
                        <IonSelect value={type} onIonChange={e => setType(e.detail.value || '')}>
                            <IonSelectOption value="all">All</IonSelectOption>
                            <IonSelectOption value="cat">Cat</IonSelectOption>
                            <IonSelectOption value="dog">Dog</IonSelectOption>
                        </IonSelect>
                    </IonButtons>
                    <IonTitle class="ion-text-center">Pet Adoption
                        {connected ? (<IonIcon class="network-icon" color="success" icon={ellipse}/>) :
                            (<IonIcon class="network-icon" color="danger" icon={ellipse}/>)}
                    </IonTitle>
                    <IonButtons slot="end">
                        <IonButton onClick={() => logout?.()}>
                            <IonIcon icon={logOut}/>
                        </IonButton>
                    </IonButtons>
                </IonToolbar>
            </IonHeader>
            <IonContent fullscreen>
                <IonSearchbar
                    placeholder="Search breed"
                    value={searchBreed}
                    debounce={500}
                    class="search-bar"
                    onIonChange={e => setSearchBreed(e.detail.value!)}>
                </IonSearchbar>
                <IonLoading isOpen={fetching} message="Fetching pets"/>
                {pets && (
                    <IonList>
                        {pets
                            .filter(pet => {
                                let validType = true;
                                if (type !== "all") {
                                    validType = pet.type === type;
                                }
                                let validBreed = true;
                                if (searchBreed !== "") {
                                    validBreed = pet.breed?.indexOf(searchBreed) !== -1;
                                }
                                return validType && validBreed;
                            })
                            .map(({
                                      _id, name, type, breed, birthDate,
                                      frontendId, saved, version, conflict, photo
                                  }) => {
                                    return <Pet key={_id || frontendId} _id={_id} name={name} type={type} breed={breed}
                                                birthDate={birthDate}
                                                frontendId={frontendId}
                                                saved={saved}
                                                version={version}
                                                conflict={conflict}
                                                photo={photo}
                                                onEdit={id => {
                                                    history.push(`/pet/${id}`)
                                                }}/>
                                }
                            )
                            .slice(0, page * 5 + 5)}
                    </IonList>
                )}
                {/*{fetchingError && (*/}
                {/*    <div>{fetchingError.message || 'Failed to fetch pets'}</div>*/}
                {/*)}*/}
                <IonInfiniteScroll threshold="100px" disabled={disableInfiniteScroll}
                                   onIonInfinite={(e: CustomEvent<void>) => searchNext(e)}>
                    <IonInfiniteScrollContent
                        loadingText="Loading more pets...">
                    </IonInfiniteScrollContent>
                </IonInfiniteScroll>
                <IonFab vertical="bottom" horizontal="end" slot="fixed">
                    <IonFabButton class="add-pet-btn" onClick={() => history.push('/pet')}>
                        <IonIcon icon={add}/>
                    </IonFabButton>
                </IonFab>
            </IonContent>
        </IonPage>
    );

    function addButtonAnimation() {
        const el = document.querySelector('.add-pet-btn');
        if (el) {
            const animation = createAnimation()
                .addElement(el)
                .duration(1000)
                .keyframes([
                    { offset: 0, transform: 'scale(3)', opacity: '0.5' },
                    {
                        offset: 1, transform: 'scale(1)', opacity: '1'
                    }
                ]);
            animation.play();
        }
    }
};

export default PetList;
