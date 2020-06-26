<?php
include "config.php";
session_start();

if (!isset($_SESSION["loggedIn"]) || $_SESSION["loggedIn"] === false) {
    header("location: login.php");
    exit;
}

$conn = openConnection();
$upload = false;
$error = "";
$username = $_SESSION["username"];
if ($_SERVER['REQUEST_METHOD'] == "GET") {
    if (!isset($_GET['name'])) {
        $upload = true;
    } else {
        $username = sanitize_string($_GET['name']);
        if ($username == $_SESSION["username"]) {
            $upload = true;
        }
        if (userExists($conn, $username) == false){
            $username = "User not found";
        }
    }
}
if (isset($_POST['submit'])) {
    $upload = true;
    //image validation
    $image = "";
    $valid = false;
    if (count($_FILES) > 0) {
        if (is_uploaded_file($_FILES['image']['tmp_name'])) {
            $valid = true;
            $image = $_FILES['image']['tmp_name'];
            $name = sanitize_string($_FILES['image']['name']);
            $imageProperties = getimageSize($image);
            if ($imageProperties === false) {
                $valid = false;
            }
            $allowedTypes = array(IMAGETYPE_PNG, IMAGETYPE_JPEG, IMAGETYPE_GIF);
            $detectedType = exif_imagetype($image);
            if (!in_array($detectedType, $allowedTypes)) {
                $valid = false;
            }
        }
    }
    if ($valid == false) {
        $error = "Invalid image";
    } else {
        try {
            $count = getNrImagesUser($conn, $username) + 1;
            $target_dir = "uploads/";
            $myName = $username . $count . $name;
            $target_file = $target_dir . basename($myName);
            move_uploaded_file($image, $target_file);

            $stmt = $conn->prepare("INSERT INTO images(type, name, user_name) VALUES(:type, :name, :user_name)");
            $stmt->bindParam(':type', $imageProperties['mime']);
            $stmt->bindParam(':name', $myName);
            $stmt->bindParam(':user_name', $username);
            $stmt->execute();
        } catch (PDOException $e) {
            die("ERROR: " . $e->getMessage());
        }
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Problema 5</title>
    <link rel="stylesheet" href="css/pb5profile.css">
</head>
<body>
<h1><?php echo htmlentities($username, ENT_QUOTES, "utf-8") ?></h1>
<div id="content">
    <?php
    if ($upload) {
        echo '<div><form method="post" action="profile.php"  enctype="multipart/form-data">
                <h3>Upload image</h3>
                <label>
                    Select image
                    <input type="file" name="image" >
                </label>
                <input id="upload" type="submit" value="Upload" name="submit">
                <div>' . htmlentities($error, ENT_QUOTES, "utf-8") . '</div>
                </form></div>';
    }
    ?>
    <h2>Images</h2>
    <div id="images">
        <?php
        if ($upload) {
            echo getImages($conn, $username, true);
        } else {
            echo getImages($conn, $username, false);
        }
        ?>
    </div>
</div>
<a id="back" href="feed.php">
    <button>Back</button>
</a>
</body>
</html>
