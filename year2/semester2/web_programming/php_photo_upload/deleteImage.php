<?php
include "config.php";
session_start();

if (!isset($_SESSION["loggedIn"]) || $_SESSION["loggedIn"] === false) {
    header("location: login.php");
    exit;
}
$conn = openConnection();
if (isset($_GET["id"])) {
    $delete = true;
    $id = sanitize_string($_GET['id']);
    $name = $_SESSION["username"];
    if (filter_var($id, FILTER_VALIDATE_INT) === false) {
        $delete = false;
    } else {
        $userImage = getImageUserById($conn, $id);
        if ($userImage != $name){
            $delete = false;
        }
    }
    if ($delete == true) {
        try {
            $url = getImageUrlById($conn, $id);
            if ($url !== 0) {
                $stmt = $conn->prepare("DELETE FROM images WHERE id=:id");
                $stmt->bindParam(':id', $id);
                $stmt->execute();
                unlink($url);
            }
        } catch (PDOException $e) {
            die("ERROR: " . $e->getMessage());
        }
    }
}
header("location: profile.php");
?>
