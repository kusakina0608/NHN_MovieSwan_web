'use strict';

(function() {
    var totalPrice = document.querySelector("#total-price");
    var seatButtons = document.querySelector(".seat-table").querySelectorAll("button");
    var selectedSeat = document.querySelector("#selected-seat");

    const prevButton = document.querySelector(".prev-button");
    const nextButton = document.querySelector(".next-button");

    var adultCount = 0;
    var youngCount = 0;
    var elderCount = 0;
    var totalCount = 0;

    var selected = 0;

    const requestTicketAPI = axios.create({
        baseURL: location.origin
    });

    // 상영시간표 API에 요청
    const seatAPI = {
        preemptSeat: (timetableId, seatCode) => {
            return requestTicketAPI.post(`/api/seat/preempt?timetableId=${timetableId}&seatCode=${seatCode}`);
        },
        cancelSeat: (timetableId, seatCode) => {
            return requestTicketAPI.delete(`/api/seat/preempt?timetableId=${timetableId}&seatCode=${seatCode}`);
        }
    }

    // 좌석을 클릭했을 때의 콜백 함수를 지정
    seatButtons.forEach(btn => {
        btn.addEventListener("click", async (e) => {
            let seatId = e.target.querySelector("input").value;
            // 이미 선택되어 있는 좌석인 경우
            if(e.target.classList.contains("selected")){
                var res = await seatAPI.cancelSeat(timetableId, seatId);
                if(res.data){
                    e.target.classList.remove("selected");
                    selectedSeat.querySelector(`.${e.target.querySelector("input").value}`).remove();
                    selected--;
                }
                else{
                    alert("이미 취소된 좌석입니다.")
                }
            }
            else{ // 선택되어 있지 않은 좌석인 경우
                if(selected < totalCount){
                    console.log(`timetableId: ${timetableId}, seatId: ${seatId}`);
                    var res = await seatAPI.preemptSeat(timetableId, seatId);
                    if(res.data){
                        e.target.classList.add("selected");
                        selected++;
                        let seatLabel = document.createElement("div");
                        seatLabel.classList.add(e.target.querySelector("input").value);
                        seatLabel.innerHTML = e.target.querySelector("input").value;
                        seatLabel.classList.add("seat-label")
                        selectedSeat.appendChild(seatLabel);
                    }
                    else{
                        alert("다른 사용자가 이미 선점한 좌석입니다.");
                        e.target.classList.add("na-seat");
                        e.target.disabled = true;
                    }
                }
                else if(totalCount === 0){
                    alert("인원을 먼저 선택해 주세요");
                }
                else{
                    if(totalCount >= 10){
                        alert("좌석은 한번에 10개까지 선택할 수 있습니다.");
                    }
                    else{
                        alert("인원을 추가로 선택해 주세요");
                    }
                }
            }
        })
    })

    const adultPrice = parseInt(document.querySelector("#adultPrice").innerHTML);
    const childPrice = parseInt(document.querySelector("#youngPrice").innerHTML);
    const otherPrice = parseInt(document.querySelector("#elderPrice").innerHTML);
    console.log(adultPrice);

    var refreshCount = function() {
        adultCount = parseInt(document.querySelector("#adultCount").value);
        youngCount = parseInt(document.querySelector("#youngCount").value);
        elderCount = parseInt(document.querySelector("#elderCount").value);
        totalCount = adultCount + youngCount + elderCount;
        totalPrice.innerHTML = adultCount * adultPrice + youngCount * childPrice + elderCount * otherPrice;
    }

    document.querySelectorAll('.minus').forEach(el => {
        el.addEventListener('click', e => {
            e.preventDefault();
            var input = e.target.parentNode.querySelector('input');

            if(parseInt(input.value) > 0){
                input.value = parseInt(input.value) - 1;
            }
            else{
                input.value = 0;
            }
            refreshCount();
        });
    });

    document.querySelectorAll('.plus').forEach(el => {
        el.addEventListener('click', e => {
            e.preventDefault();
            var input = e.target.parentNode.querySelector('input');

            if(totalCount >= 10){
                alert("좌석은 한번에 10개까지 선택할 수 있습니다.");
            }
            else if(parseInt(input.value) >= 0){
                input.value = parseInt(input.value) + 1;
            }
            else{
                input.value = 0;
            }
            refreshCount();
        });
    });

    prevButton.addEventListener("click", e => {
        e.preventDefault();
        window.history.back(1);
    });
    
    nextButton.addEventListener("click", e => {
        e.preventDefault();

        let form = document.createElement("form");

        form.setAttribute("charset", "UTF-8");
        form.setAttribute("method", "Post");
        form.setAttribute("action", "/reserve/pay");

        if(selected === totalCount){
            let selectedSeatList = [];
            document.querySelectorAll(".seat-label").forEach(el => {
                selectedSeatList.push(el.innerHTML);
            });

            let timetableIdInput = document.createElement("input");
            timetableIdInput.setAttribute("type", "hidden");
            timetableIdInput.setAttribute("name", "timetableId");
            timetableIdInput.setAttribute("value", timetableId);
            form.appendChild(timetableIdInput);

            let youngInput = document.createElement("input");
            youngInput.setAttribute("type", "hidden");
            youngInput.setAttribute("name", "youngNum");
            youngInput.setAttribute("value", youngCount);
            form.appendChild(youngInput);

            let adultInput = document.createElement("input");
            adultInput.setAttribute("type", "hidden");
            adultInput.setAttribute("name", "adultNum");
            adultInput.setAttribute("value", adultCount);
            form.appendChild(adultInput);

            let elderInput = document.createElement("input");
            elderInput.setAttribute("type", "hidden");
            elderInput.setAttribute("name", "elderNum");
            elderInput.setAttribute("value", elderCount);
            form.appendChild(elderInput);

            let totalInput = document.createElement("input");
            totalInput.setAttribute("type", "hidden");
            totalInput.setAttribute("name", "totalNum");
            totalInput.setAttribute("value", totalCount);
            form.appendChild(totalInput);

            let priceInput = document.createElement("input");
            priceInput.setAttribute("type", "hidden");
            priceInput.setAttribute("name", "price");
            priceInput.setAttribute("value", Number.parseInt(totalPrice.innerHTML));
            form.appendChild(priceInput);

            let seatInput = document.createElement("input");
            seatInput.setAttribute("type", "hidden");
            seatInput.setAttribute("name", "seats");
            seatInput.setAttribute("value", selectedSeatList);
            form.appendChild(seatInput);

            document.body.appendChild(form);
            form.submit();
        }
        else{
            alert("좌석을 선택해 주세요");
        }
    });
}());
