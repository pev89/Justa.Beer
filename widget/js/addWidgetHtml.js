function addJabWidget(response) {
    $("head").append("<style><<<INSERT_SYLES_HERE>>></style>");
    $("body").append("" +
        "<div class=\"jsb-widget widget-default widget-fixed widget-right\">" +
            "<div class=\"icon-container\">" +
                "<div class=\"icon-img\" style=\"background: url('" + response.icon_url + "')\"></div>" +
            "</div>" +
            "<div class=\"text-container\">" +
                "<p>" +
                 response.content_text + " It's just " + response.price + "$." +
                "</p>" +
                "<button type=\"button\" class=\"btn btn-primary\">Buy it</button>" +
            "</div>" +
        "</div>");
}
