export async function RegisterCall(
    uname,
    pass,
    fname,
    lname,
    genderr,
    dobb,
    addresss,
    roleid
) {
    const url = "http://localhost:8081/api/auth/register";
    let ret;
//    console.log("From connection file ", fname, lname);
    await fetch(url, {
        method: "POST",
        body: JSON.stringify({
            username: uname,
            password: pass,
            fname: fname,
            lname: lname,
            gender: genderr,
//            dob: dobb,
            address: addresss,
            role: {
                id: "1",
            },
        }),
        headers: {
            "Content-type": "application/json",
        },
    })
        .then(data => {
            ret = data.ok;
            return data.json();
        })
        .then(data => {
            // console.log(data);
        })
        .catch(err => {
            ret = false;
            // console.log(err.message);
        });
    // console.log("RET: ", ret);
    return ret;
}
