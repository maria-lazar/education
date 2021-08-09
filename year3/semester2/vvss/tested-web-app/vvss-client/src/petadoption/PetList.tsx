import {
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
    IonTitle,
    IonToolbar
} from '@ionic/react';
import React, {useContext, useEffect, useState} from 'react';
import {PetContext} from "./PetProvider";
import Pet from "./Pet";
import {getLogger} from "../core";
import {add, logOut} from "ionicons/icons";
import {RouteComponentProps} from "react-router";
import {AuthContext} from "../auth";
import "../core/Network.css"

const log = getLogger('PetList');

const PetList: React.FC<RouteComponentProps> = ({history}) => {
    const {pets, fetching, fetchingError, nextPage, hasMorePets, page, deletePet} = useContext(PetContext);
    const [disableInfiniteScroll, setDisableInfiniteScroll] = useState<boolean>(false);
    const {logout} = useContext(AuthContext);
    useEffect(() => {
        setDisableInfiniteScroll(!hasMorePets);
    }, [hasMorePets]);


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
                    <IonTitle id="title" class="ion-text-center">Pet Adoption
                    </IonTitle>
                    <IonButtons slot="end">
                        <IonButton id="logoutBtn" onClick={() => logout?.()}>
                            <IonIcon icon={logOut}/>
                        </IonButton>
                    </IonButtons>
                </IonToolbar>
            </IonHeader>
            <IonContent fullscreen>
                <IonLoading isOpen={fetching} message="Fetching pets"/>
                {pets && (
                    <IonList id="itemList">
                        {pets
                            .map(({
                                      _id, name,
                                      frontendId, saved, version
                                  }) => {
                                    return <Pet key={_id || frontendId} _id={_id} name={name}
                                                frontendId={frontendId}
                                                saved={saved}
                                                version={version}
                                                onEdit={id => {
                                                    if (deletePet) {
                                                        if (id != null) {
                                                            deletePet(id)
                                                        }
                                                    }
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
                    <IonFabButton id="addBtn" class="add-pet-btn" onClick={() => history.push('/pet')}>
                        <IonIcon icon={add}/>
                    </IonFabButton>
                </IonFab>
            </IonContent>
        </IonPage>
    );
};

export default PetList;
