<?php
include "config.php";
session_start();

if (isset($_SESSION["loggedIn"]) && $_SESSION["loggedIn"] === true) {
    header("location: feed.php");
    exit;
}
$error = "";
$username = "";
$password = "";

if (isset($_POST['submit'])) {
    $conn = openConnection();
    $username = sanitize_string($_POST["username"]);
    $password = sanitize_string($_POST["password"]);
    if (empty($username) || empty($password)) {
        $error = "Invalid username or password";
    }
    if ($error == "") {
        $stmt = $conn->prepare("SELECT * FROM user WHERE username=:username AND password=:password");
        $stmt->bindParam(':username', $username);
        $stmt->bindParam(':password', $password);
        $stmt->execute();
        if ($stmt->rowCount() == 0) {
            $error = "Invalid username or password";
        } else {
            $result = $stmt->fetchAll()[0];
            session_start();
            $_SESSION["loggedIn"] = true;
            $_SESSION["id"] = $result['id'];
            $_SESSION["username"] = $result['username'];
            header("location: feed.php");
        }
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Problema 5</title>
    <link rel="stylesheet" href="css/pb5.css">
</head>
<body>
<div id="content">
    <form method="post" action="<?php echo htmlentities($_SERVER["PHP_SELF"], ENT_QUOTES, "utf-8") ?>">
        <p>Login</p>
        <label>
            Username
            <input type="text" name="username">
        </label>
        <div>
            <label>
                Password
                <input type="password" name="password" autocomplete="off">
            </label>
        </div>
        <input id="login" name="submit" type="submit" value="Login">
        <div><?php echo htmlentities($error, ENT_QUOTES, "utf-8") ?></div>
    </form>
</div>
</body>
</html>