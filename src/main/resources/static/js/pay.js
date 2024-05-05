async function kakaoPayReady(){
    console.log("kakaoPayReady 실행 ...")
    debugger
    const response = await axios.post(`/payment/ready`)
    console.log(response)
    return response.data
}

