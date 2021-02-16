'use strict';

(function() {
    var totalPrice = document.querySelector("#total-price");
    var seatButtons = document.querySelector(".seat-table").querySelectorAll("button");
    var selectedSeat = document.querySelector("#selected-seat");

    const nextButton = document.querySelector(".next-button");

    var adultCount = 0;
    var childCount = 0;
    var otherCount = 0;
    var totalCount = 0;

    var selected = 0;

    // 좌석을 클릭했을 때의 콜백 함수를 지정
    seatButtons.forEach(btn => {
        btn.addEventListener("click", e => {
            // 이미 선택되어 있는 좌석인 경우
            if(e.target.classList.contains("selected")){
                e.target.classList.remove("selected");
                selectedSeat.querySelector(`.${e.target.querySelector("input").value}`).remove();
                selected--;
            }
            else{ // 선택되어 있지 않은 좌석인 경우
                if(selected < totalCount){
                    e.target.classList.add("selected");
                    selected++;
                    let seatLabel = document.createElement("div");
                    // console.log(e.target.id);
                    seatLabel.classList.add(e.target.querySelector("input").value);
                    seatLabel.innerHTML = e.target.querySelector("input").value;
                    seatLabel.classList.add("seat-label")
                    selectedSeat.appendChild(seatLabel);
                }
                else if(totalCount === 0){
                    alert("인원을 먼저 선택해 주세요");
                }
                else{
                    alert("인원을 추가로 선택해 주세요");
                }
            }
        })
    })

    const adultPrice = 16000;
    const childPrice = 12000;
    const otherPrice = 8000;

    var showCount = function(){
        console.log(`adultCount: ${adultCount}`);
        console.log(`childCount: ${childCount}`);
        console.log(`otherCount: ${otherCount}`);
        console.log(`totalCount: ${totalCount}`);
    }

    var refreshCount = function() {
        adultCount = parseInt(document.querySelector("#adultCount").value);
        childCount = parseInt(document.querySelector("#childCount").value);
        otherCount = parseInt(document.querySelector("#otherCount").value);
        totalCount = adultCount + childCount + otherCount;
        totalPrice.innerHTML = adultCount * adultPrice + childCount * childPrice + otherCount * otherPrice;
    }

    document.querySelectorAll('.minus').forEach(el => {
        el.addEventListener('click', e => {
            e.preventDefault();
            var input = e.target.parentNode.querySelector('input');
            var count = parseInt(input.value) - 1;
            count = count < 0 ? 0 : count;
            input.value = count;
            refreshCount();
            // showCount();
        });
    });

    document.querySelectorAll('.plus').forEach(el => {
        el.addEventListener('click', e => {
            e.preventDefault();
            var input = e.target.parentNode.querySelector('input');
            input.value = parseInt(input.value) + 1;
            refreshCount();
            // showCount();
        });
    });

    nextButton.addEventListener("click", e => {
        e.preventDefault();

        let form = document.querySelector("form");

        form.setAttribute("charset", "UTF-8");
        form.setAttribute("method", "Post");
        form.setAttribute("action", "/booking/pay");

        if(selected === totalCount){
            let selectedSeatList = [];
            document.querySelectorAll(".seat-label").forEach(el => {
                selectedSeatList.push(el.innerHTML);
            });

            let seatInput = document.createElement("input");
            seatInput.setAttribute("type", "hidden");
            seatInput.setAttribute("name", "seats");
            seatInput.setAttribute("value", selectedSeatList);
            form.appendChild(seatInput);

            let childInput = document.createElement("input");
            childInput.setAttribute("type", "hidden");
            childInput.setAttribute("name", "childnum");
            childInput.setAttribute("value", childCount);
            form.appendChild(childInput);

            let adultInput = document.createElement("input");
            adultInput.setAttribute("type", "hidden");
            adultInput.setAttribute("name", "adultnum");
            adultInput.setAttribute("value", adultCount);
            form.appendChild(adultInput);

            let otherInput = document.createElement("input");
            otherInput.setAttribute("type", "hidden");
            otherInput.setAttribute("name", "oldnum");
            otherInput.setAttribute("value", otherCount);
            form.appendChild(otherInput);

            let totalInput = document.createElement("input");
            totalInput.setAttribute("type", "hidden");
            totalInput.setAttribute("name", "totalnum");
            totalInput.setAttribute("value", totalCount);
            form.appendChild(totalInput);

            let priceInput = document.createElement("input");
            priceInput.setAttribute("type", "hidden");
            priceInput.setAttribute("name", "price");
            priceInput.setAttribute("value", Number.parseInt(totalPrice.innerHTML));
            form.appendChild(priceInput);
            form.submit();
        }
        else{
            alert("좌석을 선택해 주세요");
        }
    });
}());