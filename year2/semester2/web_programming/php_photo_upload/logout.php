<?php
session_start();
if (!(isset($_SESSION["loggedIn"])) || $_SESSION["loggedIn"] === false) {
    header("location: login.php");
    exit;
}
session_destroy();
header("location: login.php");
