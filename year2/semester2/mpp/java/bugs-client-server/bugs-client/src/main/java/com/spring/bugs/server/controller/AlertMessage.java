package com.spring.bugs.server.controller;

import javafx.scene.control.Alert;

public class AlertMessage {
    public static void showMessage(String message, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setContentText(message);
        a.show();
    }
}

