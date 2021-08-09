import React, {useContext, useState} from "react";
import {Redirect, RouteComponentProps} from "react-router";
import {getLogger} from "../core";
import "../core/Network.css"
import {
    IonButton,
    IonCol,
    IonContent,
    IonGrid,
    IonHeader,
    IonIcon,
    IonInput,
    IonLoading,
    IonPage,
    IonRow,
    IonTitle,
    IonToolbar
} from '@ionic/react';
import {AuthContext} from "./AuthProvider";
import {ellipse} from "ionicons/icons";
import {NetworkContext} from "../core/NetworkProvider";

const log = getLogger('PetProfile');

interface LoginState {
    username?: string;
    password?: string;
}

export const Login: React.FC<RouteComponentProps> = ({history}) => {
    const {isAuthenticated, isAuthenticating, login, authenticationError} = useContext(AuthContext);
    const {connected} = useContext(NetworkContext);
    const [state, setState] = useState<LoginState>({});
    const {username, password} = state;
    log('render');
    const handleLogin = () => {
        log('handleLogin...');
        login?.(username, password);
    };
    if (isAuthenticated) {
        return <Redirect to={{pathname: '/'}}/>
    }
    return (
        <IonPage>
            <IonHeader>
                <IonToolbar color="primary">
                    <IonTitle id="title">Login
                        {connected ? (<IonIcon class="network-icon" color="success" icon={ellipse}/>) :
                            (<IonIcon class="network-icon" color="danger" icon={ellipse}/>)}
                    </IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <IonGrid class="ion-padding-top ion-margin-top">
                    <IonRow class="ion-padding-top ion-margin-top">
                        <IonCol class="ion-text-center ion-padding-top ion-margin-top">
                            <IonInput id="username"
                                      class="ion-padding"
                                      placeholder="Username"
                                      value={username}
                                      onIonChange={e => setState({
                                          ...state,
                                          username: e.detail.value || ''
                                      })}/>
                            <IonInput id="password"
                                      class="ion-padding"
                                      placeholder="Password"
                                      type="password"
                                      value={password}
                                      onIonChange={e => setState({
                                          ...state,
                                          password: e.detail.value || ''
                                      })}/>
                            <IonButton id="loginBtn" class="ion-margin-top" onClick={handleLogin}>Login</IonButton>
                            <IonLoading isOpen={isAuthenticating}/>
                            {authenticationError && (
                                <div id="loginError">{'Failed to authenticate'}</div>
                            )}
                        </IonCol>
                    </IonRow>
                </IonGrid>
            </IonContent>
        </IonPage>
    );
};