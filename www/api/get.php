<?php
    header('Content-Type: application/javascript; charset=utf-8');
    if (!isset($BASEDIR))
        $BASEDIR = '../';

    require_once $BASEDIR . 'layers/filter.php';

    $data = $array = array(
        "icon_url" => "http://emoji.fileformat.info/gemoji/beer.png",
        "content_text" => "Buy me a beer!",
        "req_unique_id" => "3434343435_1454969529",
        "price" => 0
    );

    $json = json_encode($data);

    # JSON if no callback
    if( ! isset($_GET['callback']))
        exit($json);

    # JSONP if valid callback
    if(G_utf8::is_valid_callback($_GET['callback']))
        exit("{$_GET['callback']}($json)");

    # Otherwise, bad request
    header('status: 400 Bad Request', true, 400);
?>