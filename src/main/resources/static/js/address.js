async function defaultAddressCheck(member){
    // 기본배송지 존재 여부 확인
    console.log("defaultAddressCheck ...")
    debugger
    const response = await axios.get(`/orders/addressDefault/${member}`)
    console.log(response)
    return response.data
}

async function defaultAddressfalse(defAddressno) {
    // 기존의 기본배송지의 a_basic 필드 값을 0으로 수정
    const response = await axios.put(`/orders/addressDefault/${defAddressno}`)
    return response.data
}