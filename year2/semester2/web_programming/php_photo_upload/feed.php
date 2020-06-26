<?php
include "config.php";
session_start();

if (!isset($_SESSION["loggedIn"]) || $_SESSION["loggedIn"] === false) {
    header("location: login.php");
    exit;
}
$user = $_SESSION["username"];
$conn = openConnection();
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Problema 5</title>
    <link rel="stylesheet" href="css/feed.css">
</head>
<body>
<div id="header">
    <h1>Discover others</h1>
    <p><?php echo '<a href="profile.php?name=' . htmlentities($user, ENT_QUOTES, "utf-8") . '">My profile</a>' ?></p>
    <p><?php echo '<a href="logout.php">Logout</a>' ?></p>
</div>
<div id="content">
    <div id="images">
        <?php
        echo getProfiles($conn, $user);
        ?>
    </div>
</div>
</body>
</html>

