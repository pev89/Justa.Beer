$.ajax({
    url: "http://justa.beer/api/get.php",
    jsonp: "callback",
    dataType: "jsonp",
    data: {
        format: "json"
    },
    success: function( response ) {
        addJabWidget(response);
    },
    error: function( response ) {
        console.log( "ERROR", response );
    }
});