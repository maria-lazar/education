import React from 'react';
import {Redirect, Route} from 'react-router-dom';
import {IonApp, IonRouterOutlet} from '@ionic/react';
import {IonReactRouter} from '@ionic/react-router';
import PetList from './petadoption/PetList';
import {PetProvider} from './petadoption/PetProvider';
import {MapPopover} from './location/MapPopover';

/* Core CSS required for Ionic components to work properly */
import '@ionic/react/css/core.css';

/* Basic CSS for apps built with Ionic */
import '@ionic/react/css/normalize.css';
import '@ionic/react/css/structure.css';
import '@ionic/react/css/typography.css';

/* Optional CSS utils that can be commented out */
import '@ionic/react/css/padding.css';
import '@ionic/react/css/float-elements.css';
import '@ionic/react/css/text-alignment.css';
import '@ionic/react/css/text-transformation.css';
import '@ionic/react/css/flex-utils.css';
import '@ionic/react/css/display.css';

/* Theme variables */
import './theme/variables.css';
import PetProfile from "./petadoption/PetProfile";
import {AuthProvider, Login} from "./auth";
import {PrivateRoute} from "./auth/PrivateRoute";
import {NetworkProvider} from "./core/NetworkProvider";

const App: React.FC = () => (
    <IonApp>
        <IonReactRouter>
            <IonRouterOutlet>
                <NetworkProvider>
                    <AuthProvider>
                        <Route path="/login" component={Login} exact={true}/>
                        <PetProvider>
                            <PrivateRoute path="/pet" component={PetProfile} exact={true}/>
                            <PrivateRoute path="/pet/:id" component={PetProfile} exact={true}/>
                            <PrivateRoute path="/pets" component={PetList} exact={true}/>
                            <PrivateRoute path="/pet/:id/location" component={MapPopover} exact={true}/>
                        </PetProvider>
                        <Route exact path="/" render={() => <Redirect to="/pets"/>}/>
                    </AuthProvider>
                </NetworkProvider>
            </IonRouterOutlet>
        </IonReactRouter>
    </IonApp>
);

export default App;
