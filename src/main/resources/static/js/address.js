async function defaultAddressCheck(member){
    // 기본배송지 존재 여부 확인
    console.log("defaultAddressCheck 실행 ...")
    debugger
    const response = await axios.get(`/orders/addressDefault/${member}`)
    console.log(response)
    return response.data // data.defAddress.필드명(Address)
}

async function defaultAddressfalse(defAddressno) {
    // 기존의 기본배송지의 a_basic 필드 값을 0으로 수정
    const response = await axios.put(`/orders/addressDefault/${defAddressno}`)
    return response.data // data.defaultAddressfalse(String)
}

async function addressList(member) {
    // 멤버를 받아 배송지 리스트를 리턴
    const response = await axios.get(`/orders/addressList/${member}`)
    return response.data // data.addressDTOS(AddressDTO)
}

async function readOneAddress(ano) {
    // 배송지 번호를 받아 배송지를 리턴
    const response = await axios.get(`/orders/address/${ano}`)
    return response.data // data(AddressDTO)
}