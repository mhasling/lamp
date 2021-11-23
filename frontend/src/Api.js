
export function postJson(url : string, payload : string, headers : {}) {
    const requestMetadata = {
        method: 'POST',
        headers: {...{
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }, ...headers},
        body: JSON.stringify(payload)
    };
    return fetch(url, requestMetadata).then(function(res){ return res.json(); })
}

export function postFile(url : string, payload : string) {
    const formData = new FormData();
    formData.append("file", payload);
    const requestMetadata = {
        method: 'POST',
        body: formData
    };
    return fetch(url, requestMetadata).then(function(res){ return res.json(); })
}