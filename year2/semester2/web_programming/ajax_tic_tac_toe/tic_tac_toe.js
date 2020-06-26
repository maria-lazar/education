let board = [];
let player;
let computer;

function gameWon(p) {
    for (let i = 0; i < board.length; i++) {
        $(board[i]).unbind("click", blockClicked);
    }
    let message;
    if (p === "draw") {
        message = "It's a draw!"
    }
    if (p === player) {
        message = "Player won!";
    }
    if (p === computer) {
        message = "Computer won!";
    }
    $("#message").html(message);
}

function updateGameState(data) {
    let newBoard = data.board;
    for (let i = 0; i < board.length; i++) {
        $(board[i]).html(newBoard[i]);
    }
    if (data.winner !== "") {
        gameWon(data.winner);
    }
}

function computerMove() {
    let arrayBoard = board.map(x => x.innerHTML);
    let json = JSON.stringify({you: computer, board: arrayBoard});
    $.post("computer.php", json, updateGameState, "json");
}


function blockClicked() {
    let block = event.target;
    let indexBlock = board.indexOf(block);
    if ($(board[indexBlock]).html() !== "") {
        return;
    }
    $(board[indexBlock]).html(player);
    computerMove()
}


function restart() {
    $("#message").html("");
    for (let i = 0; i < board.length; i++) {
        $(board[i]).html("");
        $(board[i]).unbind("click", blockClicked);
    }
    start();
}

function start() {
    for (let i = 0; i < board.length; i++) {
        $(board[i]).click(blockClicked);
    }
    let rand = Math.random();
    if (rand < 0.5) {
        $("#players").html("Player: X<br>Computer: O");
        player = "X";
        computer = "O";
    } else {
        $("#players").html("Player: O<br>Computer: X");
        player = "O";
        computer = "X";
        computerMove();
    }
}

$(document).ready(function () {
    $("#restart").click(restart);
    board = [...$("#board").find("tr").find("td")];
    start();
});
