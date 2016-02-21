$.ajax({
    url: "http://justa.beer/api/get.php",
    jsonp: "callback",
    dataType: "jsonp",
    data: {
        z: Date.now(),
        url: document.URL,
        domain: window.location.host
    },
    success: function( response ) {
        addJabWidget(response);
    },
    error: function( response ) {
        console.log( "ERROR", response );
    }
});