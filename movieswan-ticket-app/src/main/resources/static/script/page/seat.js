'use strict';

(function() {
    var totalPrice = document.querySelector("#total-price");
    var seatButtons = document.querySelector(".seat-table").querySelectorAll("button");
    var selectedSeat = document.querySelector("#selected-seat");

    const prevButton = document.querySelector(".prev-button");
    const nextButton = document.querySelector(".next-button");

    const timetableId = document.querySelector("#main-container > form > input[type=hidden]:nth-child(4)").value;

    var adultCount = 0;
    var childCount = 0;
    var otherCount = 0;
    var totalCount = 0;

    var selected = 0;

    const requestTicketAPI = axios.create({
        baseURL: "http://movieswan.nhnent.com/movie"
    });

    // 상영시간표 API에 요청
    const seatAPI = {
        preemptSeat: (tid, sid) => {
            return requestTicketAPI.post(`/api/seat/preempt?tid=${tid}&sid=${sid}`);
        },
        cancelSeat: (tid, sid) => {
            return requestTicketAPI.delete(`/api/seat/preempt?tid=${tid}&sid=${sid}`);
        }
    }

    // 좌석을 클릭했을 때의 콜백 함수를 지정
    seatButtons.forEach(btn => {
        btn.addEventListener("click", async (e) => {
            let seatId = e.target.querySelector("input").value;
            // 이미 선택되어 있는 좌석인 경우
            if(e.target.classList.contains("selected")){
                var res = await seatAPI.cancelSeat(timetableId, seatId);
                console.log(res);
                if(res.data){
                    console.log("선점 취소 성공!");
                    e.target.classList.remove("selected");
                    selectedSeat.querySelector(`.${e.target.querySelector("input").value}`).remove();
                    selected--;
                }
                else{
                    console.log("이미 취소된 좌석입니다.");
                }
            }
            else{ // 선택되어 있지 않은 좌석인 경우
                if(selected < totalCount){
                    var res = await seatAPI.preemptSeat(timetableId, seatId);
                    console.log(res.data);
                    if(res.data){
                        // console.log("선 점 성 공");
                        e.target.classList.add("selected");
                        selected++;
                        let seatLabel = document.createElement("div");
                        seatLabel.classList.add(e.target.querySelector("input").value);
                        seatLabel.innerHTML = e.target.querySelector("input").value;
                        seatLabel.classList.add("seat-label")
                        selectedSeat.appendChild(seatLabel);
                    }
                    else{
                        // console.log("선 점 실 패");
                        alert("다른 사용자가 이미 선점한 좌석입니다.");
                        e.target.classList.add("na-seat");
                        e.target.disabled = true;
                    }
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

    prevButton.addEventListener("click", e => {
        e.preventDefault();
        window.history.back(1);
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
