let revealedCards;
let selectedCards;

function verifyMatching(card) {
    if (revealedCards.indexOf(card) >= 0 || selectedCards.indexOf(card) >= 0) {
        return;
    }
    let len = selectedCards.length;
    if (len === 0) {
        selectedCards.push(card);
        return;
    }
    let card2 = selectedCards[0];
    if (card.children[0].src === card2.children[0].src) {
        revealedCards.push(card);
        revealedCards.push(card2);
        selectedCards = [];
        verifyWin();
        return;
    }
    selectedCards = [];
    setTimeout(function () {
        hideCards(card, card2)
    }, 300);

}

function verifyWin() {
    if (revealedCards.length === document.getElementsByClassName("card").length) {
        document.getElementById("message").innerHTML = "You won!";
    }
}

function hideCards(card1, card2) {
    card1.children[0].style.visibility = "hidden";
    card2.children[0].style.visibility = "hidden";
}

function displayCard() {
    this.children[0].style.visibility = "visible";
    verifyMatching(this);
}

function shuffleElements(elements) {
    for (let i = elements.length - 1; i > 0; i--) {
        let j = Math.floor(Math.random() * i);
        let temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
    }
}

function start() {
    const board = document.getElementById("board");
    const message = document.getElementById("message");
    message.innerHTML = "";
    board.innerHTML = "";
    revealedCards = [];
    selectedCards = [];
    const cardElems = [];
    const imgSrc =
        ["images/apple.png", "images/banana.png", "images/grapes.png", "images/pear.png", "images/watermelon.png", "images/cherries.png", "images/pineapple.png", "images/peach.png"];
    for (let i = 0; i < imgSrc.length; i++) {
        const childElem1 = document.createElement("img");
        childElem1.setAttribute("class", "card-elem");
        childElem1.src = imgSrc[i];
        cardElems.push(childElem1);
        const childElem2 = document.createElement("img");
        childElem2.setAttribute("class", "card-elem");
        childElem2.src = imgSrc[i];
        cardElems.push(childElem2);
    }
    shuffleElements(cardElems);
    for (let i = 0; i < 16; i++) {
        const parentDiv = document.createElement("div");
        parentDiv.setAttribute("class", "card");
        parentDiv.appendChild(cardElems[i]);
        parentDiv.addEventListener("click", displayCard);
        board.appendChild(parentDiv);
    }
}
