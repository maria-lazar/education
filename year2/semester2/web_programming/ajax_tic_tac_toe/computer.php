<?php
function verifyGameState($b)
{
    /* rows and columns check*/
    for ($i = 0; $i < 3; $i++) {
        if (($b[3 * $i] == $b[3 * $i + 1]) && ($b[3 * $i + 1] == $b[3 * $i + 2]) && ($b[3 * $i] != "")) {
            return $b[3 * $i];
        }
        if (($b[$i] == $b[$i + 3]) && ($b[$i + 3] == $b[$i + 6]) && ($b[$i] != "")) {
            return $b[$i];
        }
    }
    /* diagonals*/
    if (($b[0] == $b[4]) && ($b[4] == $b[8]) && ($b[0] != "")) {
        return $b[0];
    }
    if (($b[2] == $b[4]) && ($b[4] == $b[6]) && ($b[2] != "")) {
        return $b[2];
    }
    /* verify draw*/
    $draw = true;
    for ($i = 0; $i < 9; $i++) {
        if ($b[$i] == "") {
            $draw = false;
            break;
        }
    }
    if ($draw) {
        return "draw";
    }
    return "";
}

$json = file_get_contents('php://input');
$json = json_decode($json);
$board = $json->board;
$computer = $json->you;
/* verify if player won or it's a draw*/
$win = verifyGameState($board);
if ($win != "") {
    $result["winner"] = $win;
    $result["board"] = $board;
    $json = json_encode($result);
    echo $json;
} else {
    /* computer moves randomly*/
    $index = array_rand($board);
    while ($board[$index] != "") {
        $index = array_rand($board);
    }
    $board[$index] = $computer;
    /* verify if computer won or it's a draw*/
    $winner = verifyGameState($board);
    $result["winner"] = $winner;
    $result["board"] = $board;
    $json = json_encode($result);
    echo $json;
}
