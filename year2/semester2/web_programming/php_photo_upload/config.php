<?php
function openConnection()
{
    $servername = "localhost:3307";
    $username = "root";
    $password = "";
    $dbname = "pw_lab7";

    try {
        $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        return $conn;
    } catch (PDOException $e) {
        die("ERROR: Could not connect. " . $e->getMessage());
    }
}

function sanitize_string($data)
{
    $data = trim($data);
    $data = stripslashes($data);
    $data = filter_var($data, FILTER_SANITIZE_STRING);
    return $data;
}

function getImages($conn, $name, $delete)
{
    try {
        $stmt = $conn->prepare("SELECT * FROM images WHERE user_name=:user");
        $stmt->bindParam(':user', $name);
        $stmt->execute();
        $count = $stmt->rowCount();
        $res = "";
        if ($count > 0) {
            while ($row = $stmt->fetch()) {
                $file = "uploads\\" . htmlentities($row['name'], ENT_QUOTES, "utf-8");
                $id = htmlentities($row['id'], ENT_QUOTES, "utf-8");
                if ($delete) {
                    $res = $res . '<div class="image">';
                    $res = $res . '<img src="' . $file . '">';
                    $res = $res . '<div><a href="deleteImage.php?id=' . $id . '"/><button>Delete</button></a></div>';
                    $res = $res . '</div>';
                } else {
                    $res = $res . '<img src="' . $file . '">';
                }
            }
            unset($result);
        }
        return $res;
    } catch (PDOException $e) {
        die("ERROR: " . $e->getMessage());
    }
}

function getImageUrlById($conn, $id)
{
    try {
        $stmt = $conn->prepare("SELECT * FROM images WHERE id=:id");
        $stmt->bindParam(':id', $id);
        $stmt->execute();
        if ($stmt->rowCount() > 0) {
            return "uploads\\" . $stmt->fetch()['name'];
        }
        return 0;
    } catch (PDOException $e) {
        die("ERROR: " . $e->getMessage());
    }
}

function userExists($conn, $username)
{
    try {
        $stmt = $conn->prepare("SELECT * FROM user WHERE username=:user");
        $stmt->bindParam(':user', $username);
        $stmt->execute();
        if ($stmt->rowCount() > 0) {
            return true;
        }
        return false;
    } catch (PDOException $e) {
        die("ERROR: " . $e->getMessage());
    }
}

function getImageUserById($conn, $id)
{
    try {
        $stmt = $conn->prepare("SELECT * FROM images WHERE id=:id");
        $stmt->bindParam(':id', $id, PDO::PARAM_INT);
        $stmt->execute();
        if ($stmt->rowCount() > 0) {
            return $stmt->fetch()['user_name'];
        }
        return 0;
    } catch (PDOException $e) {
        die("ERROR: " . $e->getMessage());
    }
}

function getProfiles($conn, $user)
{
    try {
        $stmt = $conn->prepare("SELECT * FROM user WHERE username!=:user");
        $stmt->bindParam(':user', $user);
        $stmt->execute();
        $count = $stmt->rowCount();
        $res = "";
        if ($count > 0) {
            while ($row = $stmt->fetch()) {
                $username = htmlentities($row['username'], ENT_QUOTES, "utf-8");
                $res = $res . '<div class="user"><a href="profile.php?name=' . $username . '"/>' . $username . '</a></div>';
            }
            unset($result);
        }
        return $res;
    } catch (PDOException $e) {
        die("ERROR: " . $e->getMessage());
    }
}

function getNrImagesUser($conn, $user)
{
    try {
        $stmt = $conn->prepare("SELECT * FROM images WHERE user_name=:user");
        $stmt->bindParam(':user', $user);
        $stmt->execute();
        return $stmt->rowCount();
    } catch (PDOException $e) {
        die("ERROR: " . $e->getMessage());
    }

}